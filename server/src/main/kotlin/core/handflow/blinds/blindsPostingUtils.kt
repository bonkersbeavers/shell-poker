package core.handflow.blinds

import core.handflow.hand.HandState
import core.handflow.betting.Post

fun getBlindsPostActionsSequence(handState: HandState, newPlayersSeats: Collection<Int>): List<Post> {
    // TODO()
    return listOf(
            Post(seat = handState.positions.smallBlind, chips = handState.blinds.smallBlind),
            Post(seat = handState.positions.bigBlind, chips = handState.blinds.bigBlind)
            )
}
