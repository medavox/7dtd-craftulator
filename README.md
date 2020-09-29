# 7dtd-craftulator
Web app to generate a Bill of Materials for a craftable thing in 7 Days To Die.

Given you want to make a specific thing,

And given what resources you already have,

* How much of each raw material/salvaged material will you need to gather?
* [TODO] What station will you have to craft it at?
* What intermediate products will you need to craft and at what station?
* [TODO] What perk unlocks this recipe?


This is a Kotlin/JS single-page web app.

This app needs access to a copy of the crafting recipes in the game, 
which it is not licensed to provide.

# Contributions Welcome

I am a Java/Kotlin/Android developer; I don't do HTML good.

The UI is just made of normal HTML+CSS; if you can make it prettier, please (**please**) submit a pull request with improved styling. 

# TODO

* account for quantity reductions from perks
* allow users to specify quantity of the item they want to craft
* list crafting station used
* Allow user to subtract what they already have, from what needs crafting or gathering
    - tick off (or partially tick off) already-obtained items
    -  I need 4 wheels? well I've already got 2
* better UI: expandable tree, regedit-style
* Consider wood for the crafting stations that require it: campfires: furnaces
