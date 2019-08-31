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
class HandState private constructor(
    val players: List<Player>,
    val activePlayer: Player?,
    val lastAggressor: Player?,

    val blinds: Blinds,
    val positions: Positions,

    val communityCards: List<Card>,
    val bettingRound: BettingRound,

    val lastLegalBet: Int,
    val minRaise: Int,
    val extraBet: Int
) {
    fun toBuilder(): ImmutableBuilder = ImmutableBuilder(this)

    val pot: Int = players.sumBy { it.chipsInPot }
    val totalBet: Int = lastLegalBet + extraBet

    // Small blind position sometimes may point to an empty when players leave the table between hands.
    val smallBlindPlayer: Player? = playerAtPosition(positions.smallBlind)

    // Big blind position must always point to some player.
    val bigBlindPlayer: Player = playerAtPosition(positions.bigBlind)!!

    val playersInGame: List<Player> = players.filter { it.isInGame }
    val decisivePlayers: List<Player> = players.filter { it.isDecisive }

    fun updateActivePlayer(playerUpdate: Player): HandState {

        assert(activePlayer != null)

        val newPlayers = players.map {
            when (it) {
                activePlayer -> playerUpdate
                else -> it
            }
        }

        return this.toBuilder()
                .copy(players = newPlayers, activePlayer = playerUpdate)
                .build()
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


    data class ImmutableBuilder(
            val players: List<Player>? = null,
            val activePlayer: Player? = null,
            val lastAggressor: Player? = null,

            val blinds: Blinds? = null,
            val positions: Positions? = null,

            val communityCards: List<Card>? = null,
            val bettingRound: BettingRound? = null,

            val lastLegalBet: Int? = null,
            val minRaise: Int? = null,
            val extraBet: Int? = null
    ) {
        constructor(handState: HandState) : this(
                players = handState.players,
                activePlayer = handState.activePlayer,
                lastAggressor = handState.lastAggressor,
                blinds = handState.blinds,
                positions = handState.positions,
                communityCards = handState.communityCards,
                bettingRound = handState.bettingRound,
                lastLegalBet = handState.lastLegalBet,
                minRaise = handState.minRaise,
                extraBet = handState.extraBet
        )

        fun build(): HandState {
            players!!
            assert(players.size > 2)
            assert(players.distinctBy { it.position } == players)

            activePlayer ?: assert(activePlayer in players)
            lastAggressor ?: assert(lastAggressor in players)

            blinds!!
            positions!!

            // default pre-flop state initialization
            val communityCards = this.communityCards ?: emptyList()
            val bettingRound = this.bettingRound ?: BettingRound.PRE_FLOP
            val lastLegalBet = this.lastLegalBet ?: 0
            val minRaise = this.minRaise ?: (if (bettingRound == BettingRound.PRE_FLOP) blinds.bigBlind else 0)
            val extraBet = this.extraBet ?: 0

            return HandState(
                    players = players,
                    activePlayer = activePlayer,
                    lastAggressor = lastAggressor,
                    blinds = blinds,
                    positions = positions,
                    communityCards = communityCards,
                    bettingRound = bettingRound,
                    lastLegalBet = lastLegalBet,
                    minRaise = minRaise,
                    extraBet = extraBet
            )
        }
    }
}
