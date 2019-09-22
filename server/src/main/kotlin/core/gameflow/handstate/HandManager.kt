package core.gameflow.handstate

import core.adapters.Communicator
import core.bettinground.*
import core.gameflow.*
import core.gameflow.gamestate.GameState
import core.gameflow.gamestate.PlayerInfo
import core.gameflow.player.*

class HandManager(val communicator: Communicator, val roomSettings: RoomSettings) {

    private val dealer: Dealer = Dealer()
    fun playHand(gameState: GameState, newPlayersIds: Set<Int>): GameState {

        var handState = initializeHand(gameState, newPlayersIds)

        handState = mainLoop(handState)

        return finalizeHand(handState)
    }

    private fun initializeHand(gameState: GameState, newPlayersIds: Set<Int>): HandState {
        val players = gameState.playersInfo.map { Player(position = it.position, stack = it.stack) }

        var initialStateBuilder = HandState.ImmutableBuilder(
                players = players,
                positions = gameState.positions,
                blinds = gameState.blinds
        )

        initialStateBuilder = postBlindsAndAnte(initialStateBuilder, newPlayersIds)
        return initialStateBuilder.build()
    }

    private fun finalizeHand(handState: HandState): GameState {
        val playersInfo = handState.players.map { PlayerInfo(it.position, it.stack) }
        return GameState(
                playersInfo = playersInfo,
                positions = handState.positions,
                blinds = handState.blinds
        )
    }

    private fun mainLoop(startingHandState: HandState): HandState {
        val initialBuilder = startingHandState.toBuilder()
        var handState = shiftPositions(initialBuilder, roomSettings).build()
        this.dealer.shuffle()

        while ((handState.bettingRound != BettingRound.RIVER) and (handState.players.decisive().size > 1)) {
            handState = initializeBettingRound(handState)
//            communicator.broadcastHandState(handState)
            handState = bettingRound(handState)
            handState = finalizeBettingRound(handState)
//            communicator.broadcastHandState(handState)
        }

        val showdownOrder = resolveShowdown(handState)
        println(showdownOrder)
        // TODO: broadcast showdown

        if (handState.players.inGame().size > 1) {
            while (handState.bettingRound != BettingRound.RIVER)
                handState = this.dealer.deal(handState)
        }

        handState = distributePot(handState)
//        communicator.broadcastHandState(handState)

        return handState
    }

    private fun bettingRound(startingHandState: HandState): HandState {
        var handState = startingHandState

        while (handState.activePlayer != null) {
            var action: BettingAction
            var actionValidation: ActionValidation

            do {
                action = communicator.requestBettingAction(handState.activePlayer!!.id)
                actionValidation = action.validate(handState)
                communicator.sendUpdate(handState.activePlayer!!.id, actionValidation) // TODO: Send action validation to player
            }
            while (actionValidation != ValidAction)

            communicator.broadcastUpdate(action) // TODO: Broadcast player action
            handState = action.apply(handState)
            communicator.broadcastUpdate(handState) // TODO: Broadcast new stacks, bets, new activePlayer etc.
        }

        return handState
    }

    private fun initializeBettingRound(handState: HandState): HandState {
        val firstActivePlayer = when (handState.bettingRound) {
            BettingRound.PRE_FLOP -> handState.players.nextDecisive(handState.positions.bigBlind)
            else -> handState.players.nextDecisive(handState.positions.button)
        }

        return dealer.deal(handState).rebuild(activePlayer = firstActivePlayer)
    }

    private fun finalizeBettingRound(handState: HandState): HandState {
        return handState.rebuild(
                players = handState.players.map { it.moveBetToPot() },
                bettingRound = handState.bettingRound.next(),
                lastAggressor = null,
                lastLegalBet = 0,
                minRaise = handState.blinds.bigBlind,
                extraBet = 0
        )
    }
}
