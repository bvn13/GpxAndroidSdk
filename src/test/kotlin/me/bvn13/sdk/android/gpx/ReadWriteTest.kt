package me.bvn13.sdk.android.gpx

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.time.*

class ReadWriteTest {

    @DisplayName("Read-Write test")
    @Test
    fun testReadWrite() {
        val clock = Clock.fixed(
            LocalDateTime.of(2022, 9, 24, 15, 4, 0, 0).toInstant(ZoneOffset.ofHours(3)), ZoneId.of("Europe/Moscow")
        )

        val gpxType = GpxType(
            MetadataType("test name", description = "test description", authorName = "bvn13"),
            wpt = listOf(
                WptType(
                    lat = 14.64736838389662,
                    lon = 7.93212890625,
                    ele = 10.toDouble(),
                    time = OffsetDateTime.now(clock),
                    magvar = 3.toDouble(),
                    geoidheight = 45.toDouble(),
                    name = "test point 1",
                    cmt = "comment 1",
                    desc = "description of point 1",
                    link = listOf(
                        LinkType(
                            href = "http://link-to.site.href",
                            text = "text",
                            type = "hyperlink"
                        ),
                        LinkType(
                            href = "http://link2-to.site.href",
                            text = "text2",
                            type = "hyperlink2"
                        )
                    ),
                    src = "source 1",
                    sym = "sym 1",
                    type = "type 1",
                    fix = FixType.DGPS,
                    sat = 1,
                    hdop = 55.toDouble(),
                    vdop = 66.toDouble(),
                    pdop = 77.toDouble(),
                    ageofgpsdata = 44,
                    dgpsid = 88,
                    extensions = listOf(
                        ExtensionType(
                            "extension1",
                            parameters = mapOf(Pair("first", "second"), Pair("third", "fours"))
                        ),
                        ExtensionType(
                            "extension2",
                            parameters = mapOf(Pair("aa", "bb"), Pair("cc", "dd"))
                        )
                    )
                )
            ),
            rte = listOf(
                RteType(
                    name = "rte name",
                    cmt = "cmt",
                    desc = "desc",
                    src = "src",
                    link = listOf(
                        LinkType(
                            href = "https://new.link.rte",
                            text = "new text rte",
                            type = "hyperlink"
                        )
                    ),
                    number = 1234,
                    type = "route",
                    extensions = listOf(
                        ExtensionType(
                            "ext-1",
                            value = "value1"
                        )
                    ),
                    rtept = listOf(
                        WptType(
                            lat = 14.64736838389662,
                            lon = 7.93212890625,
                            ele = 10.toDouble(),
                            time = OffsetDateTime.now(clock),
                            magvar = 3.toDouble(),
                            geoidheight = 45.toDouble(),
                            name = "test point 1",
                            cmt = "comment 1",
                            desc = "description of point 1",
                            link = listOf(
                                LinkType(
                                    href = "http://link-to.site.href",
                                    text = "text",
                                    type = "hyperlink"
                                ),
                                LinkType(
                                    href = "http://link2-to.site.href",
                                    text = "text2",
                                    type = "hyperlink2"
                                )
                            ),
                            src = "source 1",
                            sym = "sym 1",
                            type = "type 1",
                            fix = FixType.DGPS,
                            sat = 1,
                            hdop = 55.toDouble(),
                            vdop = 66.toDouble(),
                            pdop = 77.toDouble(),
                            ageofgpsdata = 44,
                            dgpsid = 88,
                            extensions = listOf(
                                ExtensionType(
                                    "extension1",
                                    parameters = mapOf(Pair("first", "second"), Pair("third", "fours"))
                                ),
                                ExtensionType(
                                    "extension2",
                                    parameters = mapOf(Pair("aa", "bb"), Pair("cc", "dd"))
                                )
                            )
                        )
                    )
                )
            ),
            trk = listOf(
                TrkType(
                    name = "track 1",
                    cmt = "comment track 1",
                    desc = "desc track 1",
                    src = "src track 1",
                    number = 1234,
                    type = "type 1",
                    trkseg = listOf(
                        TrksegType(
                            listOf(
                                WptType(
                                    lat = 14.64736838389662,
                                    lon = 7.93212890625,
                                    ele = 10.toDouble(),
                                    time = OffsetDateTime.now(clock),
                                    magvar = 3.toDouble(),
                                    geoidheight = 45.toDouble(),
                                    name = "test point 1",
                                    cmt = "comment 1",
                                    desc = "description of point 1",
                                    link = listOf(
                                        LinkType(
                                            href = "http://link-to.site.href",
                                            text = "text",
                                            type = "hyperlink"
                                        ),
                                        LinkType(
                                            href = "http://link2-to.site.href",
                                            text = "text2",
                                            type = "hyperlink2"
                                        )
                                    ),
                                    src = "source 1",
                                    sym = "sym 1",
                                    type = "type 1",
                                    fix = FixType.DGPS,
                                    sat = 1,
                                    hdop = 55.toDouble(),
                                    vdop = 66.toDouble(),
                                    pdop = 77.toDouble(),
                                    ageofgpsdata = 44,
                                    dgpsid = 88,
                                    extensions = listOf(
                                        ExtensionType(
                                            "extension1",
                                            parameters = mapOf(Pair("first", "second"), Pair("third", "fours"))
                                        ),
                                        ExtensionType(
                                            "extension2",
                                            parameters = mapOf(Pair("aa", "bb"), Pair("cc", "dd"))
                                        )
                                    )
                                ),
                                WptType(
                                    lat = 14.64736838389662,
                                    lon = 7.93212890625,
                                    ele = 10.toDouble(),
                                    time = OffsetDateTime.now(clock),
                                    magvar = 3.toDouble(),
                                    geoidheight = 45.toDouble(),
                                    name = "test point 1",
                                    cmt = "comment 1",
                                    desc = "description of point 1",
                                    link = listOf(
                                        LinkType(
                                            href = "http://link-to.site.href",
                                            text = "text",
                                            type = "hyperlink"
                                        ),
                                        LinkType(
                                            href = "http://link2-to.site.href",
                                            text = "text2",
                                            type = "hyperlink2"
                                        )
                                    ),
                                    src = "source 1",
                                    sym = "sym 1",
                                    type = "type 1",
                                    fix = FixType.DGPS,
                                    sat = 1,
                                    hdop = 55.toDouble(),
                                    vdop = 66.toDouble(),
                                    pdop = 77.toDouble(),
                                    ageofgpsdata = 44,
                                    dgpsid = 88,
                                    extensions = listOf(
                                        ExtensionType(
                                            "extension1",
                                            parameters = mapOf(Pair("first", "second"), Pair("third", "fours"))
                                        ),
                                        ExtensionType(
                                            "extension2",
                                            parameters = mapOf(Pair("aa", "bb"), Pair("cc", "dd"))
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        val gpx = gpxType.toXmlString(clock)
        val deserializedGpxType = GpxType.read(ByteArrayInputStream(gpx.toByteArray()))

        Assertions.assertEquals(gpxType, deserializedGpxType)
    }

}