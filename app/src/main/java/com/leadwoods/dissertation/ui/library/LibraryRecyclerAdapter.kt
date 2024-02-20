package com.leadwoods.dissertation.ui.library

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


//class LibraryRecyclerAdapter(private val assetList: List<Asset>) : RecyclerView.Adapter<LibraryRecyclerAdapter.Companion.playerHolder>() {

/**
 *
 * @param assetList ArrayList of [TempAsset] to display in the library
 */
class LibraryRecyclerAdapter(private val assetList: List<TempAsset>) : RecyclerView.Adapter<LibraryRecyclerAdapter.Companion.PlayerHolder>() {

    /**
     *
     * container for asset view holders
     */
    companion object{
        /**
         * TODO: add values from [res/layout/card_turn_tracker_player.xml]
         *
         * @param view The view associated with this data in the recyclerview
         */
        class PlayerHolder(view: View): ViewHolder(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class TempAsset {

}
