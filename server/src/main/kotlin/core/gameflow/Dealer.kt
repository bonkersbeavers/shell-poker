package core.gameflow

import core.cards.Card

class Dealer(deck: List<Card>) {
    private val deckIterator = deck.listIterator()

    fun dealHoleCards(handState: HandState): HandState {
        val updatedPlayers = handState.players.map { it.withCards(listOf(deckIterator.next(), deckIterator.next())) }
        return handState.copy(players = updatedPlayers)
    }

    fun dealFlop(handState: HandState): HandState {
        val flopCards = listOf(deckIterator.next(), deckIterator.next(), deckIterator.next())
        return handState.copy(communityCards = handState.communityCards + flopCards)
    }

    fun dealTurn(handState: HandState): HandState {
        val turnCard = deckIterator.next()
        return handState.copy(communityCards = handState.communityCards + turnCard)
    }

    fun dealRiver(handState: HandState): HandState {
        val riverCard = deckIterator.next()
        return handState.copy(communityCards = handState.communityCards + riverCard)
    }
}
