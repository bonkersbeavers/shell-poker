package core.gameflow

import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.player.Player
import core.gameflow.player.ordered
import kotlin.math.min

/**
 * potNumber set to zero indicates, that the chips come from the main pot.
 * Each higher number correlates with the number of side pot.
 */
data class Pot(val size: Int, val players: Set<Player>, val potNumber: Int = 0)
data class PotResult(val chips: Int, val playerId: Int, val potNumber: Int = 0)

fun HandState.pots(): List<Pot> {

    var lastPotBetToCover = 0
    var pots = emptyList<Pot>()

    // Continues while there is any side pot left to be created.
    while (players.any { it.chipsInPot > lastPotBetToCover }) {

        // Finds all players that participate in the next pot creation.
        val nextPotCreators = players.filter { it.chipsInPot > lastPotBetToCover }
        val nextPotPlayers = nextPotCreators.filter { it.isInGame }

        // Obligatory bet for the currently created pot is dictated by the smallest
        // of the player that is still in game and participates in the pot creation.
        // If player's committed chips amount is higher than this limit,
        // then the excess will be used to create another side pot.
        val nextPotBetToCover = nextPotCreators.filter { it.isInGame }.map { it.chipsInPot }.min()!!

        // Actual chips amount from each player that will form the next pot.
        // If a player is in game, his chunk will be equal to the difference between last and next pot chips limits.
        // The only case when player's chunk is lower is when he has folded.
        val nextPotChunks = nextPotCreators.map { min(it.chipsInPot, nextPotBetToCover) - lastPotBetToCover }

        pots += Pot(
                size = nextPotChunks.sum(),
                players = nextPotPlayers.toSet(),
                potNumber = pots.size
        )

        lastPotBetToCover = nextPotBetToCover
    }

    return pots
}

fun resolvePot(handState: HandState): List<PotResult> {

    // Pots should be resolved from the last one to the main pot
    val orderedPots = handState.pots().reversed()

    val potResults = orderedPots.map { pot ->
        val bestHand = pot.players.map { it.hand(handState.communityCards) }.max()!!
        val potWinnersSet = pot.players.filter { it.hand(handState.communityCards).compareTo(bestHand) == 0 }.toSet()

        // if there are multiple winners, they should be awarded in betting order
        val orderedPotWinners = handState.players
                .ordered(handState.positions.button + 1)
                .filter { it in potWinnersSet }

        val chipsWon = pot.size / potWinnersSet.size

        // If chips cannot be split equally, first players after the button are awarded with an extra chip
        val extraChips = pot.size % potWinnersSet.size

        val results = orderedPotWinners.mapIndexed { index, player ->
            val chips = chipsWon + (if (index < extraChips) 1 else 0)
            PotResult(chips, player.id, pot.potNumber)
        }

        results
    }

    return potResults.reduce { acc, list -> acc + list }
}

fun distributePot(handState: HandState): HandState {
    val potResults = resolvePot(handState)

    val totalWinnings = HashMap<Int, Int>()
    potResults.forEach {
        val currentWinnings = totalWinnings.getOrDefault(it.playerId, 0)
        totalWinnings[it.playerId] = currentWinnings + it.chips
    }

    val newPlayers = handState.players.map { player ->
        val winnings = totalWinnings.getOrDefault(player.id, 0)
        val newStack = player.stack + winnings
        player.copy(
                chipsInPot = 0,
                stack = newStack
        )
    }

    return handState.rebuild(newPlayers)
}
