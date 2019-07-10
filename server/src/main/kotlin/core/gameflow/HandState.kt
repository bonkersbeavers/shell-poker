package core.gameflow

import core.Card

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
