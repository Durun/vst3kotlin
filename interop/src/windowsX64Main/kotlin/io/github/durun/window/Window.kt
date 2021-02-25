package io.github.durun.window

import kotlinx.cinterop.*
import platform.windows.*

class Window(
    val name: String,
    val hwnd: HWND
) {
    companion object {
        fun create(name: String, windowClass: WindowClass, instance: HINSTANCE? = null): Window {
            val hwnd = memScoped {
                val parent: HWND? = null
                val menu: HMENU? = null
                val hwnd = CreateWindowEx?.invoke(
                    WS_EX_APPWINDOW.toUInt(),
                    windowClass.name.ptr,
                    name.wcstr.ptr,
                    (DwStyle.VISIBLE + DwStyle.CAPTION + DwStyle.SYSMENU).value,
                    0, 0,
                    480, 320,
                    parent, menu, instance, null
                )
                checkNotNull(hwnd)
                hwnd
            }
            return Window(name, hwnd)
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

    fun show() {
        ShowWindow(hwnd, SW_SHOW)
    }

    fun getMessage() = Companion.getMessage(hwnd)
}

sealed class DwStyle(private val flags: Int) {
    val value: UInt get() = flags.toUInt()
    operator fun plus(other: DwStyle): DwStyle = Combined(this.flags or other.flags)

    class Combined(flags: Int) : DwStyle(flags)
    object VISIBLE : DwStyle(WS_VISIBLE)
    object CAPTION : DwStyle(WS_CAPTION)
    object SYSMENU : DwStyle(WS_SYSMENU)
}

sealed class WindowClass(val name: CValues<WCHARVar>) {
    object STATIC : WindowClass("STATIC".wcstr)
    object BUTTON : WindowClass("BUTTON".wcstr)

    object Simple : WindowClass("Simple".wcstr) {
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

}