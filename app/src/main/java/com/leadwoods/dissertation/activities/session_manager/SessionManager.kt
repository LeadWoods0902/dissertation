package com.leadwoods.dissertation.activities.session_manager

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leadwoods.dissertation.Lobby
import com.leadwoods.dissertation.R
import com.leadwoods.dissertation.activities.main_switcher.MainActivity
import com.leadwoods.dissertation.support.Base
import com.leadwoods.dissertation.support.GAME_MASTER
import com.leadwoods.dissertation.support.Logger
import kotlin.random.Random

/**
 *
 */
class ErrorDialog(private val message: String): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Okay") { dialog, _ ->
                dialog.cancel()

            }.create()
    }

}

/**
 *
 */
class JoinRoomDialog(private val roomName: String, private val onConfirmed: (ArrayList<String>) -> Unit): DialogFragment(){
//    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
//        val codeEntryET = EditText(requireContext())
//        codeEntryET.maxLines = 1
//        codeEntryET.hint = "Enter Room Code"
//        codeEntryET.layoutParams
//
//        return AlertDialog.Builder(requireContext())
//            .setMessage(roomName)
//            .setView(codeEntryET)
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.cancel()
//            }
//            .setPositiveButton("Join") { _, _ ->
//                val roomCode = codeEntryET.text.toString().trim()
//                if (roomCode.length == 6 && roomCode.matches(Regex("[A-Za-z0-9]+"))) {
//                    onConfirmed(arrayListOf(roomCode))
//                } else {
//                    ErrorDialog("error in room code syntax, please check your code",)
//                        .show(requireActivity().supportFragmentManager, "error")
//                }
//            }
//            .create()
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_join_room, null)

        val codeEntryET: EditText = dialogView.findViewById(R.id.ET_RoomCode)
        val joinableRoomNameTV: TextView = dialogView.findViewById(R.id.TV_JoinableRoomName)

        joinableRoomNameTV.text = roomName

        return AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Join") { _, _ ->
                val roomCode = codeEntryET.text.toString().trim()
                if (roomCode.length == 6 && roomCode.matches(Regex("[A-Za-z0-9]+"))) {
                    onConfirmed(arrayListOf(roomCode))
                } else {
                    ErrorDialog("error in room code syntax, please check your code",)
                        .show(requireActivity().supportFragmentManager, "error")
                }
            }
            .create()
    }

}

/**
 *
 */
class HostRoomDialog(private val onConfirmed: (ArrayList<String>) -> Unit) : DialogFragment() {

    /**
     *
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_host_room, null)
        val editTextRoomName = dialogView.findViewById<EditText>(R.id.TV_JoinableRoomName)
        val editTextRoomCode = dialogView.findViewById<EditText>(R.id.ET_RoomCode)
        val seekBarMaxPlayers = dialogView.findViewById<SeekBar>(R.id.SB_MaxPlayers)
        val seekBarPlayerPermission = dialogView.findViewById<SeekBar>(R.id.SB_PlayerPermissions)

        return AlertDialog.Builder(requireContext())
            .setTitle("Host Room")
            .setView(dialogView)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Host") { _, _ ->
                val roomName = editTextRoomName.text.toString().trim()
                val roomCode = editTextRoomCode.text.toString().trim()
                val maxPlayers = seekBarMaxPlayers.progress + 1
                val playerPermission = seekBarPlayerPermission.progress + 1

                if (roomCode.length == 6 && roomCode.matches(Regex("[A-Za-z0-9]+"))) {
                    showConfirmationDialog(roomName, roomCode, maxPlayers, playerPermission)
                } else {
                    ErrorDialog("Error in Room Configuration, please try again")
                        .show(requireActivity().supportFragmentManager, "error")
                }
            }
            .create()
    }

    /**
     *
     */
    private fun showConfirmationDialog(roomName: String, roomCode: String, maxPlayers: Int, playerPermission: Int) {

        val permissionString = when(playerPermission){
            0 -> "View Only"
            1 -> "Local Edits Only"
            2 -> "Server Sync"
            else -> "Unknown Permission Level"
        }
        val confirmationDialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage(
                "Room Name: $roomName\n" +
                        "Room Code: $roomCode\n" +
                        "Max Players: $maxPlayers\n" +
                        "Permission Level: $permissionString"
            )
            .setPositiveButton("Confirm") { _, _ ->
                onConfirmed(arrayListOf(roomName, roomCode, maxPlayers.toString(), playerPermission.toString()))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        confirmationDialog.show()
    }
}

/**
 * @property sharedPreferences access to universal preference settings
 */
class SessionManager : Base(), SessionAdapter.SessionTouchInterface {

    private lateinit var sharedPreferences: SharedPreferences
    
    private lateinit var hostRoomB: Button
    private lateinit var availableRoomsRV: RecyclerView
    private lateinit var noRoomsAvailableTV: TextView

    private var lobbies = ArrayList<Lobby>()

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_manager)

        hostRoomB = findViewById(R.id.B_HostRoom)

        availableRoomsRV = findViewById(R.id.RV_AvailableRooms)
        availableRoomsRV.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
//        ItemSnapHelper().attachToRecyclerView(availableRoomsRV)

        noRoomsAvailableTV = findViewById(R.id.TV_NoRoomsAvailable)
        noRoomsAvailableTV.visibility = View.VISIBLE

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)




        hostRoomB.setOnClickListener {
            HostRoomDialog {data: ArrayList<String> ->
                sharedPreferences.edit().apply {
                    putBoolean(GAME_MASTER, true)
                }.apply()

                Toast.makeText(this, "Hosting: ${data[0]}, with code: ${data[1]} \nMax: ${data[2]} @ ${data[3]}", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
            }.show(supportFragmentManager , "Host Room")
        }

        hostRoomB.setOnLongClickListener {
            Logger.d("Long Press Detected on B_HostRoom")
            generateTestLobbies()
            updateUI()
            true
        }
    }

    private fun updateUI() {
        availableRoomsRV.adapter = SessionAdapter(lobbies, this)


        if(lobbies.size > 0)
            noRoomsAvailableTV.visibility = View.GONE
        else
            noRoomsAvailableTV.visibility = View.VISIBLE
    }

    private fun generateTestLobbies(){
        val random = Random

        for(index in 0..10){
            val max = random.nextInt(10)+1
            lobbies.add(Lobby(
                roomName = "Test Room #$index",
                playerCount = Pair(random.nextInt(max), max)
            ))
        }

        for(index in 11..20){
            val max = random.nextInt(10)+1
            lobbies.add(Lobby(
                roomName = "Test Room #$index",
                playerCount = Pair(max, max)
            ))
        }
    }

    override fun sessionJoinRequested(roomName: String) {
        JoinRoomDialog(roomName){ data: ArrayList<String> ->
            if(!attemptConnectToHost(data[0])) {
                ErrorDialog("Incorrect Code").show(supportFragmentManager, "invalid code")
                return@JoinRoomDialog
            }

            sharedPreferences.edit().apply {
                putBoolean(GAME_MASTER, false)
            }.apply()

            Toast.makeText(this, "Connected to: $roomName, with code: ${data[0]}", Toast.LENGTH_SHORT).show()
            Logger.d("Connected to: $roomName, with code: ${data[0]}")

            startActivity(Intent(this, MainActivity::class.java))

        }.show(supportFragmentManager , "Join Room")
    }

    private fun attemptConnectToHost(roomCode: String): Boolean{
        Logger.d("called | ${if(roomCode == "090242") "Correct Code" else "Incorrect Code"}")
        return roomCode == "090242"
    }
}
