package com.leadwoods.dissertation

import com.leadwoods.dissertation.local_database.character.CharacterSheet

class characterCreator {
    fun createTestCharacter(): CharacterSheet {
        return CharacterSheet(name= "Test Subject")
    }
}

