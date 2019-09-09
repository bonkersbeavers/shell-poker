package core.gameflow

import core.adapters.PlayerRouter
import core.bettinground.*
import core.gameflow.handstate.HandState
import core.gameflow.player.decisive

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

    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while(handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = playerRouter.getAction(handState.activePlayer!!.id)
                actionValidation = action.validate(handState)
                playerRouter.sendPrivateUpdate(handState.activePlayer!!.id, actionValidation)
            }
            while(actionValidation != ValidAction)

            playerRouter.broadcastPlayerAction(handState.activePlayer!!.id, action)
            handState = action.apply(handState)
            playerRouter.broadcast(handState)
        }

        return handState
    }

    /**
     * should:
     * init active player
     * collect bets (for each player: transfer chips from bet to chipsInPot)
     * set next betting round
     * reset last aggressor
     * reset last legal bet
     * reset min raise
     * reset extra bet
     * possibly post blinds and ante if called pre_Game
     * deal proper cards
     */
    private fun prepareForNextBettingRound(handState: HandState): HandState {
        return handState
    }
}
