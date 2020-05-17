package core.handflow

import core.cards.Card
import core.flowUtils.BettingRound
import core.flowUtils.Blinds
import core.player.Player
import core.flowUtils.Positions
import core.player.getByPosition

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
    val bettingRound: BettingRound?,

    val lastLegalBet: Int,
    val minRaise: Int,
    val extraBet: Int
) {
    val pot: Int = players.sumBy { it.chipsInPot }
    val totalBet: Int = lastLegalBet + extraBet

    // Small blind position sometimes may point to an empty when players leave the table between hands.
    val smallBlindPlayer: Player? = players.getByPosition(positions.smallBlind)

    // Big blind position must always point to some player.
    val bigBlindPlayer: Player = players.getByPosition(positions.bigBlind)!!

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

            // players, blinds and positions can never be null
            players!!
            assert(players.size > 2)
            assert(players.distinctBy { it.seat } == players)

            blinds!!
            positions!!

            if (activePlayer != null)
                assert(activePlayer in players)

            if (lastAggressor != null)
                assert(lastAggressor in players)

            // default pre-flop initialization
            val communityCards = this.communityCards ?: emptyList()
            val bettingRound = this.bettingRound

            val lastLegalBet = this.lastLegalBet ?: 0
            val minRaise = this.minRaise ?: blinds.bigBlind
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
