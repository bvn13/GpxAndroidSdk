package me.bvn13.sdk.android.gpx

/**
 * GPX documents contain a metadata header, followed by waypoints, routes, and tracks. You can add your own elements to the extensions section of the GPX document.
 *
 * See [official documentation](https://www.topografix.com/GPX/1/1/#element_gpx)
 *
 * [metadata] Metadata about the file.
 *
 * [wpt] A list of waypoints.
 *
 * [rte] A list of routes.
 *
 * [trk] A list of tracks.
 *
 * [extensions] You can add extend GPX by adding your own elements from another schema here.
 */
class GpxType(
    val metadata: MetadataType,
    val creator: String = "me.bvn13.sdk.android.gpx",
    val wpt: List<WptType>? = null,
    val rte: List<RteType>? = null,
    val trk: List<TrkType>? = null,
    val extensions: ExtensionType? = null
) {
    val version: String = "1.1"
}