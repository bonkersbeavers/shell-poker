package core.flowUtils

import core.betting.ActionType
import core.handflow.HandState

fun postBlindsAndAnte(
    stateBuilder: HandState.ImmutableBuilder,
    newPlayersIds: List<Int> = emptyList()
): HandState.ImmutableBuilder {

    val players = stateBuilder.players!!
    val blinds = stateBuilder.blinds!!
    val positions = stateBuilder.positions!!

    val playersWithAnte = players.map { player ->
        when {
            player.stack < blinds.ante -> player.copy(chipsInPot = player.stack, stack = 0)
            else -> player.copy(chipsInPot = blinds.ante, stack = player.stack - blinds.ante)
        }
    }

    val playersWithAnteAndBlinds = playersWithAnte.map { player ->
        val chipsToPost = when {
            player.id in newPlayersIds -> blinds.bigBlind
            player.position == positions.bigBlind -> blinds.bigBlind
            player.position == positions.smallBlind -> blinds.smallBlind
            else -> 0
        }

        val playerAfterPost = when {
            player.stack < chipsToPost -> player.copy(bet = player.stack, stack = 0)
            else -> player.copy(bet = chipsToPost, stack = player.stack - chipsToPost)
        }

        val lastAction = if (chipsToPost > 0) ActionType.POST else null
        playerAfterPost.copy(lastAction = lastAction)
    }

    return stateBuilder.copy(players = playersWithAnteAndBlinds)
}

data class Blinds(val smallBlind: Int, val bigBlind: Int, val ante: Int = 0)
