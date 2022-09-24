package me.bvn13.sdk.android.gpx

/**
 * A Track Segment holds a list of Track Points which are logically connected in order. To represent a single GPS track where GPS reception was lost, or the GPS receiver was turned off, start a new Track Segment for each continuous span of track data.
 *
 * [trkpt] A Track Point holds the coordinates, elevation, timestamp, and metadata for a single point in a track.
 *
 * [extensions] You can add extend GPX by adding your own elements from another schema here.
 */
class TrksegType(
    val trkpt: List<WptType>? = null,
    val extensions: ExtensionType? = null
) {
}