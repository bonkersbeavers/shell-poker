package core.gameflow

import core.cards.Card
import core.cards.baseDeck
import kotlin.random.Random

class Dealer() {
    private var deckIterator: ListIterator<Card>? = null

    fun dealHoleCards(handState: HandState): HandState {
        deckIterator!!

        for(player: Player in handState.players)
            assert(player.holeCards.isEmpty())

        val updatedPlayers = handState.players.map { it.withCards(listOf(deckIterator!!.next(), deckIterator!!.next())) }
        return handState.copy(players = updatedPlayers)
    }

    fun dealFlop(handState: HandState): HandState {
        deckIterator!!

        assert(handState.communityCards.isEmpty())

        val flopCards = listOf(deckIterator!!.next(), deckIterator!!.next(), deckIterator!!.next())
        return handState.copy(communityCards = flopCards)
    }

    fun dealTurn(handState: HandState): HandState {
        deckIterator!!

        assert(handState.communityCards.size == 3)

        val turnCard = deckIterator!!.next()
        return handState.copy(communityCards = handState.communityCards + turnCard)
    }

    fun dealRiver(handState: HandState): HandState {
        deckIterator!!

        assert(handState.communityCards.size == 4)

        val riverCard = deckIterator!!.next()
        return handState.copy(communityCards = handState.communityCards + riverCard)
    }

    fun shuffle(seed: Int? = null) {
        deckIterator = when (seed) {
            null -> baseDeck.shuffled().listIterator()
            else -> baseDeck.shuffled(Random(seed)).listIterator()
        }
    }

    fun setColdDeck(cards: List<Card>) {
        deckIterator = cards.listIterator()
    }
}
