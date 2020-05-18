package core.hand.pot

import core.hand.HandState
import core.hand.Player
import core.hand.orderedBySeats

fun getPotActionsSequence(handState: HandState): List<PotAction> {
    val orderedPlayers = handState.players.orderedBySeats(handState.positions.button + 1)
    val potActionSequence = mutableListOf<PotAction>()

    // pots are resolved from last to first
    for (pot in handState.pots.reversed()) {

        // TODO: implement chips excess returning

        val potPlayers = orderedPlayers.filter { it.seat in pot.playersSeats }
        val bestHand = potPlayers.map { it.makeHand(handState.communityCards) }.max()!!

        val potWinners = potPlayers.filter { it.makeHand(handState.communityCards).compareTo(bestHand) == 0 }
        val chipsWon = pot.size / potWinners.count()

        // TODO: add odd chips distribution
        for (winner in potWinners) {
            val awardAction = AwardChips(chips = chipsWon, playerSeat = winner.seat, potNumber = pot.potNumber)
            potActionSequence.add(awardAction)
        }
    }

    return potActionSequence.toList()
}

fun resolvePots(players: List<Player>, seatToPotContribution: Map<Int, Int>): List<Pot> {
    if (seatToPotContribution.isEmpty())
        return emptyList()

    val mutablePotContributionMap = seatToPotContribution.toMutableMap()
    val playersInGameSeats = players.filter { it.isInGame }.map { it.seat }.toSet()

    val mutablePotsList = mutableListOf<Pot>()

    while (mutablePotContributionMap.values.max()!! > 0) {

        val nextPotContributorsMap = mutablePotContributionMap.filterValues { it > 0 }
        val nextPotInGameContributorsSeats = nextPotContributorsMap.keys.intersect(playersInGameSeats)

        val nextPotBet = nextPotContributorsMap.values.min()!!
        val nextPotSize = nextPotBet * nextPotContributorsMap.count()

        // new pot should be merged with previous pot if same players are playing for it
        val mergePots = if (mutablePotsList.isEmpty())
            false
        else
            nextPotInGameContributorsSeats == mutablePotsList.last().playersSeats

        // add pot to current pots list
        if (mergePots) {
            val previousPot = mutablePotsList.last()
            val mergedPot = previousPot.copy(size = previousPot.size + nextPotSize)
            mutablePotsList.removeAt(mutablePotsList.lastIndex)
            mutablePotsList.add(mergedPot)
        } else {
            val nextPot = Pot(
                    size = nextPotSize,
                    playersSeats = nextPotInGameContributorsSeats,
                    potNumber = mutablePotsList.count()
            )
            mutablePotsList.add(nextPot)
        }

        // subtract chips used in new pot from players' bets
        for (seat in nextPotContributorsMap.keys)
            mutablePotContributionMap.replace(seat, mutablePotContributionMap[seat]!! - nextPotBet)
    }

    return mutablePotsList.toList()
}
