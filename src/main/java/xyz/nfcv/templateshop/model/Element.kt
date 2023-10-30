package xyz.nfcv.templateshop.model

data class NodeProp(val name: String?, val content: String?, val url: String?, val type: String?)
data class SlotProp(val size: Int, val children: List<Element>)
data class FormProp(val method: String?, val url: String?)
data class UniversalProp(val props: Map<String, Any>)

data class Element(
    val id: String?,
    val type: String?,
    val node_prop: NodeProp?,
    val children: List<SlotProp>?,
    val form_prop: FormProp?,
    val universal_prop: UniversalProp?,
) {
    data class Entry(val name: String, val desc: String?)
}