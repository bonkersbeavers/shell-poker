package core.hand.utils

import core.hand.HandState
import core.hand.player.betting.Post

fun resolveBlindPosting(handState: HandState, newPlayersSeats: Collection<Int>): List<Post> {
    // TODO()
    return listOf(
            Post(seat = handState.positions.smallBlind, chips = handState.blinds.smallBlind),
            Post(seat = handState.positions.bigBlind, chips = handState.blinds.bigBlind)
            )
}
