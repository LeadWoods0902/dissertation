package com.leadwoods.dissertation

class Lobby(
    val roomName: String? = null,

    // Only to be stored Host-Side
    val roomCode: String? = null,

    var playerCount: Pair<Int, Int>? = null,
    var permissionLevel: Int? = null,
) {

}