package com.leadwoods.dissertation

import com.leadwoods.dissertation.local_database.character.Character

class characterCreator {
    fun createTestCharacter(): Character {
        return Character(name= "Test Subject")
    }
}

