import org.w3c.dom.parsing.DOMParser
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.files.File
import org.w3c.files.FileReader

/**Keeps a count of the total amounts of each uncraftable thing we need,
 * for the requested recipe and all its intermediates*/
private val uncraftables = CountingMap<String>()

/**Keeps count of the items that need to be crafted.
 * Their insertion order is later used (in reverse)
 * to list the items in the order they can be crafted*/
private val toCraft = CountingMapWithOrder<String>()

/**Recursively finds ALL the materials needed to make this item, including sub-assemblies.
 * 1. Adds this item to the toCraft collection
 * 2. adds any items in its recipe that aren't found in recipes.xml to the uncraftables collection
 * 3. calls this method on any items that *were* found in recipes.xml
 *
 * @param name the item's name, as it appears (if it does) in `recipes.xml`
 */
fun Document.visitItem(name:String, quantity:Int) {
    val recipeCandidates:NodeList = this.querySelectorAll("recipe[name~=$name]")
    if(recipeCandidates.length == 0) {//no recipes matched
        //either the search is bollocks, or the item IS uncraftable
        //add it to the uncraftables

        uncraftables[name] += quantity
        return
    }

    val filtered = recipeCandidates.asList().filter {
        it is Element && it.tagName == "recipe" }.map { it as Element }
    //println("possible candidates for crafting this item:"+filtered.size)
    //for now as a placeholder, select the first valid recipe in the list
    val item: Node = filtered[0]

    println("ingredients needed to craft $quantity ${(item as Element).attributes["name"]?.value}:")


    //now for the meaty bit:
    //for each ingredient required by this recipe,
    //find that ingredient's own recipe, and its recipe's ingredients, and so on.
    //recursively find all recipes and materials needed to make the chosen recipe.
    //if any ingredient can't be found in the supplied recipes.xml,
    //then we assume it's uncraftable/obtain-only.
    //along with the obvious (like engines and electrical parts),
    //this also includes materials you mine (such as ores).
    //so we'll end up with a total list of materials to acquire,
    //and a list of things to craft from them
    //(preferably in order).
    //also consider `item.children`
    val ingredients:List<Element> = item.childNodes.asList().
        filter { it is Element && it.tagName == "ingredient" }.map { it as Element }
    if(ingredients.isEmpty()) {//recipes with no ingredients are also uncraftable,
        //rather than being craftable without any ingredients
        uncraftables[name] += quantity
        return
    }
    toCraft[name] += quantity
    for (ingredient in ingredients) {
        println("\t${ingredient.attributes["count"]?.value?.toInt()?.times(quantity)} " +
                "${ingredient.attributes["name"]?.value}")
        //get ingredient name and count needed
    }
    for (ingredient in ingredients) {
        this.visitItem(ingredient.attributes["name"]!!.value,
                ingredient.attributes["count"]!!.value.toInt() * quantity)
    }

}

fun main() {
    //todo: find the best-matching recipe out of those returned
    // probably using levenshtein distance or similar
    val fslekt = document.getElementById("slekt") as HTMLInputElement
    println("slekt: $fslekt")
    //println("ok then fine. Wildcard search: ${document.querySelectorAll("*").asList()}")
    fslekt.addEventListener("change", { event ->
        val foyl:File = event.target.asDynamic().files[0] as File

        println("foil: ${foyl.name}; size: ${foyl.size}")
        val fr = FileReader()
        fr.readAsText(foyl)
        fr.onload = { loadedEvent ->
            gotRecipesXml(loadedEvent.target.asDynamic().result as String)
        }
    })
}

fun gotRecipesXml(recipesXmlContents:String) {
    //println("foil contents maybe: $fileContents")
    val xmlDocument:Document = DOMParser().parseFromString(recipesXmlContents, "text/xml")
/*    val recipesEl:NodeList = xmlDocument.querySelectorAll("recipe[name^=vehicle]")
    val items = recipesEl.length
    println("reikpess:"+items)
    for( i in 0 until items) { recipesEl[i]?.let {
        if (it is Element) {
            println(it.asString() )
        }
    }}*/

    //xmlDoc.visitItem("vehicle", 1)
    xmlDocument.visitItem("vehicleGyrocopterPlaceable", 1)
    println("uncraftables: $uncraftables")
    println("to craft: $toCraft")
}

/*private fun Node.asString():String {
    return when(this){
        is Element -> {
            "Element \"$nodeName\"; "+this.attributes.asList().
            fold("") { acc, elem ->
                "$acc / ${elem.asString()}"
            }
        }
        is Text -> "Text node \""+this.wholeText+"\""
        else -> "Node \"$nodeName\"=\"$nodeValue\""
    }
}*/

private fun Attr.asString() :String {
    return "$name=\"$value\""
}