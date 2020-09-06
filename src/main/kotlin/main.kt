import org.w3c.dom.parsing.DOMParser
import kotlinx.browser.document
import org.w3c.dom.*

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
fun visitItem(name:String, quantity:Int) {
    val itemsToCraft = document.querySelectorAll("recipe[name~=$name]")
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
}

fun main() {
    document.write("Hello, world!")
    //val foyl = js("require(\"../recipes.xml\")")
    val nameSearch = "gyro"

    //todo: find the best-matching recipe out of those returned
    // probably using levenshtein distance or similar
    val fslekt = document.getElementById("file-selector") as HTMLInputElement

    lateinit var foyl:String
    fslekt.addEventListener("change", {event ->
        foyl = event.target.asDynamic().files[0] as String
        println("foil: $foyl")
    })
    val xmlDoc: Document = DOMParser().parseFromString(foyl, "text/xml")
}