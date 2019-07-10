package core.gameflow

import core.Card

data class Player(val position: Int,
                  val stack: Int,
                  val holeCards: List<Card>,
                  val chipsInPot: Int,
                  val currentBet: Int,
                  val folded: Boolean = false) {

    fun withCards(newCards: List<Card>): Player {
        return this.copy(holeCards = newCards)
    }

    fun afterFold(): Player {
        return this.copy(folded = true)
    }

    fun afterAllIn(): Player {
        return this.copy(currentBet = this.chipsInPot,
                         chipsInPot = 0)
    }

    fun withBet(betSize: Int): Player {
        return this.copy(currentBet = this.currentBet + betSize,
                         chipsInPot = this.chipsInPot - betSize)
    }

    fun isAllIn(): Boolean {
        return chipsInPot == 0 && currentBet != 0 && !folded
    }
}