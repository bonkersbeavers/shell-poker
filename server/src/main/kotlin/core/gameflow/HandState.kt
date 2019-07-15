package core.gameflow

import core.Card

/**
 * activePlayer set to null indicates that there is no further action possible in given betting round
 *
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
    val activePlayer: Player?,
    val communityCards: List<Card> = emptyList(),
    val lastAggressor: Player? = null,
    val currentBet: Int = 0,
    val minRaise: Int = blinds.bigBlind
) {
    init {
        assert(players.distinctBy { it.position }.size == players.size)

        if (activePlayer != null)
            assert(players.find { it == activePlayer } != null)
    }

    val pot: Int = players.sumBy { it.chipsInPot }

    fun updateActivePlayer(playerUpdate: Player): HandState {

        assert(activePlayer != null)

        val newPlayers = players.map {
            when (it) {
                activePlayer -> playerUpdate
                else -> it
            }
        }

        return this.copy(players = newPlayers, activePlayer = playerUpdate)
    }
}
