package core.gameflow

fun findNextPlayer(players: List<Player>, position: Int): Player {
    val sortedPlayers = players.sortedBy { it.position }
    val nextPlayer = sortedPlayers.find { it.position > position }

    return when {
        nextPlayer != null -> nextPlayer
        else -> sortedPlayers.first()
    }
}

fun getActivePlayers(players: List<Player>): List<Player>  = players.filter { !it.folded }