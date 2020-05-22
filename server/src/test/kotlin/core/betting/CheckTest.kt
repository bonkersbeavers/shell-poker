package core.betting
//
//import core.flowUtils.*
//import core.hand.player.betting.ActionType
//import core.handflow.hand.HandState
//import core.player.Player
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class CheckTest {
//
//    @Test
//    fun `checking should be legal if everyone before active player has checked after pre flop`() {
//        val player0 = Player(seat = 0, bet = 0, stack = 1000, lastAction = ActionType.FOLD)
//        val player1 = Player(seat = 1, bet = 0, stack = 1000, lastAction = ActionType.CHECK)
//        val player2 = Player(seat = 2, bet = 0, stack = 1000, lastAction = ActionType.CHECK)
//        val player3 = Player(seat = 3, bet = 0, stack = 1000, lastAction = null) // active player
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player3,
//                lastAggressor = null,
//                bettingRound = BettingRound.FLOP,
//                lastLegalBet = 0
//        ).build()
//
//        assert(Check.validate(state) == ValidAction)
//    }
//
//    @Test
//    fun `checking should be legal as a big blind when big blind is called in pre flop`() {
//        val player0 = Player(seat = 0, bet = 200, stack = 800, lastAction = ActionType.CALL)
//        val player1 = Player(seat = 1, bet = 200, stack = 800, lastAction = ActionType.CALL)
//        val player2 = Player(seat = 2, bet = 200, stack = 800, lastAction = ActionType.POST) // active player / big blind
//        val player3 = Player(seat = 3, bet = 200, stack = 800, lastAction = ActionType.CALL)
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player2,
//                lastAggressor = null,
//                bettingRound = BettingRound.PRE_FLOP,
//                lastLegalBet = 200
//        ).build()
//
//        assert(Check.validate(state) == ValidAction)
//    }
//
//    @Test
//    fun `checking should be illegal as a big blind when big blind is raised in pre flop`() {
//        val player0 = Player(seat = 0, bet = 0, stack = 1000, lastAction = ActionType.FOLD)
//        val player1 = Player(seat = 1, bet = 400, stack = 1000, lastAction = ActionType.CALL)
//        val player2 = Player(seat = 2, bet = 200, stack = 1000, lastAction = ActionType.POST) // active player / big blind
//        val player3 = Player(seat = 3, bet = 400, stack = 1000, lastAction = ActionType.RAISE)
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player2,
//                lastAggressor = player3,
//                bettingRound = BettingRound.PRE_FLOP,
//                lastLegalBet = 400
//        ).build()
//
//        assert(Check.validate(state) == InvalidAction("Cannot check when there is a bet to call"))
//    }
//
//    @Test
//    fun `checking should be illegal when a bet is made after pre flop`() {
//        val player0 = Player(seat = 0, bet = 0, stack = 1000, lastAction = ActionType.FOLD)
//        val player1 = Player(seat = 1, bet = 500, stack = 300, lastAction = ActionType.BET)
//        val player2 = Player(seat = 2, bet = 0, stack = 800, lastAction = null) // active player
//        val player3 = Player(seat = 3, bet = 0, stack = 800, lastAction = null)
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player2,
//                lastAggressor = player1,
//                bettingRound = BettingRound.FLOP,
//                lastLegalBet = 500
//        ).build()
//
//        assert(Check.validate(state) == InvalidAction("Cannot check when there is a bet to call"))
//    }
//
//    @Test
//    fun `applying check normally after pre flop`() {
//        val player0 = Player(seat = 0, bet = 0, stack = 800, lastAction = null)
//        val player1 = Player(seat = 1, bet = 0, stack = 800, lastAction = null) // active player
//        val player2 = Player(seat = 2, bet = 0, stack = 800, lastAction = null)
//        val player3 = Player(seat = 3, bet = 0, stack = 1000, lastAction = ActionType.FOLD)
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player1,
//                lastAggressor = null,
//                bettingRound = BettingRound.FLOP,
//                lastLegalBet = 0
//        ).build()
//
//        assert(Check.validate(state) == ValidAction)
//
//        val newState = Check.apply(state)
//
//        assert(newState.players[1].bet == 0)
//        assert(newState.players[1].lastAction == ActionType.CHECK)
//        assert(newState.totalBet == 0)
//        assert(newState.lastAggressor == null)
//        assert(newState.minRaise == 200)
//        assert(newState.activePlayer == player2)
//    }
//
//    @Test
//    fun `applying check and it should end the round after pre flop`() {
//        val player0 = Player(seat = 0, bet = 0, stack = 1000, lastAction = ActionType.FOLD)
//        val player1 = Player(seat = 1, bet = 100, stack = 900, lastAction = ActionType.FOLD)
//        val player2 = Player(seat = 2, bet = 0, stack = 800, lastAction = ActionType.CHECK)
//        val player3 = Player(seat = 3, bet = 0, stack = 800, lastAction = null) // active player
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player3,
//                lastAggressor = null,
//                bettingRound = BettingRound.FLOP,
//                lastLegalBet = 0
//        ).build()
//
//        assert(Check.validate(state) == ValidAction)
//
//        val newState = Check.apply(state)
//
//        assert(newState.players[3].bet == 0)
//        assert(newState.players[3].lastAction == ActionType.CHECK)
//        assert(newState.totalBet == 0)
//        assert(newState.lastAggressor == null)
//        assert(newState.minRaise == 200)
//        assert(newState.activePlayer == null)
//    }
//
//    @Test
//    fun `applying check as a big blind in pre flop`() {
//        val player0 = Player(seat = 0, bet = 200, stack = 800, lastAction = ActionType.CALL)
//        val player1 = Player(seat = 1, bet = 100, stack = 900, lastAction = ActionType.FOLD)
//        val player2 = Player(seat = 2, bet = 200, stack = 800, lastAction = ActionType.POST) // active player / big blind
//        val player3 = Player(seat = 3, bet = 200, stack = 800, lastAction = ActionType.RAISE)
//
//        val state = HandState.ImmutableBuilder(
//                players = listOf(player0, player1, player2, player3),
//                blinds = Blinds(100, 200),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                activePlayer = player2,
//                lastAggressor = player3,
//                bettingRound = BettingRound.PRE_FLOP,
//                lastLegalBet = 200,
//                minRaise = 400
//        ).build()
//
//        assert(Check.validate(state) == ValidAction)
//
//        val newState = Check.apply(state)
//
//        assert(newState.players[2].bet == 200)
//        assert(newState.players[2].lastAction == ActionType.CHECK)
//        assert(newState.totalBet == 200)
//        assert(newState.lastAggressor == player3)
//        assert(newState.activePlayer == null)
//        assert(newState.minRaise == 200 * 2)
//    }
//}
