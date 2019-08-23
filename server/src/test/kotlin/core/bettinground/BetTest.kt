package core.bettinground

import core.gameflow.Blinds
import core.gameflow.HandState
import core.gameflow.Player
import core.gameflow.BettingRound
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BetTest {
    private val blindsMock = Blinds(100, 200)
    private val startingStack = 1000

    @Test
    fun `bet should be valid if a player has bet above minRaise and he has enough chips`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player1 = Player(position = 1, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player3 = Player(position = 3, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = blindsMock.bigBlind
        )

        val currentStack = startingStack - blindsMock.bigBlind

        val bet1 = Bet(200)
        assert(bet1.validate(state) is ValidAction)

        val bet2 = Bet(199)
        assert((bet2.validate(state) == InvalidAction("Bet of size 199 smaller than minimum legal bet ${blindsMock.bigBlind}")))

        val bet3 = Bet(currentStack - 1)
        assert(bet3.validate(state) is ValidAction)

        val bet4 = Bet(currentStack)
        assert(bet4.validate(state) is ValidAction)

        val bet5 = Bet(currentStack + 1)
        assert(bet5.validate(state) == InvalidAction("Bet of size ${currentStack + 1} larger than player's maximum possible bet $currentStack"))
    }

    @Test
    fun `bet should be invalid if a bet has already been made after pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player1 = Player(position = 1, bet = 300, stack = startingStack - blindsMock.bigBlind - 300, lastAction = ActionType.BET)
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player
        val player3 = Player(position = 3, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 300,
                extraBet = 0,
                minRaise = 600
        )

        val bet1 = Bet(650)
        assert(bet1.validate(state) == InvalidAction("Cannot bet when a bet has already been made"))
    }

    @Test
    fun `bet should be valid if a bet has already been made in pre flop`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player1 = Player(position = 1, bet = blindsMock.smallBlind, stack = startingStack - blindsMock.smallBlind, lastAction = ActionType.POST)
        val player2 = Player(position = 2, bet = blindsMock.bigBlind, stack = startingStack - blindsMock.bigBlind, lastAction = ActionType.POST)
        val player3 = Player(position = 3, bet = 0, stack = startingStack, lastAction = null) // active player

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player3,
                lastAggressor = null,
                bettingRound = BettingRound.PRE_FLOP,
                lastLegalBet = blindsMock.bigBlind,
                extraBet = 0,
                minRaise = blindsMock.bigBlind * 2
        )

        val bet1 = Bet(blindsMock.bigBlind * 2 + 1)
        assert(bet1.validate(state) == InvalidAction("Cannot bet when a bet has already been made"))
    }

    @Test
    fun `applying bet normally`() {
        val player0 = Player(position = 0, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player1 = Player(position = 1, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null) // active player
        val player2 = Player(position = 2, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)
        val player3 = Player(position = 3, bet = 0, stack = startingStack - blindsMock.bigBlind, lastAction = null)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                lastLegalBet = 0,
                extraBet = 0,
                minRaise = blindsMock.bigBlind
        )

        val currentStack = startingStack - blindsMock.bigBlind
        val betSize = 500

        val bet = Bet(betSize)
        assert(bet.validate(state) is ValidAction)

        val newState = bet.apply(state)

        assert(newState.players[1].stack == currentStack - betSize)
        assert(newState.players[1].bet == betSize)
        assert(newState.players[1].lastAction == ActionType.BET)
        assert(newState.players[1].isDecisive)
        assert(newState.activePlayer == newState.players[2])
        assert(newState.minRaise == betSize * 2)
        assert(newState.lastAggressor == newState.players[1])
        assert(newState.lastLegalBet == betSize)
    }
}
