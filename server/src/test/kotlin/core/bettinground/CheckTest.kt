package core.bettinground

import core.gameflow.*
import core.gameflow.handstate.HandState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckTest {

    private val blindsMock = Blinds(100, 200)
    private val startingStack = 1000

    @Test
    fun `checking should be legal if everyone before active player has checked after pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, bet = 0, stack = startingStack, lastAction = ActionType.CHECK)
        val player2 = Player(position = 2, bet = 0, stack = startingStack, lastAction = ActionType.CHECK)
        val player3 = Player(position = 3, bet = 0, stack = startingStack, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 0
        )

        val check = Check()
        assert(check.validate(state) is ValidAction)
    }

    @Test
    fun `checking should be legal as a big blind when big blind is called in pre flop`() {
        val player0 = Player(position = 0, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.CALL)
        val player1 = Player(position = 1, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.CALL)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST) // active player / big blind
        val player3 = Player(position = 3, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.CALL)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind
        )

        val check = Check()
        assert(check.validate(state) is ValidAction)
    }

    @Test
    fun `checking should be illegal as a big blind when big blind is raised in pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, bet = blindsMock.bigBlind + 200, stack = startingStack, lastAction = ActionType.CALL)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack, lastAction = ActionType.POST) // active player / big blind
        val player3 = Player(position = 3, bet = blindsMock.bigBlind + 200, stack = startingStack, lastAction = ActionType.RAISE)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastAggressor = player3,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind + 200
        )

        val check = Check()
        assert(check.validate(state) == InvalidAction("Cannot check when there is a bet to call"))
    }

    @Test
    fun `checking should be illegal when a bet is made after pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, bet = 500, stack = startingStack - blindsMock.bigBlind - 500, lastAction = ActionType.BET)
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player
        val player3 = Player(position = 3, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastAggressor = player1,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 500
        )

        val check = Check()
        assert(check.validate(state) == InvalidAction("Cannot check when there is a bet to call"))
    }

    @Test
    fun `applying check normally after pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player1 = Player(position = 1, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player3 = Player(position = 3, bet = 0, stack = startingStack, lastAction = ActionType.FOLD)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 0
        )

        val check = Check()
        assert(check.validate(state) is ValidAction)

        val newState = check.apply(state)

        assert(newState.players[1].bet == 0)
        assert(newState.players[1].lastAction == ActionType.CHECK)
        assert(newState.totalBet == 0)
        assert(newState.lastAggressor == null)
        assert(newState.minRaise == blindsMock.bigBlind)
        assert(newState.activePlayer == player2)
    }

    @Test
    fun `applying check and it should end the round after pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.CHECK)
        val player3 = Player(position = 3, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 0
        )

        val check = Check()
        assert(check.validate(state) is ValidAction)

        val newState = check.apply(state)

        assert(newState.players[3].bet == 0)
        assert(newState.players[3].lastAction == ActionType.CHECK)
        assert(newState.totalBet == 0)
        assert(newState.lastAggressor == null)
        assert(newState.minRaise == blindsMock.bigBlind)
        assert(newState.activePlayer == null)
    }

    @Test
    fun `applying check as a big blind in pre flop`() {
        val player0 = Player(position = 0, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.CALL)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST) // active player / big blind
        val player3 = Player(position = 3, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.RAISE)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player2,
                lastAggressor = player3,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind,
                minRaise = blindsMock.bigBlind * 2
        )

        val check = Check()
        assert(check.validate(state) is ValidAction)

        val newState = check.apply(state)

        assert(newState.players[2].bet == blindsMock.bigBlind)
        assert(newState.players[2].lastAction == ActionType.CHECK)
        assert(newState.totalBet == blindsMock.bigBlind)
        assert(newState.lastAggressor == player3)
        assert(newState.activePlayer == null)
        assert(newState.minRaise == blindsMock.bigBlind * 2)
    }
}
