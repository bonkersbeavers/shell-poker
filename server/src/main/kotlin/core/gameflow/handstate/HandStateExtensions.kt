package core.gameflow.handstate

import core.cards.Card
import core.gameflow.BettingRound
import core.gameflow.Blinds
import core.gameflow.Player
import core.gameflow.Positions

/* Utility methods for manipulating players: */

fun HandState.orderedPlayers(startingPosition: Int): List<Player> {
    val sortedPlayers = players.sortedBy { it.position }
    val begin = sortedPlayers.filter { it.position >= startingPosition }
    val end = sortedPlayers.filter { it.position < startingPosition }
    return begin + end
}

fun HandState.nextPlayer(position: Int): Player = orderedPlayers(position + 1).first()
fun HandState.nextPlayer(player: Player): Player = nextPlayer(player.position)

fun HandState.nextDecisivePlayer(position: Int): Player? =
        orderedPlayers(position + 1).find { it.isDecisive }
fun HandState.nextDecisivePlayer(player: Player): Player? = nextDecisivePlayer(player.position)

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
