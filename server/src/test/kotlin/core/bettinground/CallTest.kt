package core.bettinground

import core.gameflow.*
import core.gameflow.handstate.HandState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallTest {

    private val blindsMock = Blinds(100, 200)
    private val startingStack = 1000

    @Test
    fun `calling should be an ValidAction`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = null)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = startingStack, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind,
                extraBet = 0,
                minRaise = blindsMock.bigBlind * 2
        ).build()

        val call = Call()
        assert(call.validate(state) is ValidAction)
    }

    @Test
    fun `applying call with enough chips`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = null)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = startingStack, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind,
                extraBet = 0,
                minRaise = blindsMock.bigBlind * 2
        ).build()

        val call = Call()
        assert(call.validate(state) is ValidAction)

        val newState = call.apply(state)

        assert(newState.players[3].bet == blindsMock.bigBlind)
        assert(newState.players[3].stack == startingStack - blindsMock.bigBlind)
        assert(newState.players[3].lastAction == ActionType.CALL)
        assert(newState.players[3].isDecisive)
        assert(newState.minRaise == blindsMock.bigBlind * 2)
        assert(newState.activePlayer == newState.players[0])
        assert(newState.lastAggressor == null)
        assert(newState.lastLegalBet == blindsMock.bigBlind)
    }

    @Test
    fun `applying call without enough chips`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack, lastAction = null)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = 100, lastAction = null) // active player

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind,
                extraBet = 0,
                minRaise = blindsMock.bigBlind * 2
        ).build()

        val call = Call()
        assert(call.validate(state) is ValidAction)

        val newState = call.apply(state)

        assert(newState.players[3].bet == 100)
        assert(newState.players[3].stack == 0)
        assert(newState.players[3].lastAction == ActionType.CALL)
        assert(!newState.players[3].isDecisive)
        assert(newState.players[3].isAllIn)
        assert(newState.minRaise == blindsMock.bigBlind * 2)
        assert(newState.activePlayer == newState.players[0])
        assert(newState.lastAggressor == null)
        assert(newState.lastLegalBet == blindsMock.bigBlind)
    }
}
