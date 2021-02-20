package io.github.durun.util


interface MessageDecoder {
	fun <T : MessageBase> decode(reader: ByteArrayReader): T

	@OptIn(ExperimentalStdlibApi::class)
	fun <T : MessageBase> decodeAll(reader: ByteArrayReader, sizeByte: Int): List<T> {
		return buildList {
			while (reader.offset < sizeByte) {
				add(decode(reader))
			}
		}
	}

	fun <T : MessageBase> decodeAll(bytes: ByteArray): List<T> = decodeAll(ByteArrayReader(bytes), bytes.size)
}