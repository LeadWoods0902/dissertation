package com.leadwoods.dissertation.local_database.character

import android.util.Log

val TAG = "INVENTORY"

enum class ItemType {
    WEAPON,
    ARMOUR,
    POTION,
    SCROLL,
    VALUABLE,
    CONSUMABLE,
    MISC
}

data class InventoryItem(
    val name: String,
    val type: ItemType,
    val description: String = "",
    val value: List<Int> = listOf(0, 0, 0, 0, 0),
    var quantity: Int = 1
){

    fun increaseQuantity(amount: Int = 1){
        quantity+=amount
    }

    fun decreaseQuantity(amount: Int = 1): Boolean{
        return if(quantity >= amount){
            quantity-= amount
            true
        }
        else{
            Log.w(TAG, "Cannot decrease quantity below zero")
            false
        }
    }
}