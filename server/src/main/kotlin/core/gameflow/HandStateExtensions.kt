package core.gameflow

import core.cards.Card

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
    players: List<Player>? = null,
    activePlayer: Player? = null,
    lastAggressor: Player? = null,
    blinds: Blinds? = null,
    positions: Positions? = null,
    communityCards: List<Card>? = null,
    bettingRound: BettingRound? = null,
    lastLegalBet: Int? = null,
    minRaise: Int? = null,
    extraBet: Int? = null
): HandState = this.toBuilder()
        .copy(
                players = players ?: this.players,
                activePlayer = activePlayer ?: this.activePlayer,
                lastAggressor = lastAggressor ?: this.lastAggressor,
                blinds = blinds ?: this.blinds,
                positions = positions ?: this.positions,
                communityCards = communityCards ?: this.communityCards,
                bettingRound = bettingRound ?: this.bettingRound,
                lastLegalBet = lastLegalBet ?: this.lastLegalBet,
                minRaise = minRaise ?: this.minRaise,
                extraBet = extraBet ?: this.extraBet
        ).build()
