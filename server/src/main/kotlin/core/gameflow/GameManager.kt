package core.gameflow

import core.adapters.PlayerRouter
import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.player.*

class GameManager(handState: HandState) {

    private val dealer: Dealer = Dealer()
    private val playerRouter = PlayerRouter()

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
        var handState = startingHandState

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

        while( (handState.bettingRound != BettingRound.RIVER) or handState.players.decisive().isNotEmpty() ) {
            handState = prepareForNextBettingRound(handState)
            playerRouter.broadcast(handState)
            handState = bettingRound(handState)
        }

        if(handState.players.decisive().isEmpty()) {
            //deal missing cards
        }


        var showdownOrder = resolveShowdown(handState)

        playerRouter.broadcast(showdownOrder)

        handState = distributePot(handState)

        return handState
    }

    //TODO : Test
    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while(handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = playerRouter.getAction(handState.activePlayer!!.id)
                actionValidation = action.validate(handState)
//                playerRouter.sendPrivateUpdate(handState.activePlayer!!.id, actionValidation)
            }
            while(actionValidation != ValidAction)

//            playerRouter.broadcastPlayerAction(handState.activePlayer!!.id, action)
            handState = action.apply(handState)
//            playerRouter.broadcast(handState)
        }

        return handState
    }

    //TODO : Test
    private fun initializeBettingRound(handState: HandState): HandState {
        val newHandState = when(handState.bettingRound) {
            BettingRound.PRE_FLOP ->
                postBlinds(handState.rebuild(activePlayer = handState.smallBlindPlayer))
            else ->
                handState.rebuild(activePlayer = handState.players.nextDecisive(handState.positions.button))
        }

        return dealer.deal(newHandState)
    }

    //TODO : Completely change xd
    private fun postBlinds(handState: HandState): HandState {
        return Post(handState.blinds.bigBlind).apply(
                Post(handState.blinds.smallBlind).apply(handState)
        )
    }

    //TODO : Test
    private fun finalizeBettingRound(handState: HandState): HandState {
        return handState.rebuild(
                players = handState.players.map { it.moveBetToPot() },
                bettingRound = handState.bettingRound + 1, //would be nice to implement
                lastAggressor = null,
                lastLegalBet = 0,
                minRaise = handState.blinds.bigBlind,
                extraBet = 0
        )
    }
}
