package core.handflow.hand

class HandRecord(val initState: HandState) {
    private val handHistory: MutableList<HandAction> = mutableListOf()

    fun register(action: HandAction) = handHistory.add(action)

    fun registerSequence(actions: Iterable<HandAction>) = handHistory.addAll(actions)

    fun getHandHistory() = handHistory.toList()

    fun resolveHandState() = handHistory.fold(initial = initState, operation = { state: HandState, action: HandAction ->
        if (action is ApplicableHandAction) action.apply(state) else state
    })
}
