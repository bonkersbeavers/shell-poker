package core.handflow
//
//import core.RoomSettings
//import gameControl.adapters.*
//import core.betting.*
//import core.flowUtils.*
//import core.dealer.IDealer
//import gameControl.gamestate.GameState
//import core.player.PlayerStatus
//import core.player.*
//
//class DeprecatedHandManager(val dealer: IDealer, val communicator: ICommunicator, val roomSettings: RoomSettings) : IDeprecatedHandManager {
//
//    override fun playHand(gameState: GameState): GameState = gameState
//            .initializeHand()
//            .handleAction()
//            .handleShowdown()
//            .handlePotDistribution()
//            .finalizeHand()
//
//    // ---------------------------
//    // High level hand flow components
//    // ---------------------------
//
//    private fun GameState.initializeHand(): HandState {
//        dealer.shuffle()
//
//        val players = this.playersStatuli.map { Player(seat = it.seat, stack = it.stack) }
//
//        var initialStateBuilder = HandState.ImmutableBuilder(
//                players = players,
//                positions = this.positions,
//                blinds = this.blinds
//        )
//
//        initialStateBuilder = shiftPositions(initialStateBuilder, roomSettings)
//        initialStateBuilder = postBlindsAndAnte(initialStateBuilder, this.newPlayersIds)
//
//        return initialStateBuilder.build()
//    }
//
//    private fun HandState.handleAction(): HandState {
//        var handState = this
//
//        while (true) {
//            handState = dealer.deal(handState)
//            communicator.broadcastUpdate(HandStateUpdate(handState))
//            handState = runBettingRound(handState)
//            communicator.broadcastUpdate(HandStateUpdate(handState))
//
//            if ((handState.bettingRound == BettingRound.RIVER) or (handState.players.decisive().size < 2))
//                break
//
//            handState = handState.rebuild(bettingRound = handState.bettingRound.next())
//        }
//
//        return handState
//    }
//
//    private fun HandState.handleShowdown(): HandState {
//        val showdownOrder = resolveShowdown(this)
//
//        showdownOrder.forEach { defaultAction ->
//            val playerShowdownDecision = communicator.requestShowdownAction(defaultAction.playerId)
//            val action = playerShowdownDecision ?: defaultAction
//            communicator.broadcastUpdate(ShowdownActionUpdate(action))
//        }
//
//        var newState = this
//
//        if (this.players.inGame().size > 1) {
//            while (newState.bettingRound != BettingRound.RIVER) {
//                newState = newState.rebuild(bettingRound = newState.bettingRound.next())
//                newState = dealer.deal(newState)
//                communicator.broadcastUpdate(HandStateUpdate(newState))
//            }
//        }
//
//        return newState
//    }
//
//    private fun HandState.handlePotDistribution(): HandState {
//        val potResults = resolvePot(this)
//
//        potResults.forEach { result ->
//            communicator.broadcastUpdate(PotResultUpdate(result))
//        }
//
//        val newState = distributePot(this)
//        communicator.broadcastUpdate(HandStateUpdate(newState))
//
//        return newState
//    }
//
//    private fun HandState.finalizeHand(): GameState {
//        val playersInfo = this.players.map { PlayerStatus(it.seat, it.stack) }
//        return GameState(
//                playersStatuli = playersInfo,
//                positions = this.positions,
//                blinds = this.blinds,
//                newPlayersIds = emptyList()
//        )
//    }
//
//    // ---------------------------
//    // Low level helpers
//    // ---------------------------
//
//    private fun runBettingRound(startingHandState: HandState): HandState {
//        var handState = initializeBettingRound(startingHandState)
//
//        while (handState.activePlayer != null) {
//            val activePlayer = handState.activePlayer!!
//
//            var action: BettingAction
//            var actionValidation: ActionValidation
//
//            do {
//                action = communicator.requestBettingAction(activePlayer.id)
//                actionValidation = action.validate(handState)
//                communicator.sendUpdate(activePlayer.id, ActionValidationUpdate(actionValidation))
//            }
//            while (actionValidation != ValidAction)
//
//            communicator.broadcastUpdate(PlayerActionUpdate(activePlayer.id, action))
//            handState = action.apply(handState)
//            communicator.broadcastUpdate(HandStateUpdate(handState))
//        }
//
//        return finalizeBettingRound(handState)
//    }
//
//    private fun initializeBettingRound(handState: HandState): HandState {
//        val firstActivePlayer = when (handState.bettingRound) {
//            BettingRound.PRE_FLOP -> handState.players.nextDecisive(handState.positions.bigBlind)
//            else -> handState.players.nextDecisive(handState.positions.button)
//        }
//
//        val lastLegalBet = when (handState.bettingRound) {
//            BettingRound.PRE_FLOP -> handState.blinds.bigBlind
//            else -> 0
//        }
//
//        val minRaise = when (handState.bettingRound) {
//            BettingRound.PRE_FLOP -> handState.blinds.bigBlind * 2
//            else -> handState.blinds.bigBlind
//        }
//
//        return handState.rebuild(
//                activePlayer = firstActivePlayer,
//                lastLegalBet = lastLegalBet,
//                minRaise = minRaise
//        )
//    }
//
//    private fun finalizeBettingRound(handState: HandState): HandState {
//        val newPlayers = handState.players.map {
//            val newPlayer = it.moveBetToPot()
//            if (newPlayer.isDecisive) newPlayer.copy(lastAction = null) else newPlayer
//        }
//
//        return handState.rebuild(
//                players = newPlayers,
//                lastAggressor = null,
//                lastLegalBet = 0,
//                minRaise = handState.blinds.bigBlind,
//                extraBet = 0
//        )
//    }
//}
