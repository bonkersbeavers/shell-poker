package core.hand.player

import core.hand.PlayerState

fun List<PlayerState>.orderedBySeats(startingSeat: Int): List<PlayerState> {
    val sortedPlayerStates = sortedBy { it.seat }
    val begin = sortedPlayerStates.filter { it.seat >= startingSeat }
    val end = sortedPlayerStates.filter { it.seat < startingSeat }
    return begin + end
}

fun List<PlayerState>.next(from: Int): PlayerState = orderedBySeats(from + 1).first()
fun List<PlayerState>.prev(from: Int): PlayerState = orderedBySeats(from).last()

fun List<PlayerState>.nextDecisive(from: Int): PlayerState? = orderedBySeats(from + 1).find { it.isDecisive }

fun List<PlayerState>.inGame(): List<PlayerState> = this.filter { it.isInGame }
fun List<PlayerState>.decisive(): List<PlayerState> = this.filter { it.isDecisive }

fun List<PlayerState>.getBySeat(seat: Int): PlayerState? = this.find { it.seat == seat }
