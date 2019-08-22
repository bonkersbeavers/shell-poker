package core.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AllInTest {

    @Test
    fun `all in higher or equal to min raise should properly update hand state and player's bet`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 300, lastAction = ActionType.BET)
        val player2 = Player(position = 2, stack = 0, bet = 400, lastAction = ActionType.ALL_IN)
        val player3 = Player(position = 3, stack = 900, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                lastAggressor = player1,
                activePlayer = player3,
                lastLegalBet = 300,
                extraBet = 100,
                minRaise = 600
        )

        val allIn = AllIn()
        assert(allIn.validate(state) is ValidAction)

        val newState = allIn.apply(state)
        val updatedPlayer = newState.players[3]

        assert(updatedPlayer.isAllIn)
        assert(updatedPlayer.bet == 900)
        assert(updatedPlayer.stack == 0)

        assert(newState.lastLegalBet == 900)
        assert(newState.extraBet == 0)
        assert(newState.minRaise == 1500)
        assert(newState.lastAggressor == updatedPlayer)
    }

    @Test
    fun `all in lower than min raise but higher than total bet should update extraBet field`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 300, lastAction = ActionType.BET)
        val player2 = Player(position = 2, stack = 0, bet = 400, lastAction = ActionType.ALL_IN)
        val player3 = Player(position = 3, stack = 500, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                lastAggressor = player1,
                activePlayer = player3,
                lastLegalBet = 300,
                extraBet = 100,
                minRaise = 600
        )

        val allIn = AllIn()
        assert(allIn.validate(state) is ValidAction)

        val newState = allIn.apply(state)
        val updatedPlayer = newState.players[3]

        assert(updatedPlayer.isAllIn)
        assert(updatedPlayer.bet == 500)
        assert(updatedPlayer.stack == 0)

        assert(newState.lastLegalBet == 300)
        assert(newState.extraBet == 200)
        assert(newState.minRaise == 600)
        assert(newState.lastAggressor == player1)
    }

    @Test
    fun `all in lower than current total bet should only update player's stack and bet`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 300, lastAction = ActionType.BET)
        val player2 = Player(position = 2, stack = 0, bet = 400, lastAction = ActionType.ALL_IN)
        val player3 = Player(position = 3, stack = 350, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                lastAggressor = player1,
                activePlayer = player3,
                lastLegalBet = 300,
                extraBet = 100,
                minRaise = 600
        )

        val allIn = AllIn()
        assert(allIn.validate(state) is ValidAction)

        val newState = allIn.apply(state)
        val updatedPlayer = newState.players[3]

        assert(updatedPlayer.isAllIn)
        assert(updatedPlayer.bet == 350)
        assert(updatedPlayer.stack == 0)

        assert(newState.lastLegalBet == 300)
        assert(newState.extraBet == 100)
        assert(newState.minRaise == 600)
        assert(newState.lastAggressor == player1)
    }
}
