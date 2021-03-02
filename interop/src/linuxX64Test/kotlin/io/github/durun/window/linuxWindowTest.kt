package io.github.durun.window

import io.github.durun.io.use
import io.github.durun.util.Vec2
import io.github.durun.util.toUInt
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.gui.PlatformView
import io.github.durun.vst3kotlin.hosting.ControllerInstance
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import kotlinx.cinterop.*
import platform.posix.usleep
import x11.*
import kotlin.test.Test

const val SIZE = 800

class LinuxWindowTest {

	/*
	//@Test
	fun directWindowTest() {
		val display = XOpenDisplay(null) ?: error("Can't open display")
		val screenNumber = XDefaultScreen(display)
		val screen = XDefaultScreenOfDisplay(display) ?: error("Can't detect default screen")
		val screenWidth = screen.pointed.width.toUInt()
		val screenHeight = screen.pointed.height.toUInt()

		val window = memScoped {
			val visualInfo = alloc<XVisualInfo>()
			XMatchVisualInfo(display, screenNumber, 32, TrueColor, visualInfo.ptr)

			val attrs = alloc<XSetWindowAttributes> {
				override_redirect = 1
				colormap = XCreateColormap(display, XDefaultRootWindow(display), visualInfo.visual, AllocNone)
				border_pixel = 0UL
				background_pixel = 0xffffffUL
			}.ptr
			XCreateWindow(
				display,
				XRootWindow(display, screenNumber),
				0,
				0,
				SIZE.toUInt(),
				SIZE.toUInt(),
				0,
				visualInfo.depth,
				CopyFromParent.toUInt(),
				visualInfo.visual,
				(CWColormap or CWBorderPixel or CWBackPixel or CWOverrideRedirect).toULong(),
				attrs
			)
		}
		XMapWindow(display, window)
		XRaiseWindow(display, window)
		XFlush(display)

		XSelectInput(
			display,
			window,
			PointerMotionMask or ButtonPressMask or ButtonReleaseMask or KeyPressMask or KeyReleaseMask
		)

		val gc = memScoped {
			val values = alloc<XGCValues> { graphics_exposures = False }.ptr
			XCreateGC(display, window, 0UL, values)
		}

		memScoped {
			val event = alloc<XEvent>()
			while (true) {
				XNextEvent(display, event.ptr)

				XSetForeground(display, gc, 0xdd000000UL)
				XFillRectangle(display, window, gc, 0, 0, screenWidth, screenHeight)
				if (event.type == MotionNotify) {
					XSetForeground(display, gc, 0x00000000UL)
					XFillArc(
						display, window, gc,
						event.xmotion.x - SIZE / 2, event.xmotion.y - SIZE / 2,
						SIZE.toUInt(), SIZE.toUInt(),
						0, 360 * 64
					)
					XFlush(display)
				}
			}
		}
	}

	*/

	@Test
	fun test() {
		val sizeX = 480
		val sizeY = 320
		val resizeable = true
		memScoped {
			val display = XOpenDisplay(null) ?: error("Can't open display")

			val xEmbedInfoAtom = XInternAtom(display, "_XEMBED_INFO", true.toByte().toInt())
			check(xEmbedInfoAtom != None.toULong()) { "_XEMBED_INFO does not exist" }

			val numScreen = XDefaultScreen(display)
			val displayWidth = XDisplayWidth(display, numScreen).toUInt()
			val displayHeight = XDisplayHeight(display, numScreen).toUInt()
			val borderWidth = 1u

			val vInfo: XVisualInfo = alloc<XVisualInfo>()
			XMatchVisualInfo(display, numScreen, 24, TrueColor, vInfo.ptr)
				.also { check(it != 0) }

			val attr: XSetWindowAttributes = alloc<XSetWindowAttributes>()
				.apply {
					border_pixel = XBlackPixel(display, numScreen)
					background_pixel = XWhitePixel(display, numScreen)
					colormap = XCreateColormap(display, XDefaultRootWindow(display), vInfo.visual, AllocNone)
				}
			val attrMask = CWBackPixel or CWColormap or CWBorderPixel
			val window = XCreateWindow(
				display, XRootWindow(display, numScreen),
				0, 0, displayWidth, displayHeight,
				borderWidth, vInfo.depth, InputOutput, vInfo.visual, attrMask.toULong(), attr.ptr
			)
			XFlush(display)

			XResizeWindow(display, window, sizeX.toUInt(), sizeY.toUInt())

			XMapWindow(display, window)
			XSelectInput(
				display, window,
				ExposureMask or StructureNotifyMask or SubstructureNotifyMask or FocusChangeMask
			)

			val sizeHints: CPointer<XSizeHints> = XAllocSizeHints()
				?: throw Exception("Failed to alloc XSizeHints")
			sizeHints.pointed.apply {
				flags = PMinSize
				if (!resizeable) {
					flags = flags or PMaxSize
					min_width = sizeX
					max_width = sizeX
					min_height = sizeY
					max_height = sizeY
				} else {
					min_width = 80
					min_height = 80
				}
			}
			XSetWMNormalHints(display, window, sizeHints)
			XFree(sizeHints)
			XStoreName(display, window, "Window Title")

			println("loop start")
			memScoped {
				val event: XEvent = alloc<XEvent>()
				var loop = true
				while (loop) {
					while (0 < XPending(display)) {
						XNextEvent(display, event.ptr)
						println(event.type)
						when (event.type) {
							DestroyNotify -> loop = false
						}
					}
					usleep(1000)
				}
			}
		}
	}

	@Test
	fun wrapperWindow() {
		val size = Vec2(480, 320)
		val window = Window.create(size.toUInt(), "VST3")

		val path = testResources.resolve("vst3/Witty Audio CLIP It.vst3")

		Module.of(path).use { module ->
			println("Module open.")
			val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
			ControllerInstance.create(module.factory, classInfo.classId).use {
				println("ControllerInstance open.")
				it.plugView?.attached(PlatformView(window.window.toLong().toCPointer()!!), "X11EmbedWindowID")
				println("attached")
			}
			println("ControllerInstance closed.")



			window.show()
			println("loop start")
			while (window.getEvent() != DestroyNotify) {
				usleep(1000)
			}


		}
		println("Module closed.")


	}
}