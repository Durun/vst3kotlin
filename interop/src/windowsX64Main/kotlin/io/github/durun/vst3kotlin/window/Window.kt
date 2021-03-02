package io.github.durun.vst3kotlin.window

import io.github.durun.util.Vec2
import io.github.durun.vst3kotlin.gui.PlugView
import kotlinx.cinterop.*
import platform.windows.*

actual class Window(
    private val hwnd: HWND
) {
    actual val ptr: COpaquePointer = hwnd
    actual companion object {
        actual val platformType: String = "HWND"
        actual fun create(size: Vec2<Int>, name: String): Window {
            val window = create(name, WindowClass.Vst(null))
            window.resize(size.x, size.y)
            return window
        }

        fun create(name: String, windowClass: WindowClass, instance: HINSTANCE? = null): Window {
            val hwnd = memScoped {
                val parent: HWND? = null
                val menu: HMENU? = null
                val resizable = DwStyle.SIZEBOX + DwStyle.MAXIMIZEBOX
                val hwnd = CreateWindowEx?.invoke(
                    WS_EX_APPWINDOW.toUInt(),
                    windowClass.name.ptr,
                    name.wcstr.ptr,
                    (DwStyle.CAPTION + DwStyle.SYSMENU + DwStyle.CLIPCHILDREN + DwStyle.CLIPSIBLINGS + resizable).value,
                    CW_USEDEFAULT, CW_USEDEFAULT,
                    CW_USEDEFAULT, CW_USEDEFAULT,
                    parent, menu, instance, null
                )
                checkNotNull(hwnd)
                hwnd
            }
            return Window(hwnd)
        }

        private fun getMessage(hwnd: HWND?): tagMSG {
            return memScoped {
                val message = alloc<tagMSG>()
                GetMessageA(message.ptr, hwnd, 0, 0)
                DispatchMessageA(message.ptr)
                message
            }
        }

        fun getMessageForAll() = getMessage(null)
    }

    fun attach(plugView: PlugView) {
        val size = plugView.size
        val width = size.right - size.left
        val height = size.bottom - size.top
        resize(width, height)
        plugView.attached(this)
    }

    actual fun show() {
        ShowWindow(hwnd, SW_SHOW)
    }

    actual fun resize(width: Int, height: Int) {
        SetWindowPos(
            hwnd, HWND_TOP,
            0, 0,
            width, height,
            (SWP_NOMOVE or SWP_NOCOPYBITS).toUInt()
        )
    }

    actual fun loop(continueNext: (WindowEvent) -> Boolean) {
        do {
            val event = getMessage(hwnd).toKEvent()
        } while (continueNext(event))
    }
}

sealed class DwStyle(private val flags: Int) {
    val value: UInt get() = flags.toUInt()
    operator fun plus(other: DwStyle): DwStyle = Combined(this.flags or other.flags)

    class Combined(flags: Int) : DwStyle(flags)
    object VISIBLE : DwStyle(WS_VISIBLE)
    object CAPTION : DwStyle(WS_CAPTION)
    object SYSMENU : DwStyle(WS_SYSMENU)
    object CLIPCHILDREN : DwStyle(WS_CLIPCHILDREN)
    object CLIPSIBLINGS : DwStyle(WS_CLIPSIBLINGS)
    object SIZEBOX : DwStyle(WS_SIZEBOX)
    object MAXIMIZEBOX : DwStyle(WS_MAXIMIZEBOX)

}

sealed class WindowClass(val name: CValues<WCHARVar>) {
    constructor(name: String) : this(name.wcstr)

    object STATIC : WindowClass("STATIC")
    object BUTTON : WindowClass("BUTTON")

    object Simple : WindowClass("Simple") {
        init {
            val f: WNDPROC = staticCFunction { hwnd: HWND?, msg: UINT, wp: WPARAM, lp: LPARAM ->
                when (msg.toInt()) {
                    WM_DESTROY -> PostQuitMessage(0)
                }
                return@staticCFunction DefWindowProc!!.invoke(hwnd, msg, wp, lp)
            }
            memScoped {
                val winc = alloc<WNDCLASS>().apply {
                    style = (CS_HREDRAW or CS_VREDRAW).toUInt()
                    lpfnWndProc = f
                    hIcon = LoadIconW(null, IDI_APPLICATION)
                    hbrBackground = GetStockObject(WHITE_BRUSH)?.reinterpret()
                    lpszClassName = name.ptr
                }
                val result = RegisterClass?.invoke(winc.ptr)
                checkNotNull(result)
                DefWindowProc
            }
        }
    }

    class Vst(instance: HINSTANCE?) : WindowClass("VSTSDK WindowClass") {
        init {
            memScoped {
                val struct: WNDCLASSEX = alloc<WNDCLASSEX>().apply {
                    cbSize = sizeOf<WNDCLASSEX>().toUInt()
                    style = CS_DBLCLKS.toUInt()
                    lpfnWndProc = callback
                    hInstance = instance
                    hCursor = LoadCursorW(instance, IDC_ARROW)
                    lpszClassName = name.ptr
                }
                RegisterClassEx!!(struct.ptr)
            }
        }

        companion object {
            private val TRUE: Long = 1
            private val FALSE: Long = 0
            private val callback: WNDPROC = staticCFunction { hwnd: HWND?, msg: UINT, wp: WPARAM, lp: LPARAM ->
                when (msg.toInt()) {
                    WM_DESTROY -> PostQuitMessage(0)
                    WM_CLOSE -> PostQuitMessage(0)
                    WM_ERASEBKGND -> return@staticCFunction TRUE
                    WM_PAINT -> memScoped {
                        val ps: PAINTSTRUCT = alloc()
                        BeginPaint(hwnd, ps.ptr)
                        EndPaint(hwnd, ps.ptr)
                        return@staticCFunction FALSE
                    }
                }
                DefWindowProc!!.invoke(hwnd, msg, wp, lp)
            }
        }
    }

}