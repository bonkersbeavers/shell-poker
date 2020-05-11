package core.handflow

import core.betting.BettingAction
import core.RoomSettings
import core.betting.InvalidAction
import core.dealer.Dealer
import core.flowUtils.*
import core.player.PlayerStatus
import core.player.decisive
import core.player.inGame

class HandManager(settings: RoomSettings) {
    private val dealer = Dealer()
    var currentState: HandState? = null
        private set
    private var nextPlayerAction: BettingAction? = null

    fun startNewHand(
            players: List<PlayerStatus>,
            newPlayersIds: Collection<Int>,
            blinds: Blinds,
            randomPositions: Boolean = false) {
        TODO()
        /**
         * initializes hand state by setting correct positions (random or shifted from previous state),
         * applying all blinds / ante posts to players and dealing hole cards
         */
    }

    val nextActionType: HandFlowActionType? = when {
        currentState == null -> throw HandFlowException("hand state has not been initialized")
        currentState!!.activePlayer == null -> HandFlowActionType.PLAYER_ACTION
        currentState!!.bettingRound != BettingRound.RIVER -> HandFlowActionType.GAME_ACTION
        else -> null
    }

    fun setNextPlayerAction(action: BettingAction) {
        if (nextActionType != HandFlowActionType.PLAYER_ACTION)
            throw HandFlowException("cannot set player's action when next action is not taken by player")

        val actionValidation = action.validate(currentState!!)
        if (actionValidation is InvalidAction)
            throw HandFlowException("attempt to set invalid player's action, reason is: ${actionValidation.reason}")

        this.nextPlayerAction = action
    }

    fun takeAction() {
        if (handIsOver)
            throw HandFlowException("no further action possible in hand")

        if (nextActionType == HandFlowActionType.PLAYER_ACTION && nextPlayerAction == null)
            throw HandFlowException("next player's action is not set")

        when (nextActionType) {
            HandFlowActionType.PLAYER_ACTION -> currentState = nextPlayerAction!!.apply(currentState!!)
            HandFlowActionType.GAME_ACTION -> currentState = dealer.deal(currentState!!)
        }
    }

    val playersInteractionIsOver: Boolean = currentState?.let {
        handIsOver or (it.players.decisive().count() < 2)
    } ?: false

    val handIsOver: Boolean = currentState?.let {
        (it.bettingRound == BettingRound.RIVER && it.activePlayer == null) or (it.players.inGame().count() < 2)
    } ?: false

    val showdownResults: List<ShowdownAction> = run {
        if (playersInteractionIsOver.not())
            throw HandFlowException("cannot resolve showdown while some players can still take actions")

        resolveShowdown(currentState!!)
    }

    val potResults: List<PotResult> = run {
        if (handIsOver.not())
            throw HandFlowException("cannot resolve pot while hand is not over")

        resolvePot(currentState!!)
    }

    fun finalizeHand() {
        TODO()
        /**
         * udpates hand state by applying pot results to the players
         *
         * throws HandFlowException if the action in current hand is not over
         */
    }
}
