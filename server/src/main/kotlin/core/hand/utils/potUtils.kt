package core.hand.utils

import core.hand.HandState
import core.hand.player.orderedBySeats
import core.hand.pot.AwardChips
import core.hand.pot.PotAction

fun resolvePot(handState: HandState): List<PotAction> {
    // TODO()
    val orderedPlayers = handState.playersStates.orderedBySeats(handState.positions.button)
    return orderedPlayers.map { AwardChips(playerSeat = it.seat, chips = 0, potNumber = 0) }
}
