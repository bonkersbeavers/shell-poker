package core.gameflow

import core.cards.Card
import core.cards.baseDeck
import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import kotlin.random.Random

class Dealer {
    private var deckIterator: ListIterator<Card>? = null

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

    fun shuffle(seed: Int? = null) {
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
