package core.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import core.gameflow.Positions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RaiseTest {

    @Test
    fun `raise is invalid if it is lower than current min raise`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.CHECK)
        val player2 = Player(position = 2, stack = 600, bet = 300, lastAction = ActionType.BET)
        val player3 = Player(position = 3, stack = 400, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                lastAggressor = player2,
                activePlayer = player3,
                lastLegalBet = 300,
                minRaise = 600
        )

        val raise = Raise(400)
        assert(raise.validate(state) is InvalidAction)
    }

    @Test
    fun `raise is invalid if it is higher than player's stack`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.CHECK)
        val player2 = Player(position = 2, stack = 600, bet = 300, lastAction = ActionType.BET)
        val player3 = Player(position = 3, stack = 500, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                lastAggressor = player2,
                activePlayer = player3,
                lastLegalBet = 300,
                minRaise = 600
        )

        val raise = Raise(600)
        assert(raise.validate(state) is InvalidAction)
    }

    @Test
    fun `raise is invalid if there wasn't any legal bet made yet`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.CHECK)
        val player2 = Player(position = 2, stack = 0, bet = 60, lastAction = ActionType.ALL_IN)
        val player3 = Player(position = 3, stack = 500, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                lastAggressor = player2,
                activePlayer = player3,
                lastLegalBet = 0,
                extraBet = 60,
                minRaise = 100
        )

        val raise = Raise(300)
        assert(raise.validate(state) is InvalidAction)
    }

    @Test
    fun `regular raise should transfer chips between player's stack and bet and update hand state`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.CHECK)
        val player2 = Player(position = 2, stack = 600, bet = 300, lastAction = ActionType.BET)
        val player3 = Player(position = 3, stack = 800, bet = 0, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                lastAggressor = player1,
                activePlayer = player3,
                lastLegalBet = 300,
                extraBet = 100,
                minRaise = 600
        )

        val raise = Raise(700)
        assert(raise.validate(state) is ValidAction)

        val newState = raise.apply(state)
        val updatedPlayer = newState.players[3]

        assert(updatedPlayer.stack == 100)
        assert(updatedPlayer.bet == 700)

        assert(newState.lastAggressor == updatedPlayer)
        assert(newState.lastLegalBet == 700)
        assert(newState.extraBet == 0)
        assert(newState.minRaise == 1100)
    }
}
