package core.action.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import core.gameflow.BettingRound
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckTest {

    private val blindsMock = Blinds(100, 200)

    @Test
    fun `checking should be legal if everyone before active player has checked after pre flop`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)
        val player3 = Player(position = 3, stack = 0) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                currentBet = 0
        )

        val check = Check()
        assert(check.isLegal(state))
    }

    @Test
    fun `checking should be legal as a big blind when big blind is called in pre flop`() {
        val player0 = Player(position = 0, currentBet = blindsMock.bigBlind, stack = 0)
        val player1 = Player(position = 1, currentBet = blindsMock.bigBlind, stack = 0)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player / big blind
        val player3 = Player(position = 3, currentBet = blindsMock.bigBlind, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                currentBet = blindsMock.bigBlind
        )

        val check = Check()
        assert(check.isLegal(state))
    }

    @Test
    fun `checking should be illegal as a big blind when big blind is raised in pre flop`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, currentBet = blindsMock.bigBlind + 100, stack = 0)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player / big blind
        val player3 = Player(position = 3, currentBet = blindsMock.bigBlind + 100, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player3,
                bettingRound = BettingRound.PRE_FLOP,
                currentBet = blindsMock.bigBlind + 100
        )

        val check = Check()
        assert(!check.isLegal(state))
    }

    @Test
    fun `checking should be illegal when a bet is made after pre flop`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, currentBet = 200, stack = 0)
        val player2 = Player(position = 2, currentBet = 0, stack = 0) // active player
        val player3 = Player(position = 3, currentBet = 0, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1,
                bettingRound = BettingRound.FLOP,
                currentBet = 200
        )

        val check = Check()
        assert(!check.isLegal(state))
    }

    @Test
    fun `applying check normally after pre flop`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0) // active player
        val player2 = Player(position = 2, stack = 0, folded = true)
        val player3 = Player(position = 3, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                currentBet = 0
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player1.currentBet == 0)
        assert(state.totalBet == 0)
        assert(state.lastAggressor == null)
        assert(state.minRaise == blindsMock.bigBlind)
        assert(newState.activePlayer == player3)
    }

    @Test
    fun `applying check and it should end the round after pre flop`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)
        val player3 = Player(position = 3, stack = 0) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                currentBet = 0
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player1.currentBet == 0)
        assert(state.totalBet == 0)
        assert(state.lastAggressor == null)
        assert(state.minRaise == blindsMock.bigBlind)
        assert(newState.activePlayer == null)
    }

    @Test
    fun `applying check as a big blind in pre flop`() {
        val player0 = Player(position = 0, currentBet = blindsMock.bigBlind, stack = 0)
        val player1 = Player(position = 1, stack = 0, folded = true)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player / big blind
        val player3 = Player(position = 3, stack = 0, folded = true)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player0,
                bettingRound = BettingRound.PRE_FLOP,
                currentBet = blindsMock.bigBlind
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player2.currentBet == blindsMock.bigBlind)
        assert(state.totalBet == blindsMock.bigBlind)
        assert(state.lastAggressor == player0)
        assert(newState.activePlayer == null)
    }
}
