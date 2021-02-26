package io.github.durun.log

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object Log {
    enum class Level(val importance: Int) {
        VERBOSE(0) {
            override fun toString() = "VERBOSE"
        },
        DEBUG(1) {
            override fun toString() = "DEBUG"
        },
        INFO(2) {
            override fun toString() = "INFO"
        },
        WARN(3) {
            override fun toString() = "WARN"
        },
        ERROR(4) {
            override fun toString() = "ERROR"
        },
        ASSERT(10) {
            override fun toString() = "ASSERT"
        }
    }
}

var LogLevel: Log.Level = Log.Level.INFO

class Logger(val tag: String?) {
    private fun log(message: String, level: Log.Level, tag: String?) {
        println("[$level] $message @ $tag")
    }

    fun error(message: () -> Any?) {
        if (Log.Level.ERROR.importance <= LogLevel.importance)
            log("${message()}", Log.Level.ERROR, tag)
    }

    fun warn(message: () -> Any?) {
        if (Log.Level.WARN.importance <= LogLevel.importance)
            log("${message()}", Log.Level.WARN, tag)
    }

    fun info(message: () -> Any?) {
        if (Log.Level.INFO.importance <= LogLevel.importance)
            log("${message()}", Log.Level.INFO, tag)
    }

    fun debug(message: () -> Any?) {
        if (Log.Level.DEBUG.importance <= LogLevel.importance)
            log("${message()}", Log.Level.DEBUG, tag)
    }

    fun verbose(message: () -> Any?) {
        if (Log.Level.VERBOSE.importance <= LogLevel.importance)
            log("${message()}", Log.Level.VERBOSE, tag)
    }

    fun assert(message: () -> Any?) {
        log("${message()}", Log.Level.ASSERT, tag)
    }
}

class LoggerProperty : ReadOnlyProperty<Any, Logger> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        val tag = "$thisRef"
        return Logger(tag)
    }
}

fun logger(): ReadOnlyProperty<Any, Logger> = LoggerProperty()

expect fun println()