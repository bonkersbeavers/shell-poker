package core.handflow.dealer

enum class BettingRound {
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER;

    fun next(): BettingRound {
        return when (this) {
            PRE_FLOP -> FLOP
            FLOP -> TURN
            TURN -> RIVER
            RIVER -> throw NoSuchElementException("There is no betting round after river")
        }
    }
}
