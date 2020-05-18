package core.hand.showdown

import core.hand.HandState
import core.hand.Player
import core.hand.dealer.BettingRound
import core.hand.orderedBySeats

fun getShowdownActionsSequence(handState: HandState): List<ShowdownAction> {

    val startingSeat =
            if (handState.lastAggressor != null) handState.lastAggressor.seat
            else handState.positions.button + 1

    val orderedPlayers = handState.players.orderedBySeats(startingSeat).filter { it.isInGame }

    // if only one player is left, they can muck their cards
    if (orderedPlayers.count() == 1)
        return listOf(MuckCards(orderedPlayers.first().seat))

    // if dealer's action is not over yet, every player must reveal their hand
    if (handState.bettingRound != BettingRound.RIVER)
        return orderedPlayers.map { ShowCards(it.seat, it.cards!!) }

    // in other cases players show their hands only if they can get any chips by doing so
    val showdownSequence = mutableListOf<ShowdownAction>()
    val revealedPlayers = mutableListOf<Player>()

    for (player in orderedPlayers) {
        val playerHand = player.makeHand(handState.communityCards)
        val playerPotContribution = handState.seatToPotContribution.getOrDefault(player.seat, 0)

        val playerAction = if (revealedPlayers.any { otherPlayer ->
                    val otherPlayerHand = otherPlayer.makeHand(handState.communityCards)
                    val otherPlayerPotContribution = handState.seatToPotContribution.getOrDefault(otherPlayer.seat, 0)

                    (otherPlayerHand > playerHand) and (otherPlayerPotContribution >= playerPotContribution)
                }) {
            MuckCards(player.seat)
        } else {
            ShowCards(player.seat, player.cards!!)
        }

        showdownSequence.add(playerAction)
    }

    return showdownSequence
}
