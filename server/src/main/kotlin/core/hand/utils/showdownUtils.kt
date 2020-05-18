package core.hand.utils

import core.hand.HandState
import core.hand.player.orderedBySeats
import core.hand.player.showdown.ShowCards
import core.hand.player.showdown.ShowdownAction

fun getShowdownActionsSequence(handState: HandState): List<ShowdownAction> {
    //    TODO()
    val startingSeat =
            if (handState.lastAggressor != null) handState.lastAggressor.seat
            else handState.positions.button + 1

    val orderedPlayers = handState.playersStates.orderedBySeats(startingSeat)
    return orderedPlayers.map { ShowCards(it.seat, it.cards!!) }
}
