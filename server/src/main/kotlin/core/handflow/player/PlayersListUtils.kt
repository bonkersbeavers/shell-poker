package core.handflow.player

fun List<Player>.orderedBySeats(startingSeat: Int): List<Player> {
    val sortedPlayers = sortedBy { it.seat }
    val begin = sortedPlayers.filter { it.seat >= startingSeat }
    val end = sortedPlayers.filter { it.seat < startingSeat }
    return begin + end
}

fun List<Player>.next(from: Int): Player = orderedBySeats(from + 1).first()
fun List<Player>.prev(from: Int): Player = orderedBySeats(from).last()

fun List<Player>.nextDecisive(from: Int): Player? = orderedBySeats(from + 1).find { it.isDecisive }

fun List<Player>.inGame(): List<Player> = this.filter { it.isInGame }
fun List<Player>.decisive(): List<Player> = this.filter { it.isDecisive }

fun List<Player>.getBySeat(seat: Int): Player? = this.find { it.seat == seat }
