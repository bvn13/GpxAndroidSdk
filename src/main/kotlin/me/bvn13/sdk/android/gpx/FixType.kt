package me.bvn13.sdk.android.gpx

/**
 * Type of GPS fix. none means GPS had no fix. To signify "the fix info is unknown", leave out fixType entirely. pps = military signal used
 */
enum class FixType(val value: String) {
    NONE("none"),
    _2D("2d"),
    _3D("3d"),
    DGPS("dgps"),
    PPS("pps")
}