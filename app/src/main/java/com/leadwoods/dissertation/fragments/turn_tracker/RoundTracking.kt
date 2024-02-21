package com.leadwoods.dissertation.fragments.turn_tracker


import com.leadwoods.dissertation.support.Logger

enum class TurnTag{
    PLAYER,
    EFFECT,
    DEBUG,
    GM
}

/**
 * Represents a turn in a cyclical round structure
 * @see RoundTracker
 * @see TurnTag
 *
 * @property tag Specifies the group a turn belongs to, used for filtering & visibility control
 * @property playerID Specifies the players a turn belongs to, defaults: -1 to specify no owner
 * @property initiative Representative of the position in the order of the round. Values are always 0 or greater
 * @property targetID References the characterID of a [CharacterSheet] to associate the turn with
 * @property tokenArtID Reference for art not associated with a [CharacterSheet]
 */
class Turn(
    private var tags: ArrayList<TurnTag> = arrayListOf(TurnTag.GM),
    private var playerID: Int? = null,
    private var initiative: Float = 0.0f,
    private var targetID: Int? = null,
    private var tokenArtID: Int? = null
){
    fun updateIniative(newInitiative: Float){

    }

    fun updateTokenArt(newTokenArtID: Int){
        tokenArtID = newTokenArtID
    }

    fun getTags(): ArrayList<TurnTag>{
        return tags
    }

    fun getPlayerID(): Int? {
        return playerID
    }

    fun getIniative(): Float{
        return initiative
    }



    fun getCharacterID(): Int?{
        return targetID
    }

    fun getTokenArtID(): Int? {
        return tokenArtID
    }
}

/**
 * Local store of round data, updated by [TODO()]
 *
 * The RoundTracker class maintains the turn order and provides methods for creating, adding, removing, and selecting turns, as well as manipulating the current turn counter.
 *
 * @property turns The list of turns in the round.
 * @property roundLength The total number of turns in the round.
 * @property currentTurn The index of the current turn.
 * @property resourceLock An object used for thread synchronization.
 */
class RoundTracker(
    private var turns: ArrayList<Turn> = ArrayList(),
    private var roundLength: Int = 0,
    private var currentTurn: Int = 0,
    private val resourceLock: Any = Any()
) {

    companion object {
        @Volatile
        private var instance: RoundTracker? = null

        fun getInstance(): RoundTracker {
            return instance?: synchronized(this) {
                instance ?: RoundTracker().also { instance = it }
            }
        }
    }
    /**
     * Retrieves the index of the current turn.
     * @return The index of the current turn.
     */
    fun getCurrentTurnIndex(): Int{
        return currentTurn
    }

    fun getCurrentTurn(): Turn {
        return turns[currentTurn]
    }

    /**
     * Retrieves the turn corresponding to a given index in the array
     * @return [Turn] object for the given index
     */
    fun getTurnByIndex(index: Int): Turn{
        return turns[index]
    }

    /**
     * Retrieves the total number of turns in the round.
     * @return The total number of turns in the round.
     */
    fun getRoundLength(): Int{
        return roundLength
    }

    /**
     * Creates a new turn with the specified parameters.
     * @param turnTag The tag associated with the new turn.
     * @param initiative The initiative value of the new turn.
     * @param targetID The ID of the target associated with the new turn.
     * @param tokenArtID The ID of the token art associated with the new turn.
     * @return The newly created turn.
     */
    fun createTurn(turnTags: ArrayList<TurnTag>, initiative: Float, targetID: Int, tokenArtID: Int): Turn{
        return Turn(
            tags = turnTags,
            initiative = initiative,
            targetID = targetID,
            tokenArtID = tokenArtID
        )
    }

    /**
     * Adds a new turn to the round.
     * @param newTurn The turn to be added.
     */
    fun addTurn(newTurn: Turn){
        synchronized(resourceLock) {
            val newIndex = findInsertionIndex(newTurn.getIniative())
            turns.add(newIndex, newTurn)
            roundLength += 1
        }
    }

    /**
     * Selects a turn from the round by its ID.
     * @param id The ID of the turn to select.
     */
    fun selectTurnByID(id: Int){
        TODO()
    }

    /**
     * Removes a turn from the round.
     * @param turnToRemove The turn to be removed.
     */
    fun removeTurn(turnToRemove: Turn){
        synchronized(resourceLock) {
            if (roundLength == 0)
                return

            turns.remove(turnToRemove)
            roundLength -= 1
        }
    }

    /**
     * Removes all turns with the specified tag from the round.
     * @param tag The tag of the turns to be removed.
     */
    fun removeAll(tag: TurnTag){
        turns.removeAll{
            it.getTags().contains(tag)
        }
    }

    /**
     * Increments the current turn counter, wrapping around to the beginning if necessary.
     * @return The new current turn index
     */
    fun incrementTurnCounter(){
        currentTurn +=1
        if(currentTurn >= roundLength)
            currentTurn = 0
    }

    /**
     * Decrements the current turn counter, wrapping around to the end if necessary.
     * @return The new current turn index
     */
    fun decrementTurnCounter(){
        currentTurn -=1
        if(currentTurn <= -1)
            currentTurn = roundLength-1
    }

    fun resetTurnCounter(){
        currentTurn = 0
    }

    fun skipTo(skipToTag: TurnTag, direction: Boolean) {
        Logger.d("called | skipping from: $currentTurn")
        val increment = if (direction) 1 else -1
        var currentIndex = currentTurn

        // Update the current turn index based on the specified direction
        var count = 0
        while (count < roundLength) {
            currentIndex = (currentIndex + increment + roundLength) % roundLength
            val turn = turns[currentIndex]
            if (turn.getTags().contains(skipToTag)) {
                currentTurn = currentIndex
                Logger.d("ends | skipped to: $currentTurn")
                return
            }
            count++
        }
    }

    /**
     * Finds the insertion index for a new turn based on its initiative value.
     * @param newInitiative The initiative value of the new turn.
     * @return The insertion index for the new turn.
     */
    private fun findInsertionIndex(newInitiative: Float): Int{
        var left = 0
        var right = turns.size -1

        while(left <= right){
            val mid = left + (right - left) / 2

            val midInitiative = turns[mid].getIniative()

            if(newInitiative > midInitiative)
                right = mid -1
            else if (newInitiative < midInitiative)
                left = mid + 1
            else
                return mid

        }
        return left
    }
}