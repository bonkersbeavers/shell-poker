package testutil.fake

import core.cards.Card
import core.gameflow.BettingRound
import core.gameflow.dealer.IDealer
import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild

class FakeHoldemDealer(val holeCards: List<Pair<Card, Card>>, val communityCards: List<Card>) : IDealer {

    override fun shuffle(seed: Int?) {}

    override fun deal(handState: HandState): HandState {
        return when (handState.bettingRound) {
            BettingRound.PRE_FLOP -> {
                val newPlayers = handState.players.mapIndexed {
                    index, player -> player.copy(holeCards = this.holeCards[index].toList())
                }

                handState.rebuild(players = newPlayers)
            }

            BettingRound.FLOP -> handState.rebuild(communityCards = this.communityCards.take(3))
            BettingRound.TURN -> handState.rebuild(communityCards = this.communityCards.take(4))
            BettingRound.RIVER -> handState.rebuild(communityCards = this.communityCards.take(5))
        }
    }
}
