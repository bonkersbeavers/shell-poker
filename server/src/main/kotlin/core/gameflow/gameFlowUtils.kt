package core.gameflow

fun findNextPlayer(players: List<Player>, position: Int): Player {
    val sortedPlayers = players.sortedBy { it.position }
    val nextPlayer = sortedPlayers.find { it.position > position }

    return when {
        nextPlayer != null -> nextPlayer
        else -> sortedPlayers.first()
    }
}

fun findNextActivePlayer(players: List<Player>, position: Int): Player? {
    if (players.all { !it.inGame() })
        return null

    val foundPlayer = findNextPlayer(players, position)

    return when(foundPlayer.inGame()) {
        true -> foundPlayer
        else -> findNextActivePlayer(players, foundPlayer.position)
    }
}

fun findPrevPlayer(players: List<Player>, position: Int): Player {
    val reverseSortedPlayers = players.sortedBy { it.position }.reversed()
    val prevPlayer = reverseSortedPlayers.find { it.position < position }

    return when {
        prevPlayer != null -> prevPlayer
        else -> reverseSortedPlayers.first()
    }
}

fun findPrevActivePlayer(players: List<Player>, position: Int): Player? {
    if (players.all { !it.inGame() })
        return null

    val foundPlayer = findPrevPlayer(players, position)

    return when(foundPlayer.inGame()) {
        true -> foundPlayer
        else -> findPrevActivePlayer(players, foundPlayer.position)
    }
}

