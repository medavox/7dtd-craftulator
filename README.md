# 7dtd-craftulator
Web app to generate a [Bill of Materials](https://en.wikipedia.org/wiki/Bill_of_materials) for a craftable thing in 7 Days To Die.

Given you want to make a specific thing,

And what resources you already have,

* How much of each raw material/salvaged material will you need to gather?
* What intermediate products will you need to craft?
* [TODO] What station will you have to craft it at?
* [TODO] What perk unlocks this recipe?


This is a Kotlin/JS single-page web app.

This app needs access to a copy of the crafting recipes in the game, 
which it is not licensed to provide.

# To Test Your Changes

From inside the project directory, run:

    ./gradlew browserDevelopmentRun
    
This will compile the Kotlin (.kt) files to Javascript, then open the web app in your browser.

# To Build a New Production Release

From inside the project directory, run:

    ./gradlew browserProductionWebpack

This compiles the Kotlin to javascript.

The resulting built files are output into `docs/`.

# Contributions Welcome

I am a Java/Kotlin/Android developer; I don't do HTML good.

If you can make it fancier, please do, and then send me a Pull Request.

Thanks!

# TODO

* account for quantity reductions from perks
* allow users to specify quantity of the item they want to craft
* list crafting station used
* Allow user to subtract what they already have, from what needs crafting or gathering
    - tick off (or partially tick off) already-obtained items
    -  I need 4 wheels? well I've already got 2
* better UI: expandable tree, regedit-style
* Consider wood for the crafting stations that require it: campfires: furnaces
* select what to craft using a dynamic navmenu, sorted into categories: weapons, vehicles etc 
