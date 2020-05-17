package core.dealer

import core.cards.Card
import core.cards.baseDeck
import core.flowUtils.BettingRound
import core.handflow.HandFlowException
import core.handflow.HandState
import core.handflow.rebuild
import kotlin.random.Random

class Dealer : IDealer {
    private var deckIterator: ListIterator<Card>? = null

    override fun deal(handState: HandState): HandState {
        return when (handState.bettingRound) {
            null -> dealHoleCards(handState).rebuild(bettingRound = BettingRound.PRE_FLOP)
            BettingRound.PRE_FLOP -> dealFlop(handState).rebuild(bettingRound = BettingRound.FLOP)
            BettingRound.FLOP -> dealTurn(handState).rebuild(bettingRound = BettingRound.TURN)
            BettingRound.TURN -> dealRiver(handState).rebuild(bettingRound = BettingRound.RIVER)
            BettingRound.RIVER -> throw HandFlowException("cannot deal any cards when already on river")
        }
    }

    fun dealHoleCards(handState: HandState): HandState {
        deckIterator!!

        assert(handState.bettingRound == BettingRound.PRE_FLOP)

        val updatedPlayers = handState.players.map { it.withCards(listOf(deckIterator!!.next(), deckIterator!!.next())) }
        return handState.rebuild(players = updatedPlayers)
    }

    fun dealFlop(handState: HandState): HandState {
        deckIterator!!

        assert(handState.bettingRound == BettingRound.FLOP)

        val flopCards = listOf(deckIterator!!.next(), deckIterator!!.next(), deckIterator!!.next())
        return handState.rebuild(communityCards = flopCards)
    }

    fun dealTurn(handState: HandState): HandState {
        deckIterator!!

        assert(handState.bettingRound == BettingRound.TURN)

        val turnCard = deckIterator!!.next()
        return handState.rebuild(communityCards = handState.communityCards + turnCard)
    }

    fun dealRiver(handState: HandState): HandState {
        deckIterator!!

        assert(handState.bettingRound == BettingRound.RIVER)

        val riverCard = deckIterator!!.next()
        return handState.rebuild(communityCards = handState.communityCards + riverCard)
    }

    override fun shuffle(seed: Int?) {
        deckIterator = when (seed) {
            null -> baseDeck.shuffled().listIterator()
            else -> baseDeck.shuffled(Random(seed)).listIterator()
        }
    }

    fun setColdDeck(cards: List<Card>) {
        assert(cards.size == 52)
        assert(cards.distinct() == cards)
        deckIterator = cards.listIterator()
    }
}
