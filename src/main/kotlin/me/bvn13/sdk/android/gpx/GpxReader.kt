package me.bvn13.sdk.android.gpx

import java.io.InputStream

fun GpxType.Companion.read(dis: InputStream) = GpxReader().read(dis)

fun ByteArray.asString() = String(this)

class GpxReader {
    fun read(dis: InputStream): GpxType {
        val container = readNotSpace(dis, Container.empty())
        return when (container.byte!!.toInt()) {
            '<'.code -> readBeginning(dis, container)
            else -> throw IllegalArgumentException("Not a GPX/XML?")
        }
    }

    private fun readBeginning(dis: InputStream, buffer: Container): GpxType {
        val container = readByte(dis, buffer)
        return when (container.byte!!.toInt()) {
            '?'.code -> readSignature(dis, container)
            '<'.code -> readGpx(dis, container)
            else -> throw IllegalArgumentException("Wrong symbol at ${container.position} from the very beginning")
        }
    }

    private fun readSignature(dis: InputStream, buffer: Container): GpxType {
        val container = readUntil(dis, buffer, '\n')
        if ("${GpxConstant.HEADER}\n" != container.buffer.asString()) {
            throw IllegalArgumentException("Wrong xml signature!")
        }
        return readBeginning(dis, Container.empty(container.position))
    }

    private fun readGpx(dis: InputStream, buffer: Container): GpxType {
        val container = readUntil(dis, buffer, setOf(' ', '\n'))
        val tagName = container.buffer.asString().substring(1, container.buffer.size - 1).lowercase()
        if ("gpx" != tagName) {
            throw IllegalArgumentException("There must be GPX tag in given InputStream")
        }
        val xmlObject = XmlObject(tagName)
        val attributesContainer = readAttributes(dis, container)
        xmlObject.attributes = attributesContainer.attributes
        val nestedContainer = readNestedObjects(dis, attributesContainer)
        val finishingContainer = readFinishingTag(dis, nestedContainer, tagName)
        return GpxType(

            MetadataType("test")
        )
    }

    private fun readObject(dis: InputStream, buffer: Container, checker: (Container) -> Unit): Container {
        val container = readUntil(dis, buffer, setOf(' ', '\n'))
        val tagName = container.buffer.asString().substring(1, container.buffer.size - 1).lowercase()
        checker.invoke(container)
        if ("gpx" != tagName) {
            throw IllegalArgumentException("There must be GPX tag in given InputStream")
        }
        val xmlObject = XmlObject(tagName)
        val attributesContainer = readAttributes(dis, container)
        xmlObject.attributes = attributesContainer.attributes
        val nestedContainer = readNestedObjects(dis, attributesContainer)
        val finishingContainer = readFinishingTag(dis, nestedContainer, tagName)
    }

    private fun readNestedObjects(dis: InputStream, buffer: Container): Container {

    }

    private fun readFinishingTag(dis: InputStream, buffer: Container, tagName: String): Container =
        readExactly(dis, Container.empty(buffer.position), "</${tagName}>")

    private fun readAttributes(dis: InputStream, buffer: Container): Container {
        if (buffer.byte!!.toInt() == '>'.code) {
            return Container.empty(buffer.position)
        }
        val attributes = HashMap<String, String>()
        var attributeContainer: Container = Container.empty(buffer.position)
        do {
            attributeContainer = readAttribute(dis, attributeContainer)
            attributes.putAll(attributeContainer.attributes)
        } while (attributeContainer.attributes.isNotEmpty() && attributeContainer.byte!!.toInt() != '>'.code)
        val result = Container.emptyWithAttributes(attributeContainer.position, attributes)
        return result
    }

    private fun readAttribute(dis: InputStream, buffer: Container): Container {
        val nameContainer = readUntil(dis, buffer, setOf(' ', '\n', '=', '\t'))
        val nameAsString = nameContainer.buffer.asString()
        val name = nameAsString.substring(0, nameAsString.length - 1)
        val equalsContainer = readUntil(dis, Container.empty(nameContainer.position), '"')
        val valueContainer = readUntil(dis, Container.empty(equalsContainer.position), '"')
        val valueAsString = valueContainer.buffer.asString()
        val value = valueAsString.substring(0, valueAsString.length - 1)
        val result = Container.empty(valueContainer.position)
        val closingContainer = readSkipping(dis, result, setOf(' ', '\n', '\t'))
        val nextBlockContainer = Container.of(closingContainer.byte!!, closingContainer.position)
        nextBlockContainer.attributes = mapOf(name to value)
        return nextBlockContainer
    }

    private fun readUntil(dis: InputStream, buffer: Container, ch: Char): Container {
        var container = buffer
        do {
            container = readByte(dis, container)
        } while (container.byte!!.toInt() != ch.code)
        return container
    }

    private fun readUntil(dis: InputStream, buffer: Container, chs: Set<Char>): Container {
        val chars = chs.map { c -> c.code }
        var container = buffer
        do {
            container = readByte(dis, container)
        } while (!chars.contains(container.byte!!.toInt()))
        return container
    }

    private fun readSkipping(dis: InputStream, buffer: Container, chs: Set<Char>): Container {
        val chars = chs.map { c -> c.code }
        var container = buffer
        do {
            container = readByte(dis, container)
        } while (chars.contains(container.byte!!.toInt()))
        return container
    }

    private fun readExactly(dis: InputStream, buffer: Container, charSequence: CharSequence): Container {
        var container = buffer
        charSequence.forEach {
            container = readByte(dis, container)
            if (container.byte!!.toInt() != it.code) {
                throw IllegalArgumentException("Expected closing tag $charSequence at position ${buffer.position}")
            }
        }
        return container
    }

    private fun readNotSpace(dis: InputStream, buffer: Container): Container {
        val container = readByte(dis, buffer)
        if (container.byte!!.toInt() == ' '.code
            || container.byte.toInt() == '\n'.code
        ) {
            return readByte(dis, container)
        } else {
            return container
        }
    }

    private fun readByte(dis: InputStream, container: Container): Container {
        val ba = ByteArray(1);
        if (-1 == dis.read(ba, 0, 1)) {
            throw InterruptedException("EOF")
        } else if (ba.size != 1) {
            throw InterruptedException("Reading 1 byte returns ${ba.size} bytes")
        }
        return Container(container.position + 1, ba[0], container.buffer.plus(ba))
    }

    class Container(val position: Long = 0, val byte: Byte?, val buffer: ByteArray) {
        var objects: List<XmlObject>? = null
        var attributes: Map<String, String> = HashMap()

        companion object {
            fun empty(): Container = empty(0)
            fun empty(position: Long) = Container(position, null, ByteArray(0))
            fun emptyWithAttributes(position: Long, attributes: Map<String, String>): Container {
                val container = Container.empty(position)
                container.attributes = attributes
                return container
            }
            fun of(b: Byte, position: Long) = Container(position, b, ByteArray(1) {_ -> b})
        }

        override fun toString(): String = this.buffer.asString()
    }

    class XmlObject(val type: String) {
        var attributes: Map<String, String> = HashMap()
        var nested: List<XmlObject>? = null
    }

}