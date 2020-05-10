package core.betting

import core.flowUtils.*
import core.handflow.HandState
import core.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallTest {

    @Test
    fun `calling should be an ValidAction`() {
        val player0 = Player(position = 0, bet = 0, stack = 1000, lastAction = null)
        val player1 = Player(position = 1, bet = 100, stack = 900, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = 200, stack = 800, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = 1000, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = 200,
                extraBet = 0,
                minRaise = 400
        ).build()

        assert(Call.validate(state) == ValidAction)
    }

    @Test
    fun `applying call with enough chips`() {
        val player0 = Player(position = 0, bet = 0, stack = 1000, lastAction = null)
        val player1 = Player(position = 1, bet = 100, stack = 900, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = 200, stack = 800, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = 1000, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = 200,
                extraBet = 0,
                minRaise = 400
        ).build()

        assert(Call.validate(state) == ValidAction)

        val newState = Call.apply(state)

        assert(newState.players[3].bet == 200)
        assert(newState.players[3].stack == 800)
        assert(newState.players[3].lastAction == ActionType.CALL)
        assert(newState.players[3].isDecisive)
        assert(newState.minRaise == 400)
        assert(newState.activePlayer == newState.players[0])
        assert(newState.lastAggressor == null)
        assert(newState.lastLegalBet == 200)
    }

    @Test
    fun `applying call without enough chips`() {
        val player0 = Player(position = 0, bet = 0, stack = 1000, lastAction = null)
        val player1 = Player(position = 1, bet = 100, stack = 900, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = 200, stack = 800, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = 100, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = 200,
                extraBet = 0,
                minRaise = 400
        ).build()

        assert(Call.validate(state) == ValidAction)

        val newState = Call.apply(state)

        assert(newState.players[3].bet == 100)
        assert(newState.players[3].stack == 0)
        assert(newState.players[3].lastAction == ActionType.CALL)
        assert(!newState.players[3].isDecisive)
        assert(newState.players[3].isAllIn)
        assert(newState.minRaise == 400)
        assert(newState.activePlayer == newState.players[0])
        assert(newState.lastAggressor == null)
        assert(newState.lastLegalBet == 200)
    }
}
