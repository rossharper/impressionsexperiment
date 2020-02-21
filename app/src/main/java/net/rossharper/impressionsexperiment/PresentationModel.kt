package net.rossharper.impressionsexperiment

fun createPresentationModel() = PresentationModel((0..10).map { sectionNum ->
    Section(
        sectionNum.toString(),
        (0..100).map { itemNum -> Item(itemNum.toString(), itemNum) })
})

data class PresentationModel(val sections: List<Section>)

data class Section(val id: String, val items: List<Item>)

data class Item(val id: String, val num: Int)