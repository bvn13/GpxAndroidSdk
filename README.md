# Android (Kotlin) SDK for manipulating GPX files

## About

This is another one SDK for reading and writing (manipulating) GPX files.

## Changelog

### 2022-09-25

1) project started
2) implemented GPX format in Kotlin
3) implemented representing GPX object into String for future saving

## Examples

### Full example

```kotlin
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
                                    )
                                )
                            )
                        )
                    )
                )
            )

```

this example will be presented in GPX as following

```xml
<?xml version="1.0" encoding="UTF-8"?>
    <gpx
    xmlns="http://www.topografix.com/GPX/1/1"
    version="1.1"
    creator="me.bvn13.sdk.android.gpx"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd">
    <time>2022-09-24T15:04:00+03:00</time>
    <metadata>
      <name>test name</name>
      <desc>test description</desc>
      <author>
      <name>bvn13</name>
      </author>
      </metadata>
    <wpt lat="14.64736838389662" lon="7.93212890625">
    <ele>10.0</ele>
    <time>2022-09-24T15:04:00+03:00</time>
    <magvar>3.0</magvar>
    <geoidheight>45.0</geoidheight>
    <name>test point 1</name>
    <cmt>comment 1</cmt>
    <desc>description of point 1</desc>
    <src>source 1</src>
    <link href="http://link-to.site.href">
    <text>text</text>
    <type>hyperlink</type>
    </link>
<link href="http://link2-to.site.href">
    <text>text2</text>
    <type>hyperlink2</type>
    </link>
    <sym>sym 1</sym>
    <type>type 1</type>
    <fix>dgps</fix>
    <sat>1</sat>
    <hdop>55.0</hdop>
    <vdop>66.0</vdop>
    <pdop>77.0</pdop>
    <ageofgpsdata>44</ageofgpsdata>
    <dgpsid>88</dgpsid>
    <extensions>
<extension1 first="second" third="fours"></extension1>
<extension2 aa="bb" cc="dd"></extension2>
</extensions>
    </wpt>
    <rte>
    <name>rte name</name>
    <cmt>cmt</cmt>
    <desc>desc</desc>
    <src>src</src>
    <link href="https://new.link.rte">
    <text>new text rte</text>
    <type>hyperlink</type>
    </link>
    <number>1234</number>
    <type>route</type>
    <extensions>
<ext-1>value1</ext-1>
</extensions>
    <wpt lat="14.64736838389662" lon="7.93212890625">
    <ele>10.0</ele>
    <time>2022-09-24T15:04:00+03:00</time>
    <magvar>3.0</magvar>
    <geoidheight>45.0</geoidheight>
    <name>test point 1</name>
    <cmt>comment 1</cmt>
    <desc>description of point 1</desc>
    <src>source 1</src>
    <link href="http://link-to.site.href">
    <text>text</text>
    <type>hyperlink</type>
    </link>
<link href="http://link2-to.site.href">
    <text>text2</text>
    <type>hyperlink2</type>
    </link>
    <sym>sym 1</sym>
    <type>type 1</type>
    <fix>dgps</fix>
    <sat>1</sat>
    <hdop>55.0</hdop>
    <vdop>66.0</vdop>
    <pdop>77.0</pdop>
    <ageofgpsdata>44</ageofgpsdata>
    <dgpsid>88</dgpsid>
    <extensions>
<extension1 first="second" third="fours"></extension1>
<extension2 aa="bb" cc="dd"></extension2>
</extensions>
    </wpt>
    </rte>
    <trk>
    <name>track 1</name>
    <cmt>comment track 1</cmt>
    <desc>desc track 1</desc>
    <src>src track 1</src>
    null
    <number>1234</number>
    <type>type 1</type>
    null
    <trkseg>
    <trkpt lat="14.64736838389662" lon="7.93212890625">
    <ele>10.0</ele>
    <time>2022-09-24T15:04:00+03:00</time>
    <magvar>3.0</magvar>
    <geoidheight>45.0</geoidheight>
    <name>test point 1</name>
    <cmt>comment 1</cmt>
    <desc>description of point 1</desc>
    <src>source 1</src>
    <link href="http://link-to.site.href">
    <text>text</text>
    <type>hyperlink</type>
    </link>
<link href="http://link2-to.site.href">
    <text>text2</text>
    <type>hyperlink2</type>
    </link>
    <sym>sym 1</sym>
    <type>type 1</type>
    <fix>dgps</fix>
    <sat>1</sat>
    <hdop>55.0</hdop>
    <vdop>66.0</vdop>
    <pdop>77.0</pdop>
    <ageofgpsdata>44</ageofgpsdata>
    <dgpsid>88</dgpsid>
    <extensions>
<extension1 first="second" third="fours"></extension1>
<extension2 aa="bb" cc="dd"></extension2>
</extensions>
    </trkpt>
    </trkseg>
    </trk>
    </gpx>
```