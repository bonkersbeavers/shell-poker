package core.gameflow

import core.Card

/**
 * lastAggressor parameter should be used to determine when the action in a betting round should end,
 * as well as to resolve the order in which players show their cards during the showdown. That's why
 * every time a betting rounds starts with a check, the first player that checks should be marked
 * as the last aggressor (even though no raise was made). The same rule applies when UTG calls the big blind
 * in pre-flop betting round.
 */
data class HandState(
    val players: List<Player>,
    val blinds: Blinds,
    val buttonPosition: Int,
    val activePlayer: Player,
    val communityCards: List<Card> = emptyList(),
    val lastAggressor: Player? = null,
    val currentBet: Int = 0,
    val minRaise: Int = blinds.bigBlind
) {
    init {
        assert(players.distinctBy { it.position }.size == players.size)
        assert(players.find { it == activePlayer } != null)
    }

    val pot: Int = players.sumBy { it.chipsInPot }

    fun updateActivePlayer(playerUpdate: Player): HandState {

        val newPlayers = players.map {
            when (it) {
                activePlayer -> playerUpdate
                else -> it
            }
        }

        return this.copy(players = newPlayers, activePlayer = playerUpdate)
    }
}
