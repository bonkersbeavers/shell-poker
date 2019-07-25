package core.action.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CheckTest {

    private val blindsMock = Blinds(100, 200)

    @Test
    fun `checking should be legal after round 1 if everyone has checked before you`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0) // active player
        val player3 = Player(position = 3, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = null
        )

        val check = Check()
        assert(check.isLegal(state))
    }

    // TODO: BUGGED FOR NOW, IMPOSSIBLE SCENARIO ACCRORDING TO @meszszi
    @Test
    fun `checking should be legal as a big blind when big blind is called`() {
        val player0 = Player(position = 0, currentBet = blindsMock.bigBlind, stack = 0)
        val player1 = Player(position = 1, currentBet = blindsMock.bigBlind, stack = 0)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player
        val player3 = Player(position = 3, currentBet = blindsMock.bigBlind, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1
        )

        val check = Check()
        assert(check.isLegal(state))
    }

    @Test
    fun `checking should be illegal as a big blind when big blind is raised`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, currentBet = blindsMock.bigBlind, stack = 0)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player
        val player3 = Player(position = 3, currentBet = blindsMock.bigBlind + 100, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player3
        )

        val check = Check()
        assert(!check.isLegal(state))
    }

    @Test
    fun `checking should be illegal when a bet is made after round 1`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, currentBet = 200, stack = 0)
        val player2 = Player(position = 2, currentBet = 0, stack = 0) // active player
        val player3 = Player(position = 3, currentBet = 0, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1
        )

        val check = Check()
        assert(!check.isLegal(state))
    }

    @Test
    fun `applying check normally after round 1`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0) // active player
        val player2 = Player(position = 2, stack = 0, folded = true)
        val player3 = Player(position = 3, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = null
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player1.currentBet == 0)
        assert(newState.activePlayer == player3)
    }

    @Test
    fun `applying check after round 1 and it should end the round`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)
        val player3 = Player(position = 3, stack = 0) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                lastAggressor = null
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player1.currentBet == 0)
        assert(newState.activePlayer == null)
    }

    @Test
    fun `applying check in round 1 as a big blind`() {
        val player0 = Player(position = 0, currentBet = blindsMock.bigBlind, stack = 0)
        val player1 = Player(position = 1, stack = 0, folded = true)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 0) // active player
        val player3 = Player(position = 3, stack = 0, folded = true)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player0
        )

        val check = Check()
        assert(check.isLegal(state))

        val newState = check.apply(state)

        assert(player2.currentBet == blindsMock.bigBlind)
        assert(newState.activePlayer == null)
    }
}
