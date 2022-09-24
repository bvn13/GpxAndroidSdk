package me.bvn13.sdk.android.gpx

/**
 * XML Node describer
 *
 * will be composed into XML in following format:
 *
 * ```xml
 * <nodeName parameter1-key="parameter1-value" parameter2-key="parameter2-value">
 *     value
 * </nodeName>
 * ```
 *
 * [nodeName] XmlTag
 *
 * [value] Xml inner text
 *
 * [parameters] Map of key-value pairs
 */
class ExtensionType(val nodeName: String, val value: String? = null, val parameters: Map<String, String>? = null) {
    init {
        require(value != null || parameters != null) {
            "value or parameters must be specified"
        }
    }
}