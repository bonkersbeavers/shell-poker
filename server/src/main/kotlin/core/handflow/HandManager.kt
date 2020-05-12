package core.handflow

import core.betting.BettingAction
import core.RoomSettings
import core.betting.InvalidAction
import core.dealer.Dealer
import core.flowUtils.*
import core.player.*

class HandManager(val settings: RoomSettings) {
    private val dealer = Dealer()
    var currentState: HandState? = null
        private set
    private var nextPlayerAction: BettingAction? = null

    fun startNewHand(
            playersStatuses: List<PlayerStatus>,
            newPlayersIds: Collection<Int>,
            blinds: Blinds,
            randomPositions: Boolean = false) {

        dealer.shuffle()

        val players = playersStatuses.map { Player(seat = it.seat, stack = it.stack) }

        var stateBuilder = HandState.ImmutableBuilder(
                players = players,
                blinds = blinds
        )

        stateBuilder = when (randomPositions) {
            true -> setRandomPositions(stateBuilder, settings)
            false -> shiftPositions(stateBuilder.copy(positions = currentState!!.positions), settings)
        }

        stateBuilder = postBlindsAndAnte(stateBuilder, newPlayersIds)

        var state = stateBuilder.build()
        state = dealer.deal(state)

        this.currentState = state
        initializeActivePlayer()
    }

    fun getNextActionType(): HandFlowActionType? {
        return when {
            currentState == null -> throw HandFlowException("hand state has not been initialized")
            currentState!!.activePlayer != null -> HandFlowActionType.PLAYER_ACTION
            currentState!!.bettingRound != BettingRound.RIVER -> HandFlowActionType.GAME_ACTION
            else -> null
        }
    }

    fun setNextPlayerAction(action: BettingAction) {
        if (getNextActionType() != HandFlowActionType.PLAYER_ACTION)
            throw HandFlowException("cannot set player's action when next action is not taken by player")

        val actionValidation = action.validate(currentState!!)
        if (actionValidation is InvalidAction)
            throw HandFlowException("attempt to set invalid player's action, reason is: ${actionValidation.reason}")

        this.nextPlayerAction = action
    }

    fun takeAction() {
        if (handIsOver)
            throw HandFlowException("no further action possible in hand")

        if (getNextActionType() == HandFlowActionType.PLAYER_ACTION && nextPlayerAction == null)
            throw HandFlowException("next player's action is not set")

        when (getNextActionType()) {
            HandFlowActionType.PLAYER_ACTION -> currentState = nextPlayerAction!!.apply(currentState!!)
            HandFlowActionType.GAME_ACTION -> {
                currentState = dealer.deal(currentState!!)
                if (playersInteractionIsOver.not())
                    initializeActivePlayer()
            }
        }
    }

    val playersInteractionIsOver: Boolean = currentState?.let {
        handIsOver or (it.players.decisive().count() < 2)
    } ?: false

    val handIsOver: Boolean = currentState?.let {
        (it.bettingRound == BettingRound.RIVER && it.activePlayer == null) or (it.players.inGame().count() < 2)
    } ?: false

    fun getShowdownResults(): List<ShowdownAction> {
        if (playersInteractionIsOver.not())
            throw HandFlowException("cannot resolve showdown while some players can still take actions")

        return resolveShowdown(currentState!!)
    }

    fun getPotResults(): List<PotResult> {
        if (handIsOver.not())
            throw HandFlowException("cannot resolve pot while hand is not over")

        return resolvePot(currentState!!)
    }

    fun finalizeHand() {
        if (handIsOver.not())
            throw HandFlowException("cannot finalize hand while hand is not over")

        this.currentState = distributePot(this.currentState!!)
    }

    private fun initializeActivePlayer() {
        val state = currentState!!
        val firstActivePlayer = when (state.bettingRound) {
            BettingRound.PRE_FLOP -> state.players.nextDecisive(state.positions.bigBlind)
            else -> state.players.nextDecisive(state.positions.button)
        }

        this.currentState = currentState!!.rebuild(activePlayer = firstActivePlayer)
    }
}
