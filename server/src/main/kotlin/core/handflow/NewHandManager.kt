package core.handflow

import core.betting.BettingAction
import core.flowUtils.Blinds
import core.flowUtils.PotResult
import core.RoomSettings
import core.flowUtils.ShowdownAction
import core.dealer.Dealer
import core.player.PlayerStatus

class NewHandManager(settings: RoomSettings) {
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

    fun nextActionType(): HandFlowActionType? {
        TODO()
        /**
         * determines next action's type (player's action of game action)
         *
         * returns null if there is no further action to be taken (the hand is over)
         */
    }

    fun setNextPlayerAction(action: BettingAction) {
        TODO()
        /**
         * sets next player's action
         * throws HandFlowException if next action is not a player's action
         * throws HandFlowException if the action is invalid in current hand state
         */
    }

    fun takeAction() {
        TODO()
        /**
         * proceeds with proper action (whether player's action or game action) and updates hand state
         * throws HandFlowException if there is no further action possible
         * throws HandFlowException if next action should be player's action and nextPlayerAction is not set
         */
    }

    fun playersInteractionIsOver(): Boolean {
        TODO()
        /**
         * checks if player's interaction is no longer possible in current hand
         */
    }

    fun handIsOver(): Boolean {
        TODO()
        /**
         * checks if no further action is possible in current hand
         */
    }

    fun getShowdownResults(): List<ShowdownAction> {
        TODO()
        /**
         * returns proper showdown order
         *
         * throws HandFlowException if the showdown cannot be resolved (e.g. players can still take valid actions)
         */
    }

    fun getPotResults(): List<PotResult> {
        TODO()
        /**
         * returns properly resolved pot
         *
         * throws HandFlowException if the action in current hand is not over
         */
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
