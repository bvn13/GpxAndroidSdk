package me.bvn13.sdk.android.gpx

/**
 * A link to an external resource (Web page, digital photo, video clip, etc) with additional information.
 *
 * [href] URL of hyperlink.
 *
 * [text] Text of hyperlink.
 *
 * [type] Mime type of content (image/jpeg)
 */
class LinkType(val href: String, val text: String? = null, val type: String? = null) {
}