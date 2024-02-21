package com.leadwoods.dissertation.fragments.turn_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leadwoods.dissertation.R
import com.leadwoods.dissertation.support.Logger
import kotlin.properties.Delegates

class TrackedTurnsAdapter(
    private val trackedTurns: RoundTracker,
    private val gmFlag: Boolean,
): RecyclerView.Adapter<TrackedTurnsAdapter.Companion.CharacterTurnHolder>() {

    private var startingIndex by Delegates.notNull<Int>()

    init{
        startingIndex = trackedTurns.getCurrentTurnIndex()+1
    }

    companion object{
        class CharacterTurnHolder(view: View): RecyclerView.ViewHolder(view){

            var characterName: TextView = view.findViewById(R.id.TV_CharacterName)
            var characterInitiative: TextView = view.findViewById(R.id.TV_Initiative)
            var characterToken: ImageView = view.findViewById(R.id.IV_Token)

            var characterStatuses: RecyclerView = view.findViewById(R.id.RV_Statuses)

            var characterTrackables: LinearLayout = view.findViewById(R.id.LL_CharacterTrackables)
//            var characterTrackableA: ProgressBar = view.findViewById(R.id.PB_CharacterTrackable_A)
//            var characterTrackableB: ProgressBar = view.findViewById(R.id.PB_CharacterTrackable_B)
//            var characterTrackableC: ProgressBar = view.findViewById(R.id.PB_CharacterTrackable_C)
        }

        class TextTurnHolder(view: View): RecyclerView.ViewHolder(view){
            var turnTextView: TextView = view.findViewById(R.id.TV_TurnText)
        }
        class ImageTurnHolder(view: View): RecyclerView.ViewHolder(view){
            var turnImageView: TextView = view.findViewById(R.id.IV_TurnImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterTurnHolder {
        Logger.d("called | building TurnHolder")
        return CharacterTurnHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_turn_tracker_character, parent, false))
    }

    override fun getItemCount(): Int {
        Logger.d("called | Item Count: ${trackedTurns.getRoundLength()}")
        return trackedTurns.getRoundLength()-1
    }

    override fun onBindViewHolder(holder: CharacterTurnHolder, position: Int) {
        val index = (startingIndex+position) % (trackedTurns.getRoundLength())
        Logger.d("called | Binding View for position: $position | Using data from trackedTurn: $index")

        val turn = trackedTurns.getTurnByIndex(index)
        val characterID = turn.getCharacterID()
//        val characterSheet

        holder.characterName.text = turn.getCharacterID().toString()

        holder.characterInitiative.text = turn.getIniative().toString()

        holder.characterToken.setImageResource(turn.getTokenArtID()?: R.drawable.unknown_token)

    }
}