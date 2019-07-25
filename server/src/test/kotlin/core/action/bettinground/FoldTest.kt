package core.action.bettinground

import core.gameflow.BettingRound
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
        val player0 = Player(position = 0, stack = 150, currentBet = 100) // original raiser
        val player1 = Player(position = 1, stack = 200) // active player
        val player2 = Player(position = 2, stack = 100, folded = true)
        val player3 = Player(position = 3, stack = 100, currentBet = 50)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                currentBet = 100
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == player3)
        assert(newState.playersInGame.toSet() == setOf(player0, player3))
    }

    @Test
    fun `folding to original raiser should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 0, folded = true)
        val player1 = Player(position = 1, stack = 100, currentBet = 100) // original raiser
        val player2 = Player(position = 2, stack = 100, currentBet = 100)
        val player3 = Player(position = 3, stack = 200) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                currentBet = 100
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player1, player2))
    }

    @Test
    fun `folding with only big blind left in game (no aggressor) should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 100, folded = true)
        val player1 = Player(position = 1, stack = 100, folded = true)
        val player2 = Player(position = 2, stack = 100, currentBet = 50) // active player / small blind
        val player3 = Player(position = 3, stack = 100, currentBet = 100) // big blind

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                currentBet = 100
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player3))
    }

    @Test
    fun `folding to big blind pre-flop should set activePlayer to BB if no raise was made`() {
        val player0 = Player(position = 0, stack = 100, folded = true)
        val player1 = Player(position = 1, stack = 100, currentBet = 100) // button
        val player2 = Player(position = 2, stack = 100, currentBet = 50) // active player / small blind
        val player3 = Player(position = 3, stack = 100, currentBet = 100) // big blind

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                buttonPosition = 1,
                activePlayer = player2,
                currentBet = 100,
                bettingRound = BettingRound.PRE_FLOP
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == player3)
        assert(newState.playersInGame.toSet() == setOf(player1, player3))
    }

    @Test
    fun `folding to big blind pre-flop should end the action if a raise was made`() {
        val player0 = Player(position = 0, stack = 100, folded = true)
        val player1 = Player(position = 1, stack = 100, currentBet = 200) // button
        val player2 = Player(position = 2, stack = 100, currentBet = 50) // active player / small blind
        val player3 = Player(position = 3, stack = 100, currentBet = 200) // big blind (raiser)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                buttonPosition = 1,
                activePlayer = player2,
                currentBet = 200,
                bettingRound = BettingRound.PRE_FLOP
        )

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player1, player3))
    }
}
