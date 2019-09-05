package core.gameflow

import core.gameflow.handstate.HandState

/**
 * potNumber set to zero indicates, that the chips come from the main pot.
 * Each higher number correlates with the number of side pot.
 */
data class PotResult(val playerId: Int, val chips: Int, val potNumber: Int = 0)

fun resolvePot(handState: HandState): Sequence<PotResult> {
    return emptySequence()
}
