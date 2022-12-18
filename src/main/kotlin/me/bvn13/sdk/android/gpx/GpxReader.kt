package me.bvn13.sdk.android.gpx

import me.bvn13.sdk.android.gpx.GpxConstant.Companion.DTF
import java.io.InputStream
import java.time.OffsetDateTime

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
        var tagName: String? = null
        val container = readObject(dis, buffer) {
            tagName = it.buffer.asString().substring(1, it.buffer.size - 1).lowercase()
            if ("gpx" != tagName) {
                throw IllegalArgumentException("There must be GPX tag in given InputStream")
            }
        }
        tagName?.let {
            return assembleGpxType(container)
        } ?: run {
            throw IllegalArgumentException("Unable to read content")
        }
    }

    private fun assembleGpxType(container: Container): GpxType {
        if (container.objects == null) {
            throw IllegalArgumentException("It was not parsed properly")
        }
        if (container.objects!!.size != 1) {
            throw IllegalArgumentException("It was parsed ${container.objects!!.size} objects, but only 1 is expected")
        }
        return findObject(container.objects, "gpx").let {
            return GpxType(
                metadata = assembleMetadataType(it.nested),
                creator = findAttributeOrNull(it.attributes, "creator") ?: throw IllegalArgumentException("Gpx.Creator not found"),
                wpt = findObjectsOrNull(it.nested ,"wpt")?.map { assembleWptType(it) },
                rte = findObjectsOrNull(it.nested, "rte")?.map { assembleRteType(it) },
                trk = findObjectsOrNull(it.nested, "trk")?.map { assembleTrkType(it) }
            )
        }
    }

    private fun assembleMetadataType(objects: List<XmlObject>?): MetadataType =
        findObject(objects, "metadata")
            .let {
                return MetadataType(
                    name = findObjectOrNull(it.nested, "name")?.value ?: throw IllegalArgumentException("Gpx.Metadata.Name not found"),
                    description = findObjectOrNull(it.nested, "desc")?.value ?: throw IllegalArgumentException("Gpx.Metadata.Description not found"),
                    authorName = findObject(it.nested, "author").let { author ->
                        findObject(author.nested, "name").value ?: throw IllegalArgumentException("Gpx.Metadata.Author.Name not found")
                    }
                )
            }

    private fun assembleWptType(obj: XmlObject): WptType =
        WptType(
            lat = findAttribute(obj.attributes, "lat").toDouble(),
            lon = findAttribute(obj.attributes, "lon").toDouble(),
            ele = findObjectOrNull(obj.nested, "ele")?.value?.toDouble(),
            time = findObjectOrNull(obj.nested, "time")?.value?.let { OffsetDateTime.parse(it, DTF) },
            magvar = findObjectOrNull(obj.nested, "magvar")?.value?.toDouble(),
            geoidheight = findObjectOrNull(obj.nested, "geoidheight")?.value?.toDouble(),
            name = findObjectOrNull(obj.nested, "name")?.value,
            cmt = findObjectOrNull(obj.nested, "cmt")?.value,
            desc = findObjectOrNull(obj.nested, "desc")?.value,
            src = findObjectOrNull(obj.nested, "src")?.value,
            link = findObjectsOrNull(obj.nested, "link")?.map { assembleLinkType(it) },
            sym = findObjectOrNull(obj.nested, "sym")?.value,
            type = findObjectOrNull(obj.nested, "type")?.value,
            fix = findObjectOrNull(obj.nested, "fix")?.value?.let { assembleFixType(it) },
            sat = findObjectOrNull(obj.nested, "sat")?.value?.toInt(),
            hdop = findObjectOrNull(obj.nested, "hdop")?.value?.toDouble(),
            vdop = findObjectOrNull(obj.nested, "vdop")?.value?.toDouble(),
            pdop = findObjectOrNull(obj.nested, "pdop")?.value?.toDouble(),
            ageofgpsdata = findObjectOrNull(obj.nested, "ageofgpsdata")?.value?.toInt(),
            dgpsid = findObjectOrNull(obj.nested, "dgpsid")?.value?.toInt(),
            extensions = findObjectOrNull(obj.nested, "extensions")?.nested?.map { assembleExtensionType(it) }
        )

    private fun assembleRteType(obj: XmlObject): RteType =
        RteType(
            name = findObjectOrNull(obj.nested, "name")?.value,
            cmt = findObjectOrNull(obj.nested, "cmt")?.value,
            desc = findObjectOrNull(obj.nested, "desc")?.value,
            src = findObjectOrNull(obj.nested, "src")?.value,
            link = findObjectsOrNull(obj.nested, "link")?.map { assembleLinkType(it) },
            number = findObjectOrNull(obj.nested, "number")?.value?.toInt(),
            type = findObjectOrNull(obj.nested, "type")?.value,
            extensions = findObjectOrNull(obj.nested, "extensions")?.nested?.map { assembleExtensionType(it) },
            rtept = findObjectsOrNull(obj.nested, "rtept")?.map { assembleWptType(it) }
        )

    private fun assembleTrkType(obj: XmlObject): TrkType =
        TrkType(
            name = findObjectOrNull(obj.nested, "name")?.value,
            cmt = findObjectOrNull(obj.nested, "cmt")?.value,
            desc = findObjectOrNull(obj.nested, "desc")?.value,
            src = findObjectOrNull(obj.nested, "src")?.value,
            link = findObjectsOrNull(obj.nested, "link")?.let { list -> if (list.isNotEmpty()) list.map { assembleLinkType(it) } else null },
            number = findObjectOrNull(obj.nested, "number")?.value?.toInt(),
            type = findObjectOrNull(obj.nested, "type")?.value,
            extensions = findObjectOrNull(obj.nested, "extensions")?.let { assembleExtensionType(it) },
            trkseg = findObjectsOrNull(obj.nested, "trkseg")?.map { assembleTrksegType(it) }
        )

    private fun assembleLinkType(obj: XmlObject): LinkType =
        LinkType(
            href = findAttribute(obj.attributes, "href"),
            text = findObjectOrNull(obj.nested, "text")?.value,
            type = findObjectOrNull(obj.nested, "type")?.value
        )

    private fun assembleFixType(value: String): FixType =
        FixType.valueOf(value.uppercase())

    private fun assembleExtensionType(obj: XmlObject): ExtensionType =
        ExtensionType(
            nodeName = obj.type,
            value = if (obj.value == "") null else obj.value,
            parameters = if (obj.attributes.isEmpty()) null else obj.attributes.toSortedMap()
        )

    private fun assembleTrksegType(obj: XmlObject): TrksegType =
        TrksegType(
            trkpt = findObjectsOrNull(obj.nested, "trkpt")?.map { assembleWptType(it) },
            extensions = findObjectOrNull(obj.nested, "extensions")?.let { assembleExtensionType(it) }
        )

    private fun findObjectOrNull(objects: List<XmlObject>?, name: String): XmlObject? =
        objects?.firstOrNull { o ->
            o.type.lowercase() == name.lowercase()
        }

    private fun findObject(objects: List<XmlObject>?, name: String): XmlObject =
        findObjectOrNull(objects, name)
            ?: throw IllegalArgumentException("$name not found")

    private fun findObjectsOrNull(objects: List<XmlObject>?, name: String): List<XmlObject>? =
        objects?.filter { o ->
            o.type.lowercase() == name.lowercase()
        }

    private fun findAttributeOrNull(attributes: Map<String, String>?, name: String): String? =
        attributes?.firstNotNullOf { e -> if (e.key.lowercase() == name.lowercase()) e.value else null }

    private fun findAttribute(attributes: Map<String, String>?, name: String): String =
        findAttributeOrNull(attributes, name)
            ?: throw IllegalArgumentException("Unable to find attribute $name")

    private fun readObject(dis: InputStream, buffer: Container, checker: ((Container) -> Unit)? = null): Container {
        if (buffer.buffer[0].toInt() != '<'.code) {
            throw IllegalArgumentException("Not a tag at position ${buffer.position}")
        }
        var container = readUntil(dis, buffer, setOf(' ', '\n', '>'))
        val tagName = container.buffer.asString().substring(1, container.buffer.size - 1).lowercase()
        checker?.invoke(container)
        val xmlObject = XmlObject(tagName)
        if (container.byte!!.toInt() != '>'.code) {
            container = readAttributes(dis, container)
            xmlObject.attributes = container.attributes
        }
        container = readSkipping(dis, container, SKIPPING_SET)
        if (container.byte!!.toInt() == '<'.code) {
            container = readNestedObjects(dis, container)
            xmlObject.nested = container.objects
            container = readFinishingTag(dis, container, tagName)
        }
        if (container.buffer.asString() != "</$tagName>") {
            container = readValue(dis, container, tagName)
        }
        xmlObject.value = container.buffer.asString().replace("</$tagName>", "")
        container.objects = listOf(xmlObject)
        return container
    }

    private fun readNestedObjects(dis: InputStream, buffer: Container): Container {
        val list = ArrayList<XmlObject>()
        var container: Container = readByte(dis, buffer)
        if (container.byte!!.toInt() == '/'.code) {
            return container
        }
        while (true) {
            val objectContainer = readObject(dis, container)
            val skippingContainer = readSkipping(dis, objectContainer, SKIPPING_SET)
            container = readByte(dis, skippingContainer)
            objectContainer.objects?.forEach {
                list.add(it)
            }
            if (container.byte!!.toInt() == '/'.code) {
                break;
            }
        }
        container.objects = list
        return container
    }

    private fun readFinishingTag(dis: InputStream, buffer: Container, tagName: String): Container =
        readExactly(dis, buffer, "</${tagName}>")

    private fun readValue(dis: InputStream, buffer: Container, tagName: String): Container =
        readUntil(dis, buffer, "</$tagName>")

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
        val closingContainer = readSkipping(dis, result, SKIPPING_SET)
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

    private fun readUntil(dis: InputStream, buffer: Container, charSequence: CharSequence): Container {
        var container = Container.of(buffer.byte!!, buffer.position)
        var pos = 0
        do {
            container = readByte(dis, container)
            if (container.byte!!.toInt() == charSequence[pos].code) {
                pos++
            } else {
                pos = 0
            }
        } while (pos < charSequence.length)
        return container
    }

    private fun readSkipping(dis: InputStream, buffer: Container, chs: Set<Char>): Container {
        val chars = chs.map { c -> c.code }
        var container = buffer
        do {
            container = readByte(dis, container)
        } while (chars.contains(container.byte!!.toInt()))
        return Container.of(container.byte!!, container.position)
    }

    private fun readExactly(dis: InputStream, buffer: Container, charSequence: CharSequence): Container {
        var container = buffer
        do {
            container = readByte(dis, container)
            if (charSequence.substring(0, container.buffer.size) != container.buffer.asString()) {
                throw IllegalArgumentException("Expected closing tag $charSequence at position ${buffer.position}")
            }
        } while (container.buffer.size < charSequence.length)
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
            throw InterruptedException("Reading of 1 byte returns ${ba.size} bytes")
        }
        return Container(container.position + 1, ba[0], container.buffer.plus(ba))
    }

    class Container(val position: Long = 0, val byte: Byte?, val buffer: ByteArray) {
        var objects: List<XmlObject>? = null
        var attributes: Map<String, String> = HashMap()
        var value: String? = null

        companion object {
            fun empty(): Container = empty(0)
            fun empty(position: Long) = Container(position, null, ByteArray(0))
            fun emptyWithAttributes(position: Long, attributes: Map<String, String>): Container {
                val container = Container.empty(position)
                container.attributes = attributes
                return container
            }

            fun of(b: Byte, position: Long) = Container(position, b, ByteArray(1) { _ -> b })
        }

        override fun toString(): String = this.buffer.asString()
    }

    class XmlObject(type: String) {
        val type: String
        var attributes: Map<String, String> = HashMap()
        var nested: List<XmlObject>? = null
        var value: String? = null

        init {
            this.type = type.lowercase()
        }
    }

    companion object {
        val SKIPPING_SET = setOf(' ', '\n', '\r', '\t')
    }
}