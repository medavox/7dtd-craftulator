import org.w3c.dom.parsing.DOMParser
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.files.File
import org.w3c.files.FileReader

/**The string is the name, the int is the total quantity needed of the thing,
 * for the requested recipe and all its intermediates*/
private val uncraftables = CountingMap<String>()

/**Would be nice in future to use a data structure that indicates the order that things should be crafted in,
 * but this will do for now*/
private val toCraft = CountingMap<String>()

/**1. Adds this item to the toCraft collection
 * 2. adds any items in its recipe that aren't found in recipes.xml to the uncraftables collection
 * 3. calls this method on any items that *were* found in recipes.xml
 *
 * @param name the item's name, as it appears (if it does) in `recipes.xml`
 */
fun Document.visitItem(name:String, quantity:Int) {
    println("recipe[name~=$name]")
    val itemsToCraft:NodeList = this.querySelectorAll("recipe[name~=$name]")
    if(itemsToCraft.length == 0) {//no recipes matched
        //either the search was bollocks, or the item was uncraftable
        //add it to the uncraftables

        uncraftables[name] += quantity
    } else {
        toCraft[name] += quantity
        //for now as a placeholder, select the first item in the list
        val item: Node = itemsToCraft.item(0)!!

        println("item: $item")
        println("ingredients in its recipe:")

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
        for (i in 0 until item.childNodes.length) {
            val ingredient = item.childNodes[i]
            println(ingredient)

        }
    }
    println("uncraftables: $uncraftables")
    println("to craft: $toCraft")
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
            gotRecipesXml(loadedEvent.target.asDynamic().result)
        }
    })
}

fun gotRecipesXml(recipesXmlContents:String) {
    //println("foil contents maybe: $fileContents")
    val xmlDocument:Document = DOMParser().parseFromString(recipesXmlContents, "text/xml")
    val recipesEl:NodeList = xmlDocument.querySelectorAll("recipe[name^=vehicle]")
    val items = recipesEl.length
    println("reikpess:"+items)
    for( i in 0 until items) { recipesEl[i]?.let {
        if (it is Element) {
            println(it.asString() )
        }
    }}

    //xmlDoc.visitItem("vehicle", 1)
    xmlDocument.visitItem("vehicleGyrocopterPlaceable", 1)
}

private fun Element.asString():String {

    return "Node \"$nodeName\"; ${this.attributes.asList().fold(""){ acc, elem ->
        "$acc / ${elem.asString()}"
        
    }}"
}

private fun Attr.asString() :String {
    return "$name=\"$value\""
}