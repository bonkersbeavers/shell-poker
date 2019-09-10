package core.gameflow

enum class BettingRound {
    PRE_FLOP,
    FLOP,
    TURN,
    RIVER;


    fun next(): BettingRound {
        return when(this) {
            PRE_FLOP -> FLOP
            FLOP -> TURN
            TURN -> RIVER
            else -> TODO()
        }
    }
}
