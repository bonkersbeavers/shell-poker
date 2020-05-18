package core.hand.utils

import core.hand.HandState
import core.hand.player.orderedBySeats
import core.hand.pot.AwardChips
import core.hand.pot.PotAction
import core.hand.pot.ReturnChips

fun getPotActionsSequence(handState: HandState): Iterable<PotAction> {
    // TODO()
    val orderedPlayers = handState.playersStates.orderedBySeats(handState.positions.button + 1)
    val potActionSequence = mutableListOf<PotAction>()

    // pots are resolved from last to first
    for (pot in handState.pots.reversed()) {

        val potPlayers = orderedPlayers.filter { it.isInGame and (it.seat in pot.playersSeats) }

        if (potPlayers.count() == 1) {

            if (pot == handState.pots.last()) {
                val returnAction = ReturnChips(chips = pot.size, playerSeat = potPlayers.first().seat)
                potActionSequence.add(returnAction)
            } else {
                val awardAction = AwardChips(chips = pot.size, playerSeat = potPlayers.first().seat, potNumber = pot.potNumber)
                potActionSequence.add(awardAction)
            }

            continue
        }

        val bestHand = potPlayers.map { it.makeHand(handState.communityCards) }.max()!!

        val potWinners = potPlayers.filter { it.makeHand(handState.communityCards).compareTo(bestHand) == 0 }
        val chipsWon = pot.size / potWinners.count()

        // TODO: add odd chips distribution
        for (winner in potWinners) {
            val awardAction = AwardChips(chips = chipsWon, playerSeat = winner.seat, potNumber = pot.potNumber)
            potActionSequence.add(awardAction)
        }
    }

    return potActionSequence.toList()
}
