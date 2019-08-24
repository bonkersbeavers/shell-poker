package core.gameflow

sealed class ShowdownAction

data class ShowCards(val playerId: Int) : ShowdownAction()
data class MuckCards(val playerId: Int) : ShowdownAction()

fun resolveShowdown(handState: HandState): List<ShowdownAction> {

    val orderedPlayers = when {
        handState.lastAggressor == null -> handState.orderedPlayers(handState.buttonPosition + 1)
        else -> handState.orderedPlayers(handState.lastAggressor.position)
    }.filter { it.isInGame }

    // when only one player is left in the game
    if (orderedPlayers.size == 1)
        return listOf(MuckCards(orderedPlayers.first().id))

    // all players must show their cards if there are more streets to be dealt
    if (handState.bettingRound != BettingRound.RIVER)
        return orderedPlayers.map { ShowCards(it.id) }

    // one player definitely wins with another if he has a better hand
    // and he has put at least the same amount of chips into the pot
    fun definitelyWins(player: Player, other: Player): Boolean {
        val communityCards = handState.communityCards
        return (player.hand(communityCards) > other.hand(communityCards)) and (player.chipsInPot >= other.chipsInPot)
    }

    val showingPlayers = mutableListOf<Player>()

    orderedPlayers.forEach { player ->

        // player should show the cards if no other player definitely wins with him
        if (showingPlayers.none { definitelyWins(it, player) })
            showingPlayers.add(player)
    }

    return orderedPlayers.map {
        if (it in showingPlayers)
            ShowCards(it.id)
        else
            MuckCards(it.id)
    }
}
