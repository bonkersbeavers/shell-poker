package core.gameflow

import core.adapters.IPlayerAdapter
import core.adapters.PlayerRouter
import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.handstate.toBuilder
import core.gameflow.player.*

class GameManager(private val initialHandState: HandState, val playerRouter: PlayerRouter, val roomSettings: RoomSettings) {

    private val dealer: Dealer = Dealer()

    fun run() {
        var handCounter = 0
        var state = initialHandState
        while (true) {
            println("Hand number $handCounter")
            state = cleanState(state)
            state = playHand(state)
        }
    }

    private fun cleanState(state: HandState): HandState {
        return HandState.ImmutableBuilder(
                players = state.players,
                blinds = state.blinds,
                positions = state.positions
        ).build()
    }

    /**
     * input: clean initial handState with default pre-flop initialization and:
     * players (by UPDATE PLAYERS IN PLAYER rOUTER OR WAITING ROOM)
     * active_player == null
     * last aggressor = null
     * blinds(by GAME SETTINGS)
     * positions(by Game SETTINGS)
     *
     */
    private fun playHand(startingHandState: HandState): HandState {
        val initialBuilder = startingHandState.toBuilder()
        var handState = shiftPositions(initialBuilder, roomSettings).build()
        this.dealer.shuffle()



//        //PRE_GAME
//        handState = prepareForNextBettingRound(handState) //should post blinds here and deal hole cards
//
//        //PRE_FLOP
//        handState = bettingRound(handState)
//        handState = prepareForNextBettingRound(handState) //should should deal flop and so on
//
//        //FLOP
//        handState = bettingRound(handState)
//        handState = prepareForNextBettingRound(handState) //should should deal turn and so on
//
//        //TURN
//        handState = bettingRound(handState)
//        handState = prepareForNextBettingRound(handState) //should should deal river and so on
//
//        //RIVER
//        handState = bettingRound(handState)

        while ((handState.bettingRound != BettingRound.RIVER) and (handState.players.decisive().size > 1)) {
            handState = initializeBettingRound(handState)
            playerRouter.broadcastHandState(handState)
            handState = bettingRound(handState)
            handState = finalizeBettingRound(handState)
            playerRouter.broadcastHandState(handState)
        }

        val showdownOrder = resolveShowdown(handState)
        println("XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
        println(showdownOrder)
        println("XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
        // TODO: broadcast showdown

        if (handState.players.inGame().size > 1) {
            while (handState.bettingRound != BettingRound.RIVER)
                handState = this.dealer.deal(handState)
        }

        handState = distributePot(handState)
        playerRouter.broadcastHandState(handState)

        return handState
    }

    // TODO : Test
    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while (handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = playerRouter.requestAction(handState.activePlayer!!.id)
                actionValidation = action.validate(handState)
                playerRouter.sendActionValidation(handState.activePlayer!!.id, actionValidation)
            }
            while (actionValidation != ValidAction)

//            playerRouter.broadcastPlayerAction(handState.activePlayer!!.id, action)
            handState = action.apply(handState)
            playerRouter.broadcastHandState(handState)
        }

        return handState
    }

    // TODO : Test
    private fun initializeBettingRound(handState: HandState): HandState {
        val newHandState = when (handState.bettingRound) {
            BettingRound.PRE_FLOP ->
                postBlinds(handState.rebuild(activePlayer = handState.smallBlindPlayer))
            else ->
                handState.rebuild(activePlayer = handState.players.nextDecisive(handState.positions.button))
        }

        return dealer.deal(newHandState)
    }

    // TODO : Completely change xd
    private fun postBlinds(handState: HandState): HandState {
        return Post(handState.blinds.bigBlind).apply(
                Post(handState.blinds.smallBlind).apply(handState)
        )
    }

    // TODO : Test
    private fun finalizeBettingRound(handState: HandState): HandState {
        return handState.rebuild(
                players = handState.players.map { it.moveBetToPot() },
                bettingRound = handState.bettingRound.next(), // would be nice to implement
                lastAggressor = null,
                lastLegalBet = 0,
                minRaise = handState.blinds.bigBlind,
                extraBet = 0
        )
    }
}
