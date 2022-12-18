package me.bvn13.sdk.android.gpx

import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeFormatterBuilder

class GpxConstant {
    companion object {
        const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        const val VERSION = "1.1"
        val DTF =
            DateTimeFormatterBuilder().append(ISO_LOCAL_DATE_TIME) // use the existing formatter for date time
                .appendOffset("+HH:MM", "+00:00") // set 'noOffsetText' to desired '+00:00'
                .toFormatter()
    }
}