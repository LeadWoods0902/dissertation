package com.leadwoods.dissertation.local_database.character

enum class SkillType{
    LANGUAGE,
    WEAPON,
    ARMOUR,
    TOOL,
    OTHER
}

enum class Attribute{
    STRENGTH,
    DEXTERITY,
    CONSTITUTION,
    INTELLIGENCE,
    WISDOM,
    CHARISMA,
    QUERY
}

class Skill(
    var name: String,
    var type: SkillType,
    var attribute: Attribute,
    var bonus: Int
)