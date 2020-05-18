package core.hand.helpers

import core.hand.player.betting.ActionType
import core.hand.ApplicableHandAction
import core.hand.HandAction
import core.hand.HandState
import core.hand.pot.Pot

object CollectBets : HandAction(), ApplicableHandAction {
    override fun apply(handState: HandState): HandState {

        val seatToBetSize = handState.playersStates.map { it.seat to it.currentBet }.toMap().toMutableMap()
        val mutablePotList = handState.pots.toMutableList()

        // update pots
        while (seatToBetSize.values.max()!! > 0) {

            // determine next pot
            val nextPotContributorsMap = seatToBetSize.filterValues { it > 0 }
            val nextPotSeats = nextPotContributorsMap.keys
            val nextPotBet = nextPotContributorsMap.values.min()!!
            val nextPotSize = nextPotBet * nextPotSeats.count()

            val mergePots = if (mutablePotList.isEmpty()) false
                else nextPotSeats.containsAll(mutablePotList.last().playersSeats)

            // add pot to current pots list
            if (mergePots) {
                val previousPot = mutablePotList.last()
                val mergedPot = previousPot.copy(size = previousPot.size + nextPotSize)
                mutablePotList.removeAt(mutablePotList.lastIndex)
                mutablePotList.add(mergedPot)
            } else {
                val nextPot = Pot(size = nextPotSize, playersSeats = nextPotSeats, potNumber = mutablePotList.count())
                mutablePotList.add(nextPot)
            }

            // subtract chips used in new pot from players' bets
            for (seat in nextPotSeats)
                seatToBetSize.replace(seat, seatToBetSize[seat]!! - nextPotBet)
        }

        // clear bets
        val newPlayers = handState.playersStates.map {
            it.copy(
                    currentActionType = if (it.currentActionType == ActionType.FOLD) ActionType.FOLD else null,
                    currentBet = 0
            )
        }

        return handState.copy(
                playersStates = newPlayers,
                pots = mutablePotList.toList(),
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = handState.blinds.bigBlind
        )
    }
}
