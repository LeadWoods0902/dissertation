package com.leadwoods.dissertation.local_database.character

enum class Tags{
    CLASS,
    RACIAL,
    FEAT,
    BACKGROUND,
    OTHER
}

data class Feature(
    val title: String,
    val tag: Tags,
    val source: String,
    val description: String,

    /* Example:
     * title  | "Danger Sense"
     * tag    | Tags.CLASS
     * source | "Barbarian Level 2"
     * desc'  | "At 2nd level, ... you can't be blinded, deafened, or incapacitated"
     */
)
