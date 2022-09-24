package me.bvn13.sdk.android.gpx

import me.bvn13.sdk.android.gpx.GpxWriter.Companion.DTF
import me.bvn13.sdk.android.gpx.GpxWriter.Companion.HEADER
import me.bvn13.sdk.android.gpx.GpxWriter.Companion.SCHEMA_LOCATION
import me.bvn13.sdk.android.gpx.GpxWriter.Companion.XMLNS
import me.bvn13.sdk.android.gpx.GpxWriter.Companion.XMLNS_XSI
import java.time.Clock
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatterBuilder

fun GpxType.toXmlString(): String = this.toXmlString(null)

fun GpxType.toXmlString(clock: Clock?): String = """
    $HEADER
    <gpx
    xmlns="$XMLNS"
    version="${this.version}"
    creator="${this.creator}"
    xmlns:xsi="$XMLNS_XSI"
    xsi:schemaLocation="$SCHEMA_LOCATION">
    <time>${now(clock)}</time>
    ${this.metadata.toXmlString()}
    ${this.wpt?.toXmlString()}
    ${this.rte?.toXmlString()}
    ${this.trk?.toXmlString()}
    </gpx>
""".trim()

fun MetadataType.toXmlString(): String = """
      <metadata>
      <name>${this.name}</name>
      <desc>${this.description}</desc>
      <author>
      <name>${this.authorName}</name>
      </author>
      </metadata>
""".trim()

private fun now(clock: Clock?) = OffsetDateTime.now(clock ?: Clock.systemDefaultZone()).format(DTF)

fun WptType.toXmlString(nodeName: String? = null) =
    if (nodeName != null) {
        this.toXmlString(nodeName)
    } else {
        this.toXmlString("wpt")
    }

@JvmName("toXmlStringNamed")
fun WptType.toXmlString(nodeName: String) = """
    <${nodeName} lat="${this.lat}" lon="${this.lon}">
    ${toXmlString(ele, "ele")}
    ${toXmlString(time, "time")}
    ${toXmlString(magvar, "magvar")}
    ${toXmlString(geoidheight, "geoidheight")}
    ${toXmlString(name, "name")}
    ${toXmlString(cmt, "cmt")}
    ${toXmlString(desc, "desc")}
    ${toXmlString(src, "src")}
    ${link?.toXmlString()}
    ${toXmlString(sym, "sym")}
    ${toXmlString(type, "type")}
    ${fix?.toXmlString()}
    ${toXmlString(sat, "sat")}
    ${toXmlString(hdop, "hdop")}
    ${toXmlString(vdop, "vdop")}
    ${toXmlString(pdop, "pdop")}
    ${toXmlString(ageofgpsdata, "ageofgpsdata")}
    ${toXmlString(dgpsid, "dgpsid")}
    ${extensions?.toXmlString()}
    </${nodeName}>
""".trim()

fun RteType.toXmlString() = """
    <rte>
    ${toXmlString(this.name, "name")}
    ${toXmlString(this.cmt, "cmt")}
    ${toXmlString(this.desc, "desc")}
    ${toXmlString(this.src, "src")}
    ${this.link?.toXmlString()}
    ${toXmlString(this.number, "number")}
    ${toXmlString(this.type, "type")}
    ${this.extensions?.toXmlString()}
    ${this.rtept?.toXmlString()}
    </rte>
""".trim()

fun TrkType.toXmlString() = """
    <trk>
    ${toXmlString(this.name, "name")}
    ${toXmlString(this.cmt, "cmt")}
    ${toXmlString(this.desc, "desc")}
    ${toXmlString(this.src, "src")}
    ${this.link?.toXmlString()}
    ${toXmlString(this.number, "number")}
    ${toXmlString(this.type, "type")}
    ${this.extensions?.toXmlString()}
    ${this.trkseg?.toXmlString()}
    </trk>
""".trim()

fun List<WptType>.toXmlString(nodeName: String?) =
    this.joinToString(prefix = "", postfix = "", separator = "") {
        it.toXmlString(nodeName)
    }

@JvmName("toXmlStringWptType")
fun List<WptType>.toXmlString() =
    this.joinToString(prefix = "", postfix = "", separator = "") {
        it.toXmlString()
    }

@JvmName("toXmlStringLinkType")
fun List<LinkType>.toXmlString() =
    this.joinToString(prefix = "", postfix = "", separator = "\n", transform = LinkType::toXmlString)

@JvmName("toXmlStringRteType")
fun List<RteType>.toXmlString() =
    this.joinToString(prefix = "", postfix = "", separator = "", transform = RteType::toXmlString)

@JvmName("toXmlStringTrkType")
fun List<TrkType>.toXmlString() =
    this.joinToString(prefix = "", postfix = "", separator = "", transform = TrkType::toXmlString)

@JvmName("toXmlStringTrksegType")
fun List<TrksegType>.toXmlString() =
    this.joinToString(prefix = "", postfix = "", separator = "", transform = TrksegType::toXmlString)

fun List<ExtensionType>.toXmlString() =
    this.joinToString(
        prefix = "<extensions>\n",
        postfix = "\n</extensions>",
        separator = "\n",
        transform = ExtensionType::toXmlString
    )

fun LinkType.toXmlString() = """
    <link href="${this.href}">
    <text>${this.text}</text>
    <type>${this.type}</type>
    </link>
""".trim()

fun FixType.toXmlString() = """
    <fix>${this.value}</fix>
""".trim()

fun ExtensionType.toXmlString() = """
        <${this.nodeName}${toXmlString(this.parameters)}>${this.value ?: ""}</${this.nodeName}>
    """.trim()

fun TrksegType.toXmlString() = """
    <trkseg>
    ${this.trkpt?.toXmlString("trkpt")}
    </trkseg>
""".trim()

fun toXmlString(value: String?, nodeName: String) =
    if (value != null) {
        """
            <${nodeName}>${value}</${nodeName}>
        """.trim()
    } else {
        ""
    }

fun toXmlString(value: Int?, nodeName: String) =
    if (value != null) {
        """
            <${nodeName}>${value}</${nodeName}>
        """.trim()
    } else {
        ""
    }

fun toXmlString(value: Double?, nodeName: String) =
    if (value != null) {
        """
            <${nodeName}>${value}</${nodeName}>
        """.trim()
    } else {
        ""
    }

fun toXmlString(value: OffsetDateTime?, nodeName: String) =
    if (value != null) {
        """
            <${nodeName}>${value.format(DTF)}</${nodeName}>
        """.trim()
    } else {
        ""
    }

fun toXmlString(value: Map<String, String>?) =
    value?.entries?.joinToString(separator = "") {
        " ${it.key}=\"${it.value}\""
    } ?: ""

class GpxWriter {
    companion object {
        const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        const val XMLNS = "http://www.topografix.com/GPX/1/1"
        const val XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance"
        const val SCHEMA_LOCATION = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"

        internal val DTF = DateTimeFormatterBuilder()
            .append(ISO_LOCAL_DATE_TIME) // use the existing formatter for date time
            .appendOffset("+HH:MM", "+00:00") // set 'noOffsetText' to desired '+00:00'
            .toFormatter()
    }
}