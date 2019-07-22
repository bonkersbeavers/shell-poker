package core.gameflow

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerTest {

    private val testHoleCards = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.TEN, CardSuit.SPADES)
    )

    private val testStack = 1000

    private val testCurrentBet = 700

    private val testPlayer = Player(
            position = 0,
            stack = testStack,
            holeCards = testHoleCards,
            currentBet = testCurrentBet)

    @Test
    fun `Player afterAllIn method should transfer all chips from chipsInStack to currentBet`() {
        val playerAfterAllIn = testPlayer.afterAllIn()
        assert(playerAfterAllIn.stack == 0 && playerAfterAllIn.currentBet == testStack + testCurrentBet)
    }

    @Test
    fun `Player isAllIn method should return true when player is all in`() {
        val playerAfterAllIn = testPlayer.afterAllIn()
        assert(playerAfterAllIn.isAllIn())
    }

    @Test
    fun `Player inGame method should return true when player is not all in nor has folded`() {
        val playerAfterAllIn = testPlayer.afterAllIn()
        assert(!playerAfterAllIn.inGame())

        val playerAfterFold = testPlayer.afterFold()
        assert(!playerAfterFold.inGame())
    }

    @Test
    fun `Player inGame method should return false when player is all in or has folded`() {
        assert(testPlayer.inGame())
    }

    @Test
    fun `Player afterRaise method should transfer proper amount of chips from stack to currentBet`() {
        val testBetSize = 500
        val playerWithBet = testPlayer.afterRaise(testBetSize)
        assert(playerWithBet.stack == testStack - testBetSize && playerWithBet.currentBet == testBetSize + testCurrentBet)
    }

    @Test
    fun `Player afterRaise method throw NotEnoughChipsException when betSize is larger than stack`() {
        val testBetSize = 1001

        assertThrows<NotEnoughChipsException> {
            testPlayer.afterRaise(testBetSize)
        }
    }

    @Test
    fun `Player afterCall method should appply allIn when there are not enough chips in stack to call`() {
        val testBetSize = 2000

        val playerAfterCall = testPlayer.afterCall(testBetSize)
        assert(playerAfterCall.isAllIn())
    }

    @Test
    fun `Player afterCall method should appply withBet when there are enough chips in stack to call`() {
        val testBetSize = 1500
        val amountToCall = testBetSize - testPlayer.currentBet

        val playerAfterCall = testPlayer.afterCall(testBetSize)
        assert(playerAfterCall.stack == testStack - amountToCall && playerAfterCall.currentBet == testBetSize)
    }
}
