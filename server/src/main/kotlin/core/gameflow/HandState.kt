package core.gameflow

import core.cards.Card

/**
 * activePlayer set to null indicates that there is no further action possible in given betting round.
 *
 * lastAggressor parameter should be used to determine when the action in a betting round should end,
 * as well as to resolve the order in which players show their cards during the showdown. If set to null,
 * it indicates that no aggressive action was taken yet.
 *
 * extraBet is used when there is an incomplete raise all-in. It is used to store the additional bet value.
 * It should be reset to 0 every time a legal bet / raise is made.
 */
data class HandState(
    val players: List<Player>,
    val blinds: Blinds,
    val positions: Positions,
    val activePlayer: Player? = null,
    val communityCards: List<Card> = emptyList(),
    val bettingRound: BettingRound = BettingRound.PRE_FLOP,
    val lastAggressor: Player? = null,
    val lastLegalBet: Int = 0,
    val minRaise: Int = blinds.bigBlind,
    val extraBet: Int = 0
) {
    init {
        assert(players.size >= 2)
        assert(players.distinctBy { it.position }.size == players.size)

        if (activePlayer != null)
            assert(players.find { it == activePlayer } != null)

        if (lastAggressor != null)
            assert(players.find { it == lastAggressor } != null)
    }

    val pot: Int = players.sumBy { it.chipsInPot }
    val totalBet: Int = lastLegalBet + extraBet

    val playersInGame: List<Player> = players.filter { it.isInGame }
    val decisivePlayers: List<Player> = players.filter { it.isDecisive }

    // Small blind position may point to an empty seat in some scenarios
    // when players leave the table between hands.
    val smallBlindPlayer: Player? = playerAtPosition(positions.smallBlind)

    // Big blind position must always point to some player.
    val bigBlindPlayer: Player = playerAtPosition(positions.bigBlind)!!

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

    fun orderedPlayers(startingPosition: Int): List<Player> {
        val sortedPlayers = players.sortedBy { it.position }
        val begin = sortedPlayers.filter { it.position >= startingPosition }
        val end = sortedPlayers.filter { it.position < startingPosition }
        return begin + end
    }

    fun playerAtPosition(position: Int): Player? = players.find { it.position == position }

    fun nextPlayer(position: Int): Player = orderedPlayers(position + 1).first()
    fun nextPlayer(player: Player): Player = nextPlayer(player.position)

    fun nextDecisivePlayer(position: Int): Player? = orderedPlayers(position + 1).find { it.isDecisive }
    fun nextDecisivePlayer(player: Player): Player? = nextDecisivePlayer(player.position)
}
