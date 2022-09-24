package me.bvn13.sdk.android.gpx

/**
 * 	Information about the GPX file, author, and copyright restrictions goes in the metadata section.
 * 	Providing rich, meaningful information about your GPX files allows others to search for and use your GPS data.
 */
class MetadataType(val name: String, val description: String = "", val authorName: String = "") {
}