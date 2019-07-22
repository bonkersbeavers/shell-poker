package core.gameflow

import core.Card

data class Player(
    val position: Int,
    val stack: Int,
    val holeCards: List<Card> = emptyList(),
    val chipsInPot: Int = 0,
    val currentBet: Int = 0,
    val folded: Boolean = false,
    val id: Int = position
) {
    fun withCards(newCards: List<Card>): Player {
        return this.copy(holeCards = newCards)
    }

    fun afterFold(): Player {
        return this.copy(folded = true)
    }

    fun afterAllIn(): Player {
        return this.copy(currentBet = currentBet + stack, stack = 0)
    }

    fun afterRaise(betSize: Int): Player {
        if (betSize > stack)
            throw NotEnoughChipsException("Bet size of $betSize larger than stack size $stack")

        return this.copy(currentBet = currentBet + betSize, stack = stack - betSize)
    }

    fun afterCall(betSize: Int): Player {
        val amountToCall = betSize - currentBet

        return when (amountToCall > stack) {
            true -> afterAllIn()
            else -> afterRaise(amountToCall)
        }
    }

    fun isAllIn(): Boolean {
        return stack == 0 && currentBet != 0 && !folded
    }

    fun isActive(): Boolean {
        return !(isAllIn() or folded)
    }
}
