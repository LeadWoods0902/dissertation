package com.leadwoods.dissertation.local_database.character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.Update

enum class CharacterClass {
    ARTIFICER,
    BARBARIAN,
    BARD,
    CLERIC,
    DRUID,
    FIGHTER,
    MONK,
    PALADIN,
    RANGER,
    ROGUE,
    SORCERER,
    WARLOCK,
    WIZARD
}

@Entity
data class Character(
    @PrimaryKey(autoGenerate = true)
    val characterID: Int = 0,

    var name: String = "Edit Name",
    var characterClass: CharacterClass? = null,
    var level: Int = 0,

//    var characterDetails: List<String> = listOf(),
//    var speed: Int = 0,
//    var armorClass: Int = 0,
//
//    var attributes: List<Int> = listOf(),
//    var proficiencyBonus: Int = 0,
//
//    var proficiencies: Proficiencies = Proficiencies(),
//    var skills: List<Skill> = listOf(),
//
//    var hitPoints: Triple<Int, Int, Int> = Triple(0,0,0),
//    var deathSaves: Pair<Int, Int> = Pair(0,0),
//
//    var wallet: List<Pair<String, Int>> = listOf(),
//    var inventory: List<InventoryItem> = listOf(),
//
//    var features: List<Feature> = listOf(),
//
//    var resources: List<Resource> = listOf(Resource("action", 1,1), Resource("bonus action", 1, 1)),
//
//    var spellSlots: List<Pair<Int, Int>> = listOf(
//        Pair(0,0), Pair(0,0), Pair(0,0), Pair(0,0), Pair(0,0),
//        Pair(0,0), Pair(0,0), Pair(0,0), Pair(0,0)
//    ),
//
//    var classResources: List<ClassResource> = listOf(),
//
//    var actions: List<CharacterAction> = listOf(),

    var notes: String = ""
    )

@Dao
interface CharacterDao{

    @Insert
    suspend fun insertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character:Character)

    @Update
    suspend fun updateCharacter(character: Character)

    @Transaction
    @Query("SELECT * FROM Character WHERE characterID = :searchTerm")
    suspend fun retrieveByID(searchTerm: Int): Character?

    @Transaction
    @Query("SELECT * FROM Character WHERE name = :name OR (:characterClass IS NULL OR characterClass = :characterClass) OR (:level IS NULL OR level = :level)")
    suspend fun retrieveCharacters(name: String?, characterClass: CharacterClass?, level: Int?): List<Character>?
}

class Converters{

    // Skill
    @TypeConverter
    fun fromSkills(skill: Skill?): String{
        if (skill== null)
            return ""

        return StringBuilder()
            .append("${skill.name}|")
            .append("${skill.type}|")
            .append("${skill.attribute}|")
            .append("${skill.bonus}|").toString()
    }
    @TypeConverter
    fun stringToSkill(skillString: String?): Skill?{
        if(skillString.isNullOrEmpty())
            return null

        val parts = skillString.split("|")
        if (parts.size != 4) {
            throw IllegalArgumentException("Invalid Skill string: $skillString")
        }

        val name = parts[0]

        val type: SkillType = try {
            SkillType.valueOf(parts[1])
        } catch (e:IllegalArgumentException) {
            SkillType.OTHER
        }

        val attribute: Attribute = try {
            Attribute.valueOf(parts[2])
        } catch (e: IllegalArgumentException) {
            Attribute.QUERY // default value or handle error as needed
        }

        val bonus = parts[3].toIntOrNull()?: 0

        return Skill(name, type, attribute, bonus)

    }


    // InventoryItem
    @TypeConverter
    fun fromInventoryItem(item: InventoryItem?): String{
        if (item== null)
            return ""

        return StringBuilder()
            .append("${item.name}|")
            .append("${item.type}|")
            .append("${item.description}|")
            .append(item.value.joinToString(",") { it.toString() } + "|")
            .append("${item.quantity}|").toString()

    }
    @TypeConverter
    fun stringToIventoryItem(itemString: String?): InventoryItem?{
        if(itemString.isNullOrEmpty())
            return null

        val parts = itemString.split("|")
        if (parts.size != 5) {
            throw IllegalArgumentException("Invalid inventoryItem string: $itemString")
        }

        val name = parts[0]

        val type: ItemType = try {
            ItemType.valueOf(parts[1])
        } catch (e:IllegalArgumentException) {
            ItemType.MISC
        }

        val description = parts[2]

        val value = parts[3].split(",").map { it.toInt() }

        val quantity = parts[4].toIntOrNull()?:1

        return InventoryItem(name, type, description, value, quantity)
    }


    // Feature
    @TypeConverter
    fun fromFeature(feature: Feature?): String {
        if(feature == null)
            return ""

        return StringBuilder()
            .append("${feature.title}|")
            .append("${feature.tag}|")
            .append("${feature.source}|")
            .append("${feature.description}|").toString()
    }
    @TypeConverter
    fun stringToFeature(featureString: String?): Feature? {
        if(featureString.isNullOrEmpty())
            return null

        val parts = featureString.split("|")
        if (parts.size != 4) {
            throw IllegalArgumentException("Invalid Feature string: $featureString")
        }

        val name = parts[0]
        val source = parts[2]
        val description = parts[3]

        val tag: Tags = try {
            Tags.valueOf(parts[1])
        } catch (e:IllegalArgumentException) {
            Tags.OTHER
        }

        return Feature(name, tag, source, description)
    }


    // Resource
    @TypeConverter
    fun fromResource(resource: Resource?): String {
        if(resource == null)
            return ""

        return StringBuilder()
            .append("${resource.type}|")
            .append("${resource.max}|")
            .append("${resource.current}|").toString()
    }
    @TypeConverter
    fun stringToResource(resourceString: String?): Resource? {
        if(resourceString.isNullOrEmpty())
            return null

        val parts = resourceString.split("|")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid Resource string: $resourceString")
        }

        val type = parts[0]
        val mutableList = parts[2].toIntOrNull()?:1
        val character = parts[3].toIntOrNull()?:1

        return Resource(type, mutableList, character)
    }


    // ClassResource
    @TypeConverter
    fun fromClassResource(classResource: ClassResource?): String {
        if(classResource == null)
            return ""

        return StringBuilder()
            .append("${classResource.name}|")
            .append("${classResource.max}|")
            .append("${classResource.min}|").toString()
    }
    @TypeConverter
    fun stringToClassResource(classResourceString: String?): ClassResource? {
        if(classResourceString.isNullOrEmpty())
            return null

        val parts = classResourceString.split("|")
        if(parts.size != 3)
            throw IllegalArgumentException("Invalid ClassResource string: $classResourceString")

        val name = parts[0]
        val max = parts[1].toIntOrNull()?:0
        val min =  parts[2].toIntOrNull()?:0

        return ClassResource(name, min, max)
    }


    // CharacterAction
    @TypeConverter
    fun fromCharacterAction(action: CharacterAction?): String {
        if(action == null)
            return ""

        return StringBuilder()
            .append("${action.name}|")
            .append("${action.type}|")
            .append("${action.description}|").toString()
    }
    @TypeConverter
    fun stringToCharacterAction(actionString: String?): CharacterAction? {
        if(actionString.isNullOrEmpty())
            return null

        val parts = actionString.split("|")
        if(parts.size != 3)
            throw IllegalArgumentException("Invalid ClassResource string: $actionString")

        val name = parts[0]
        val type: ActionType = try{
            ActionType.valueOf(parts[1])
        } catch (e:IllegalArgumentException){
            ActionType.ERROR
        }
        val description =  parts[2]

        return CharacterAction(name, type, description)

    }

}
