package io.github.durun.window

import io.github.durun.log.logger
import kotlinx.cinterop.*
import x11.*

data class Vec2<T>(val x: T, val y: T) {
	fun <R> map(transform: (T) -> R): Vec2<R> {
		return Vec2(transform(x), transform(y))
	}
}

fun Vec2<Int>.toUInt(): Vec2<UInt> = map { it.toUInt() }

class Window(
	val window: x11.Window,
	val parentWindow: x11.Window,
	val display: CPointer<Display>
) {
	companion object {
		val log by logger()
		fun create(size: Vec2<UInt>, name: String = "", resizeable: Boolean = true): Window {
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
						min_width = size.x.toInt()
						max_width = size.x.toInt()
						min_height = size.y.toInt()
						max_height = size.y.toInt()
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
					display, window, 0, 0, size.x, size.y, borderWidth,
					vInfo.depth, InputOutput, null, attrMask.toULong(), attr.ptr
				)
			}
			XFlush(display)
			XResizeWindow(display, window, size.x, size.y)
			XSelectInput(
				display, window,
				ExposureMask or StructureNotifyMask or SubstructureNotifyMask or FocusChangeMask
			)
			XSelectInput(display, parentWindow, SubstructureNotifyMask or PropertyChangeMask)
			return Window(window, parentWindow, display)
		}
	}

	fun show() {
		XMapWindow(display, window)
	}

	fun getEvent(): Int? = memScoped {
		val event: XEvent = alloc<XEvent>()
		if (0 < XPending(display)) {
			XNextEvent(display, event.ptr)
			event.type
		} else null
	}
}