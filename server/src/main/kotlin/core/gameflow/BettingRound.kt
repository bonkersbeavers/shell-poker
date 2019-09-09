package core.gameflow

enum class BettingRound(val roundNumber: Int) {
    PRE_FLOP(0),
    FLOP(1),
    TURN(2),
    RIVER(3)
}
