package core.gameflow.handstate

import core.cards.Card
import core.gameflow.BettingRound
import core.gameflow.Blinds
import core.gameflow.player.Player
import core.gameflow.Positions

fun HandState.updateActivePlayer(playerUpdate: Player): HandState {

    assert(activePlayer != null)

    val newPlayers = players.map {
        when (it) {
            activePlayer -> playerUpdate
            else -> it
        }
    }

    return this.rebuild(players = newPlayers, activePlayer = playerUpdate)
}

/* Helpers used to create new HandState instances based on an existing instance: */

fun HandState.toBuilder(): HandState.ImmutableBuilder = HandState.ImmutableBuilder(this)

fun HandState.rebuild(
    players: List<Player>? = this.players,
    activePlayer: Player? = this.activePlayer,
    lastAggressor: Player? = this.lastAggressor,
    blinds: Blinds? = this.blinds,
    positions: Positions? = this.positions,
    communityCards: List<Card>? = this.communityCards,
    bettingRound: BettingRound? = this.bettingRound,
    lastLegalBet: Int? = this.lastLegalBet,
    minRaise: Int? = this.minRaise,
    extraBet: Int? = this.extraBet
): HandState = this.toBuilder()
        .copy(
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
        ).build()
