package me.bvn13.sdk.android.gpx

/**
 * 	trk represents a track - an ordered list of points describing a path.
 *
 * [namee] GPS name of route.
 *
 * [cmt] GPS comment for route.
 *
 * [desc] Text description of route for user. Not sent to GPS.
 *
 * [src] Source of data. Included to give user some idea of reliability and accuracy of data.
 *
 * [link] Links to external information about the route.
 *
 * [number] GPS route number.
 *
 * [type] Type (classification) of route.
 *
 * [extensions] You can add extend GPX by adding your own elements from another schema here.
 *
 * [trkseg] A Track Segment holds a list of Track Points which are logically connected in order.
 *          To represent a single GPS track where GPS reception was lost, or the GPS receiver was turned off,
 *          start a new Track Segment for each continuous span of track data
 */
class TrkType(
    val name: String? = null,
    val cmt: String? = null,
    val desc: String? = null,
    val src: String? = null,
    val link: List<LinkType>? = null,
    val number: Int? = null,
    val type: String? = null,
    val extensions: ExtensionType? = null,
    val trkseg: List<TrksegType>? = null
) {
    init {
        require(number == null || number >= 0) {
            "number must be non negative Integer"
        }
    }
}