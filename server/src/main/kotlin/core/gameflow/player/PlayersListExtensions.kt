package core.gameflow.player

fun List<Player>.ordered(startingPosition: Int): List<Player> {
    val sortedPlayers = sortedBy { it.position }
    val begin = sortedPlayers.filter { it.position >= startingPosition }
    val end = sortedPlayers.filter { it.position < startingPosition }
    return begin + end
}

fun List<Player>.next(from: Int): Player = ordered(from + 1).first()
fun List<Player>.next(from: Player): Player = next(from.position)

fun List<Player>.prev(from: Int): Player = ordered(from).last()

fun List<Player>.nextDecisive(from: Int): Player? = ordered(from + 1).find { it.isDecisive }
fun List<Player>.nextDecisive(from: Player): Player? = nextDecisive(from.position)

fun List<Player>.inGame(): List<Player> = this.filter { it.isInGame }
fun List<Player>.decisive(): List<Player> = this.filter { it.isDecisive }

fun List<Player>.getByPosition(position: Int): Player? = this.find { it.position == position }
