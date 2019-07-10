package core.gameflow

import core.Card

data class Player(
    val position: Int,
    val stack: Int,
    val holeCards: List<Card> = listOf(),
    val chipsInPot: Int = 0,
    val currentBet: Int = 0,
    val folded: Boolean = false
) {
    fun withCards(newCards: List<Card>): Player {
        return this.copy(holeCards = newCards)
    }

    fun afterFold(): Player {
        return this.copy(folded = true)
    }

    fun afterAllIn(): Player {
        return this.copy(currentBet = this.currentBet + this.stack, stack = 0)
    }

    fun withBet(betSize: Int): Player {
        if (betSize > this.stack)
            throw NotEnoughChipsException("Bet size of $betSize larger than stack size ${this.stack}")

        return this.copy(currentBet = this.currentBet + betSize, stack = this.stack - betSize)
    }

    fun isAllIn(): Boolean {
        return stack == 0 && currentBet != 0 && !folded
    }
}
