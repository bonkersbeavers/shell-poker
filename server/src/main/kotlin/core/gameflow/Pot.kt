package core.gameflow

import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import core.gameflow.player.ordered

/**
 * potNumber set to zero indicates, that the chips come from the main pot.
 * Each higher number correlates with the number of side pot.
 */
data class Pot(val size: Int, val players: Set<Player>, val potNumber: Int = 0)
data class PotResult(val chips: Int, val playerId: Int, val potNumber: Int = 0)

fun HandState.pots(): List<Pot> {

    class RemainingChipsInPot(val player: Player, var chips: Int) {

        fun take(amount: Int): Int {
            return when {
                amount > this.chips -> {
                    val taken = this.chips
                    this.chips = 0
                    taken
                }

                else -> {
                    this.chips -= amount
                    amount
                }
            }
        }
    }

    var remainingChipsList = this.players.map { RemainingChipsInPot(it, it.chipsInPot) }
    var pots = emptyList<Pot>()

    while (remainingChipsList.isNotEmpty()) {
        // Searches for the player that is still in game but has committed the least chips
        // compared to other players left. This player's remaining chips will dictate
        // how many chips from each player must go into the currently created side pot.
        val minRemainingChips = remainingChipsList.filter { it.player.isInGame }.map { it.chips }.min()!!

        // Takes previously calculated amount of chips from each remaining player's stack.
        val nextPotSize = remainingChipsList.sumBy { it.take(minRemainingChips) }
        val nextPotPlayerIds = remainingChipsList.filter { it.player.isInGame }.map { it.player }.toSet()

        val nextPot = Pot(nextPotSize, nextPotPlayerIds, pots.size)
        pots += nextPot

        remainingChipsList = remainingChipsList.filter { it.chips > 0 }
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
        val extraChips = pot.size % potWinnersSet.size

        val results = orderedPotWinners.mapIndexed { index, player ->
            val chips = chipsWon + (if (index < extraChips) 1 else 0)
            PotResult(chips, player.id, pot.potNumber)
        }

        results
    }

    return potResults.reduce { acc, list -> acc + list }
}
