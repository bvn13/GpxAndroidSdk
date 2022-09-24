package me.bvn13.sdk.android.gpx

import java.time.OffsetDateTime
import java.time.ZonedDateTime

/**
 * wpt represents a waypoint, point of interest, or named feature on a map.
 *
 * [lat] The latitude of the point. Decimal degrees
 *
 * [lon] The longitude of the point. Decimal degrees
 *
 * [ele] Elevation (in meters) of the point.
 *
 * [time] Creation/modification timestamp for element. Date and time in are in Univeral Coordinated Time (UTC), not local time!
 *        Conforms to ISO 8601 specification for date/time representation. Fractional seconds are allowed for millisecond timing in tracklogs.
 *
 * [magvar] Magnetic variation (in degrees) at the point
 *
 * [geoidheight] Height (in meters) of geoid (mean sea level) above WGS84 earth ellipsoid. As defined in NMEA GGA message.
 *
 * [name] The GPS name of the waypoint. This field will be transferred to and from the GPS. GPX does not place restrictions
 *        on the length of this field or the characters contained in it. It is up to the receiving application to validate the field before sending it to the GPS.
 *
 * [cmt] GPS waypoint comment. Sent to GPS as comment.
 *
 * [desc] A text description of the element. Holds additional information about the element intended for the user, not the GPS.
 *
 * [src] Source of data. Included to give user some idea of reliability and accuracy of data. "Garmin eTrex", "USGS quad Boston North", e.g.
 *
 * [link] Link to additional information about the waypoint.
 *
 * [sym] Text of GPS symbol name. For interchange with other programs, use the exact spelling of the symbol as displayed on the GPS.
 *       If the GPS abbreviates words, spell them out.
 *
 * [type] Type (classification) of the waypoint.
 *
 * [fix] Type of GPX fix.
 *
 * [sat] Number of satellites used to calculate the GPX fix.
 *
 * [hdop] Horizontal dilution of precision.
 *
 * [vdop] Vertical dilution of precision.
 *
 * [pdop] Position dilution of precision.
 *
 * [ageofgpsdata] Number of seconds since last DGPS update.
 *
 * [dgpsid] ID of DGPS station used in differential correction.
 *
 * [extensions] You can add extend GPX by adding your own elements from another schema here.
 */
class WptType(
    val lat: Double,
    val lon: Double,
    val ele: Double? = null,
    val time: OffsetDateTime? = null,
    val magvar: Double? = null,
    val geoidheight: Double? = null,
    val name: String? = null,
    val cmt: String? = null,
    val desc: String? = null,
    val src: String? = null,
    val link: List<LinkType>? = null,
    val sym: String? = null,
    val type: String? = null,
    val fix: FixType? = null,
    val sat: Int? = null,
    val hdop: Double? = null,
    val vdop: Double? = null,
    val pdop: Double? = null,
    val ageofgpsdata: Int? = null,
    val dgpsid: Int? = null,
    val extensions: List<ExtensionType>? = null
) {
    init {
        require(sat == null || sat >= 0) {
            "sat must be non-negative integer"
        }
        require(magvar == null || magvar in 0.0..360.0) {
            "magvar must be degree in (0)..(360) in Double"
        }
        require(dgpsid in 0..1023) {
            "dgpsid must be in 0..1023"
        }
    }
}
