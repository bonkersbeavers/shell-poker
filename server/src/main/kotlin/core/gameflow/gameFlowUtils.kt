package core.gameflow

fun findNextPlayer(players: List<Player>, position: Int): Player {
    val sortedPlayers = players.sortedBy { it.position }
    val nextPlayer = sortedPlayers.find { it.position > position }

    return when {
        nextPlayer != null -> nextPlayer
        else -> sortedPlayers.first()
    }
}

fun findNextActivePlayer(players: List<Player>, position: Int): Player {
    val foundPlayer = findNextPlayer(players, position)

    return when(foundPlayer.isActive()) {
        true -> foundPlayer
        else -> findNextActivePlayer(players, foundPlayer.position)
    }
}

fun findPrevPlayer(players: List<Player>, position: Int): Player {
    val sortedPlayers = players.sortedBy { it.position }
    val nextPlayer = sortedPlayers.find { it.position < position }

    return when {
        nextPlayer != null -> nextPlayer
        else -> sortedPlayers.first()
    }
}

fun findPrevActivePlayer(players: List<Player>, position: Int): Player {
    val foundPlayer = findPrevPlayer(players, position)
    
    return when(foundPlayer.isActive()) {
        true -> foundPlayer
        else -> findPrevActivePlayer(players, foundPlayer.position)
    }
}

