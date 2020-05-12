package core.handflow

import core.betting.ActionType
import core.flowUtils.Blinds
import core.player.Player
import core.flowUtils.Positions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateExtensionsTest {

    @Test
    fun `updateActivePlayer should properly substitute active player in players list`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 100)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        val newPlayer = player3.copy(stack = 0, bet = 100)
        val newState = state.updateActivePlayer(newPlayer)

        assert(newState.players[3] == newPlayer)
    }

    @Test
    fun `rebuild should create new HandState with properly updated fields`() {

        val player0 = Player(seat = 0, stack = 100)
        val player1 = Player(seat = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(seat = 2, stack = 100)
        val player3 = Player(seat = 3, stack = 0, lastAction = ActionType.ALL_IN)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3
        ).build()

        val newState = state.rebuild(activePlayer = null, extraBet = 250, minRaise = 700)

        assert(newState.players == state.players)
        assert(newState.blinds == state.blinds)
        assert(newState.positions == state.positions)
        assert(newState.activePlayer == null)
        assert(newState.extraBet == 250)
        assert(newState.minRaise == 700)
    }
}
