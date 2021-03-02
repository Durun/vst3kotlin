package io.github.durun.vst3kotlin.window

import io.github.durun.log.logger
import io.github.durun.util.Vec2
import io.github.durun.util.toUInt
import kotlinx.cinterop.*
import platform.posix.usleep
import x11.*

actual class Window(
	private val window: x11.Window,
	private val parentWindow: x11.Window,
	private val display: CPointer<Display>
) {
	actual val ptr: COpaquePointer = window.toLong().toCPointer()!!
	actual fun show() {
		XMapWindow(display, window)
	}

	actual fun loop(continueNext: (WindowEvent) -> Boolean) {
		var running = true
		memScoped {
			val event: XEvent = alloc()
			do {
				if (0 < XPending(display)) {
					XNextEvent(display, event.ptr)
					running = continueNext(event.toKEvent())
				} else usleep(1000)
			} while (running)
		}
	}

	actual fun resize(width: Int, height: Int) {
		XResizeWindow(display, window, width, height)
	}

	actual companion object {
		private val log by logger()
		actual val platformType: String = "X11EmbedWindowID"
		actual fun create(size: Vec2<Int>, name: String): Window {
			val uSize = size.toUInt()
			val resizeable = true
			val display = XOpenDisplay(null) ?: error("Can't open display")
			log.info { "Opened display" }

			XInternAtom(display, "_XEMBED_INFO", True)
				.also { check(it != None.toULong()) { "_XEMBED_INFO does not exist" } }

			val numScreen = XDefaultScreen(display)
			val displaySize = Vec2(XDisplayWidth(display, numScreen), XDisplayHeight(display, numScreen)).toUInt()
			val borderWidth = 1u

			val attrMask = CWBackPixel or CWColormap or CWBorderPixel
			// window
			val window: x11.Window = memScoped {
				val vInfo: XVisualInfo = alloc()
				XMatchVisualInfo(display, numScreen, 24, TrueColor, vInfo.ptr).also { check(it != 0) }
				val attr: XSetWindowAttributes = alloc<XSetWindowAttributes>()
					.apply {
						border_pixel = XBlackPixel(display, numScreen)
						background_pixel = XWhitePixel(display, numScreen)
						colormap = XCreateColormap(display, XDefaultRootWindow(display), vInfo.visual, AllocNone)
					}

				XCreateWindow(
					display, XRootWindow(display, numScreen),
					0, 0, displaySize.x, displaySize.y,
					borderWidth, vInfo.depth, InputOutput, vInfo.visual, attrMask.toULong(), attr.ptr
				)
			}
			// window setting
			val sizeHints = (XAllocSizeHints() ?: error("failed to alloc"))
				.pointed.apply {
					flags = PMinSize
					if (!resizeable) {
						flags = flags or PMaxSize
						min_width = size.x
						max_width = size.x
						min_height = size.y
						max_height = size.y
					} else {
						min_width = 80
						min_height = 80
					}
				}
			XSetWMNormalHints(display, window, sizeHints.ptr)
			XFree(sizeHints.ptr)

			XStoreName(display, window, "window title")

			val gc = XCreateGC(display, window, 0, null)
			XSetForeground(display, gc, XWhitePixel(display, numScreen))
			XSetBackground(display, gc, XBlackPixel(display, numScreen))
			log.info { "Set GraphicContext" }

			// parent window
			val parentWindow: x11.Window = memScoped {
				val vInfo: XVisualInfo = alloc()
				XMatchVisualInfo(display, numScreen, 24, TrueColor, vInfo.ptr).also { check(it != 0) }

				val iconName: XTextProperty = alloc()
				val iconNameBuf: CPointerVar<ByteVar> = alloc()
				XStringListToTextProperty(iconNameBuf.ptr, 1, iconName.ptr)

				val wmDeleteWindow: AtomVar = alloc()
				wmDeleteWindow.value = XInternAtom(display, "WM_DELETE_WINDOW", False)
				XSetWMProtocols(display, window, wmDeleteWindow.ptr, 1)

				val attr: XSetWindowAttributes = alloc<XSetWindowAttributes>()
					.apply {
						override_redirect = True
						event_mask =
							ExposureMask or KeyPressMask or ButtonPressMask or ButtonReleaseMask or ButtonMotionMask
					}

				XCreateWindow(
					display, window, 0, 0, uSize.x, uSize.y, borderWidth,
					vInfo.depth, InputOutput, null, attrMask.toULong(), attr.ptr
				)
			}
			XFlush(display)
			XResizeWindow(display, window, uSize.x, uSize.y)
			XSelectInput(
				display, window,
				ExposureMask or StructureNotifyMask or SubstructureNotifyMask or FocusChangeMask
			)
			XSelectInput(display, parentWindow, SubstructureNotifyMask or PropertyChangeMask)
			return Window(window, parentWindow, display)
		}
	}
}