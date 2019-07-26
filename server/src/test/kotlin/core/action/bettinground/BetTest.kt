package core.action.bettinground

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
    fun `bet should be legal if a player has bet above minRaise and he has enough chips`() {
        val player0 = Player(position = 0, currentBet = 0, stack = startingStack)
        val player1 = Player(position = 1, currentBet = 0, stack = startingStack)  // active player
        val player2 = Player(position = 2, currentBet = 0, stack = startingStack)
        val player3 = Player(position = 3, currentBet = 0, stack = startingStack)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player1,
                lastAggressor = null,
                bettingRound = BettingRound.FLOP,
                currentBet = 0
        )


        val bet1 = Bet(200)
        assert(bet1.isLegal(state))

        //TODO: NOT ABOVE MIN RAISE EXCEPTION
        val bet2 = Bet(199)
        assert(!bet2.isLegal(state))

        val bet3 = Bet(999)
        assert(bet3.isLegal(state))

        //ALL IN BET CASE -> WHAT TO DO?
        val bet4 = Bet(1000)
        assert(bet4.isLegal(state))

        //TODO: NOT ENOUGH CHIPS EXCEPTION
        val bet5 = Bet(1001)
        assert(!bet5.isLegal(state))
    }

    @Test
    fun `bet should be legal only if a player bet above minRaise`() {
        val player0 = Player(position = 0, currentBet = 0, stack = startingStack)
        val player1 = Player(position = 1, currentBet = 200, stack = startingStack - 200)
        val player2 = Player(position = 2, currentBet = 0, stack = startingStack) // active player
        val player3 = Player(position = 3, currentBet = 0, stack = startingStack)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1,
                bettingRound = BettingRound.FLOP,
                currentBet = 200,
                minRaise = 200
        )

        val bet1 = Bet(400)
        assert(bet1.isLegal(state))

        //TODO: NOT ABOVE MIN RAISE EXCEPTION
        val bet2 = Bet(399)
        assert(!bet2.isLegal(state))
    }

    @Test
    fun `bet should be illegal if a player has already made a bet after pre flop`() {
        val player0 = Player(position = 0, currentBet = 0, stack = startingStack)
        val player1 = Player(position = 1, currentBet = 400, stack = startingStack - 400)
        val player2 = Player(position = 2, currentBet = 200, stack = startingStack - 200) // active player
        val player3 = Player(position = 3, currentBet = 0, stack = startingStack, folded = true)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player1,
                bettingRound = BettingRound.FLOP,
                currentBet = 400,
                minRaise = 400
        )

        // BET ILLEGAL -> SHOULD APPLY RAISE, TODO: SHOULD_RAISE EXCEPTION
        val bet1 = Bet(200)
        assert(!bet1.isLegal(state))
    }

    @Test
    fun `bet should be illegal as bigBlind in pre flop`() {
        val player0 = Player(position = 0, currentBet = 0, stack = startingStack)
        val player1 = Player(position = 1, currentBet = blindsMock.bigBlind + 200, stack = 1000)
        val player2 = Player(position = 2, currentBet = blindsMock.bigBlind, stack = 1000) // active player / big blind
        val player3 = Player(position = 3, currentBet = blindsMock.bigBlind + 200, stack = 1000)

        val state = HandState(
                players = listOf(player0, player1, player2, player3),
                blinds = blindsMock,
                buttonPosition = 0,
                activePlayer = player2,
                lastAggressor = player3,
                bettingRound = BettingRound.PRE_FLOP,
                currentBet = blindsMock.bigBlind + 200
        )

        val check = Check()
        assert(!check.isLegal(state))
    }



}
