package com.leadwoods.dissertation.fragments.turn_tracker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leadwoods.dissertation.R
import com.leadwoods.dissertation.databinding.FragmentTurnTrackerBinding
import com.leadwoods.dissertation.support.GAME_MASTER
import kotlin.random.Random

class TurnTrackerFragment : Fragment() {

    private var _binding: FragmentTurnTrackerBinding? = null
    private val binding get() = _binding!!

    private var roundTracker = RoundTracker.getInstance()

    private lateinit var upcomingTurnsRV: RecyclerView
    private lateinit var currentTurnFL: FrameLayout


    private lateinit var nextTurnIB: ImageButton
    private lateinit var previousTurnIB: ImageButton
    private lateinit var skipRoundIB: ImageButton
    private lateinit var addTurnIB: ImageButton

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTurnTrackerBinding.inflate(inflater, container, false)

        upcomingTurnsRV = binding.RVUpcomingTurns
        currentTurnFL = binding.FLCurrentTurn

        nextTurnIB = binding.IBNextTurn
        previousTurnIB = binding.IBPreviousTurn
        skipRoundIB = binding.IBSkipRound
        addTurnIB = binding.IBAddTurn

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())

        upcomingTurnsRV.layoutManager = LinearLayoutManager(this.requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }


        // define GM Button functionality
        configUserControls()

        updateUI()

        return binding.root
    }

    private fun configUserControls() {

        val gmFlag = sharedPreferences.getBoolean(GAME_MASTER, false)

        if(!gmFlag) {
            binding.LLTurnTrackerControls.visibility = View.GONE
            return
        }

        // continue to next turn
//        nextTurnFAB.setOnClickListener {
        nextTurnIB.setOnClickListener {
            roundTracker.incrementTurnCounter()
            updateUI()
        }

        // Skip to next Player
        nextTurnIB.setOnLongClickListener {
            roundTracker.skipTo(TurnTag.PLAYER, true)
            updateUI()
            true
        }

        // return to previous turn
//        previousTurnFAB.setOnClickListener {
        previousTurnIB.setOnClickListener {
            roundTracker.decrementTurnCounter()
            updateUI()
        }

        // Skip to previous Player
        previousTurnIB.setOnLongClickListener {
            roundTracker.skipTo(TurnTag.PLAYER, false)
            updateUI()
            true
        }

//        skipRoundFAB.setOnClickListener {
        skipRoundIB.setOnClickListener {
            roundTracker.resetTurnCounter()
            updateUI()
        }

//        addTurnFAB.setOnClickListener {
        addTurnIB.setOnClickListener {
            Toast.makeText(this.requireContext(), "You can add a new turn using me :)", Toast.LENGTH_SHORT).show()
        }

        addTurnIB.setOnLongClickListener {
            // load example characters for debugging
            if(roundTracker.getRoundLength() == 0)
                loadExampleCharacters()

            updateUI()

            true
        }

//        toggleControlsFAB.setOnClickListener {
//            val visibility = if (nextTurnFAB.visibility == View.VISIBLE) View.GONE else View.VISIBLE
//
//            nextTurnFAB.visibility =  visibility
//            previousTurnFAB.visibility = visibility
//            skipRoundFAB.visibility = visibility
//            addTurnFAB.visibility = visibility
//
//            toggleControlsFAB.setImageResource(if (nextTurnFAB.visibility == View.VISIBLE) R.drawable.visibility_off_24px else R.drawable.visibility_24px)
//        }
    }

    @SuppressLint("InflateParams")
    private fun updateUI() {
        if(roundTracker.getRoundLength() == 0)
            return

        val gmFlag = sharedPreferences.getBoolean(GAME_MASTER, false)

        val inflater = LayoutInflater.from(context)
        val currentCard = inflater.inflate(R.layout.card_turn_tracker_character, null)
        updateCurrentCard(currentCard)
        currentTurnFL.addView(currentCard)
        upcomingTurnsRV.adapter = TrackedTurnsAdapter(roundTracker, gmFlag)
    }

    private fun updateCurrentCard(currentCard: View) {
        val currentName: TextView = currentCard.findViewById(R.id.TV_CharacterName)
        val currentInitiative: TextView = currentCard.findViewById(R.id.TV_Initiative)
        val currentToken: ImageView = currentCard.findViewById(R.id.IV_Token)

        currentName.text = roundTracker.getCurrentTurn().getCharacterID().toString()
        currentInitiative.text = roundTracker.getCurrentTurn().getIniative().toString()
        currentToken.setImageResource(roundTracker.getCurrentTurn().getTokenArtID()?: R.drawable.unknown_token)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun finishCurrentTurn(){

    }

    private fun loadExampleCharacters() {
        val random = Random
        val characterArtArray = arrayOf(
            R.drawable.bismuth_token,
            R.drawable.jasper_token,
            R.drawable.duska_token,
            R.drawable.mazikeen_token,
            R.drawable.tom_token,
        )

        val creatureArtArray = arrayOf(
            R.drawable.arachnoconda_token,
            R.drawable.basilisk_token,
            R.drawable.gravolith_token,
        )

        var idIndex = 0

        repeat(5) {

            roundTracker.addTurn(
                Turn(
                    tags = arrayListOf(TurnTag.DEBUG, TurnTag.PLAYER),
                    initiative = random.nextInt(21).toFloat(),
                    targetID = idIndex,
                    playerID = idIndex,
                    tokenArtID = characterArtArray[idIndex] // Adjusted index for tokenArtArray
                )
            )

            idIndex+=1
        }

        repeat(5) {
            val id = random.nextInt(3)

            roundTracker.addTurn(
                Turn(
                    tags = arrayListOf(TurnTag.DEBUG),
                    initiative = random.nextInt(21).toFloat(),
                    targetID = id,
                    playerID = id,
                    tokenArtID = creatureArtArray[id] // Adjusted index for tokenArtArray
                )
            )

        }
    }

    private fun removeExampleCharacters(){
        roundTracker.apply {
             removeAll(TurnTag.DEBUG)
        }
    }
}