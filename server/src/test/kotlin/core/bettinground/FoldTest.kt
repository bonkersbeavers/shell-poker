package core.bettinground

import core.gameflow.*
import core.gameflow.handstate.HandState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoldTest {

    @Test
    fun `folding with active players ahead should simply move activePlayer`() {
        val player0 = Player(position = 0, stack = 100, bet = 100, lastAction = ActionType.RAISE) // BTN / aggressor
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.CHECK) // active player
        val player2 = Player(position = 2, stack = 100, bet = 50, lastAction = ActionType.BET)
        val player3 = Player(position = 3, stack = 100, bet = 0, lastAction = ActionType.FOLD)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 100,
                bettingRound = BettingRound.FLOP
        ).build()

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == player2)
        assert(newState.playersInGame.toSet() == setOf(player0, player2))
    }

    @Test
    fun `folding to original raiser should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 150, bet = 100, lastAction = ActionType.BET) // BTN / aggressor
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 100, bet = 100, lastAction = ActionType.CALL)
        val player3 = Player(position = 3, stack = 100, bet = 0, lastAction = ActionType.CHECK) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastLegalBet = 100,
                bettingRound = BettingRound.FLOP
        ).build()

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player0, player2))
    }

    @Test
    fun `folding with only big blind left in game (no aggressor) should set activePlayer to null`() {
        val player0 = Player(position = 0, stack = 100, bet = 0, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, stack = 100, bet = 0, lastAction = ActionType.FOLD) // BTN
        val player2 = Player(position = 2, stack = 100, bet = 50, lastAction = ActionType.POST) // SB / active player
        val player3 = Player(position = 3, stack = 100, bet = 100, lastAction = ActionType.POST) // BB

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 1, smallBlind = 2, bigBlind = 3),
                activePlayer = player2,
                lastLegalBet = 100,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player3))
    }

    @Test
    fun `folding to big blind pre-flop should set activePlayer to BB even if no raise was made`() {
        val player0 = Player(position = 0, stack = 100, bet = 0, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, stack = 100, bet = 100, lastAction = ActionType.CALL) // BTN
        val player2 = Player(position = 2, stack = 100, bet = 50, lastAction = ActionType.POST) // SB / active player
        val player3 = Player(position = 3, stack = 100, bet = 100, lastAction = ActionType.POST) // BB

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 1, smallBlind = 2, bigBlind = 3),
                activePlayer = player2,
                lastLegalBet = 100,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == player3)
        assert(newState.playersInGame.toSet() == setOf(player1, player3))
    }

    @Test
    fun `folding to big blind pre-flop should end the action if BB raised before`() {
        val player0 = Player(position = 0, stack = 100, bet = 0, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, stack = 100, bet = 200, lastAction = ActionType.CALL) // BTN
        val player2 = Player(position = 2, stack = 100, bet = 100, lastAction = ActionType.CALL) // SB / active player
        val player3 = Player(position = 3, stack = 100, bet = 200, lastAction = ActionType.RAISE) // BB / aggressor

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 1, smallBlind = 2, bigBlind = 3),
                activePlayer = player2,
                lastLegalBet = 200,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val fold = Fold()
        val newState = fold.apply(state)

        assert(newState.activePlayer == null)
        assert(newState.playersInGame.toSet() == setOf(player1, player3))
    }
}
