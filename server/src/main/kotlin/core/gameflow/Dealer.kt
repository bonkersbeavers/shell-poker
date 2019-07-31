package core.gameflow

import core.cards.Card

class Dealer(deck: List<Card>){
    private val deckIterator = deck.listIterator()

    fun dealHoleCards(handState: HandState): HandState {
        val oldPlayers = handState.players
        val newPlayers: MutableList<Player> = mutableListOf()

        for (player: Player in oldPlayers) {
            newPlayers.add(
                    player.withCards(listOf(deckIterator.next(), deckIterator.next()))
            )
        }

        return handState.copy(players = newPlayers.toList())
    }
}
