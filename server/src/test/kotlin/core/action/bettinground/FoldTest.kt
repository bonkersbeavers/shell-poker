package core.action.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoldTest {

    private val blindsMock = Blinds(0, 0)

    @Test
    fun `folding with active players ahead should simply move activePlayer`() {
        val player0 = Player(position = 0, stack = 0, folded = false) // aggressor
        val player1 = Player(position = 1, stack = 0, folded = false) // active player
        val player2 = Player(position = 2, stack = 0, folded = true)
        val player3 = Player(position = 3, stack = 0, folded = false)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = player0
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == player3)
        assert(newState.playersInGame.toSet() == setOf(player0, player3))
    }

    @Test
    fun `folding with last aggressor as the next player should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0, folded = false) // aggressor
        val player2 = Player(position = 2, stack = 0, folded = false)
        val player3 = Player(position = 3, stack = 0, folded = false) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                lastAggressor = player1
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player1, player2))
    }

    @Test
    fun `folding with only big blind left in game (no aggressor) should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 0, folded = true)
        val player2 = Player(position = 2, stack = 0, folded = false) // active player
        val player3 = Player(position = 3, stack = 0, folded = false) // big blind

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = null
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player3))
    }
}
