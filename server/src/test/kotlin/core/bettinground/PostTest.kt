package core.bettinground

import core.gameflow.*
import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostTest {

    @Test
    fun `posting the blind should transfer chips from player's stack to their bet`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = null) // active player
        val player2 = Player(position = 2, stack = 600, bet = 0, lastAction = null)
        val player3 = Player(position = 3, stack = 400, bet = 0, lastAction = null)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 0,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val post = Post(50)
        val newState = post.apply(state)
        val updatedPlayer = newState.players[1]

        assert(updatedPlayer.stack == player1.stack - state.blinds.smallBlind)
        assert(updatedPlayer.bet == state.blinds.smallBlind)
    }

    @Test
    fun `posting the blind should put player all in if they don't have enough chips`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 100, bet = 0, lastAction = null) // active player
        val player2 = Player(position = 2, stack = 600, bet = 0, lastAction = null)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(200, 400),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 0,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val post = Post(200)
        val newState = post.apply(state)
        val updatedPlayer = newState.players[1]

        assert(updatedPlayer.stack == 0)
        assert(updatedPlayer.bet == player1.stack)
        assert(updatedPlayer.isAllIn)
    }

    @Test
    fun `posting small blind should not be considered a legal bet and shouldn't affect the min raise`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 0, lastAction = null) // active player
        val player2 = Player(position = 2, stack = 600, bet = 0, lastAction = null)
        val player3 = Player(position = 3, stack = 400, bet = 0, lastAction = null)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 0,
                minRaise = 100,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val post = Post(50)
        val newState = post.apply(state)

        assert(newState.lastLegalBet == 0)
        assert(newState.extraBet == 50)
        assert(newState.minRaise == 100)
    }

    @Test
    fun `posting big blind should automatically update the min raise and current bet`() {
        val player0 = Player(position = 0, stack = 500, bet = 0, lastAction = null) // BTN
        val player1 = Player(position = 1, stack = 200, bet = 50, lastAction = ActionType.POST)
        val player2 = Player(position = 2, stack = 600, bet = 0, lastAction = null) // active player
        val player3 = Player(position = 3, stack = 400, bet = 0, lastAction = null)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player1,
                lastLegalBet = 0,
                extraBet = 50,
                minRaise = 100,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val post = Post(100)
        val newState = post.apply(state)

        assert(newState.lastLegalBet == 100)
        assert(newState.extraBet == 0)
        assert(newState.minRaise == 200)
    }
}
