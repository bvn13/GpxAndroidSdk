package me.bvn13.sdk.android.gpx

/**
 * rte represents route - an ordered list of waypoints representing a series of turn points leading to a destination.
 *
 * [namee] GPS name of track.
 *
 * [cmt] GPS comment for track.
 *
 * [desc] User description of track.
 *
 * [src] Source of data. Included to give user some idea of reliability and accuracy of data.
 *
 * [link] Links to external information about track.
 *
 * [number] GPS track number.
 *
 * [type] Type (classification) of track.
 *
 * [extensions] You can add extend GPX by adding your own elements from another schema here.
 *
 * [rtept] A list of route points.
 */
class RteType(
    val name: String? = null,
    val cmt: String? = null,
    val desc: String? = null,
    val src: String? = null,
    val link: List<LinkType>? = null,
    val number: Int? = null,
    val type: String? = null,
    val extensions: List<ExtensionType>? = null,
    val rtept: List<WptType>? = null
) {
    init {
        require(number == null || number >= 0) {
            "number must be non negative Integer"
        }
    }
}