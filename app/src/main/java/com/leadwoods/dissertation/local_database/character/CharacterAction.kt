package com.leadwoods.dissertation.local_database.character

enum class ActionType {
    ACTION,
    BONUS_ACTION,
    CANTRIP,
    SPELL_1,
    SPELL_2,
    SPELL_3,
    SPELL_4,
    SPELL_5,
    SPELL_6,
    SPELL_7,
    SPELL_8,
    SPELL_9,
    ERROR
}

class CharacterAction(
    var name: String,
    var type: ActionType,
    var description: String
)


