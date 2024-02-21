package com.leadwoods.dissertation.activities.session_manager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leadwoods.dissertation.Lobby
import com.leadwoods.dissertation.R
import com.leadwoods.dissertation.support.Logger



class SessionAdapter(private val sessionList: List<Lobby>, private val callBack: SessionTouchInterface)
    : RecyclerView.Adapter<SessionAdapter.Companion.SessionHolder>(){
    interface SessionTouchInterface{
        fun sessionJoinRequested(roomName: String)
    }
    companion object{
        class SessionHolder(view: View): RecyclerView.ViewHolder(view){
            val roomNameTV: TextView = view.findViewById(R.id.TV_RoomName)
            val roomPlayersTV: TextView = view.findViewById(R.id.TV_RoomPlayerCount)
            val joinRoomButton: Button = view.findViewById(R.id.B_JoinRoom)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        Logger.d("called | building SessionHolder")
        return SessionHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_available_room, parent, false))
    }

    override fun getItemCount(): Int {
        Logger.d("called | getting list size")
        return sessionList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        Logger.d("called | Binding View for position: $position")

        val lobby = sessionList[position]

        holder.roomNameTV.text = lobby.roomName
        holder.roomPlayersTV.text = "${lobby.playerCount?.first?: "?"}/${lobby.playerCount?.second?: "?"}"

        if((lobby.playerCount?.first ?: 99) < (lobby.playerCount?.second ?: 0)){
            holder.joinRoomButton.apply {
                alpha = 1f
                isClickable = true
                setOnClickListener{ callBack.sessionJoinRequested(lobby.roomName?: "Room Name Unknown") }
            }
        } else {
            holder.joinRoomButton.apply {
                alpha = .5f
                isClickable = false
            }
        }
    }
}