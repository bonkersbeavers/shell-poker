package core.gameflow

sealed class ShowdownAction(open val playerId: Int)

data class ShowCards(override val playerId: Int) : ShowdownAction(playerId)
data class MuckCards(override val playerId: Int) : ShowdownAction(playerId)

fun resolveShowdown(handState: HandState): List<ShowdownAction> {

    val orderedPlayers = when {
        handState.lastAggressor == null -> handState.orderedPlayers(handState.buttonPosition + 1)
        else -> handState.orderedPlayers(handState.lastAggressor.position)
    }.filter { it.isInGame }

    // Player can muck his cards if he is the only one left in the hand.
    if (orderedPlayers.size == 1)
        return listOf(MuckCards(orderedPlayers.first().id))

    // All players must show their cards if there are more streets to be dealt.
    // This is the case when players go all in before the river and no further betting action is possible.
    if (handState.bettingRound != BettingRound.RIVER)
        return orderedPlayers.map { ShowCards(it.id) }

    // One player 'definitely wins' with another if he has a better hand and he has put
    // at least the same amount of chips into the pot. If one player has a better hand than the other one
    // but his chips commitment is lower, then the other player may still win some chips from side pots.
    fun definitelyWins(player: Player, other: Player): Boolean {
        val communityCards = handState.communityCards
        return (player.hand(communityCards) > other.hand(communityCards)) and (player.chipsInPot >= other.chipsInPot)
    }

    val showingPlayers = mutableListOf<Player>()

    orderedPlayers.forEach { player ->

        // A player should show the cards if no other showing player definitely wins with him.
        // If so, he is added to the showing pool, and all the following players that are about to
        // take a showdown action have to take him into account as well.
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
