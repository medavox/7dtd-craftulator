import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.w3c.dom.parsing.DOMParser
import kotlin.browser.document

fun main() {
    /**The string is the name, the int is the total quantity needed for this recipe and all its intermediates*/
    val uncraftableLeafNodes = mapOf<String, Int>()
    //val intermediatesToCraft =
    document.write("Hello, world!")
    val document:Document = DOMParser().parseFromString("lol", "text/xml")
    val nameSearch = "gyro"
    val itemsToCraft = document.querySelectorAll("recipe[name~=$nameSearch]")
    if(itemsToCraft.length == 0) {//no recipes matched
        //either the search was bollocks, or the item was uncraftable
        //exit early with an error message
    }
    //find the best-matching recipe out of those returned
    //probably using levenshtein distance or similar

    //for now as a placeholder, select the first item in the list
    val item = itemsToCraft.item(0)

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
    for(ingredient in item?.childNodes.asDynamic().values) {

    }

}