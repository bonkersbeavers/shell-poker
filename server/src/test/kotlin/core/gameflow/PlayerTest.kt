package core.gameflow

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerTest {

    private val testHoleCards: List<Card> = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.TEN, CardSuit.SPADES)
    )

    private val testStack: Int = 1000

    private val testBetSize: Int = 500

    private val testCurrentBet: Int = 700

    private val testPlayer: Player = Player(
            position = 0,
            stack = testStack,
            holeCards = testHoleCards,
            chipsInPot = 0,
            currentBet = testCurrentBet)

    @Test
    fun `Player afterAllIn method should transfer all chips from chipsInStack to currentBet`() {
        val playerAfterAllIn: Player = testPlayer.afterAllIn()
        assert(playerAfterAllIn.stack == 0 && playerAfterAllIn.currentBet == testStack + testCurrentBet)
    }

    @Test
    fun `Player isAllIn method should return true when player is all in`() {
        val playerAfterAllIn: Player = testPlayer.afterAllIn()
        assert(playerAfterAllIn.isAllIn())
    }

    @Test
    fun `Player withBet method should transfer proper amount of chips from stack to currentBet`() {
        val playerWithBet: Player = testPlayer.withBet(testBetSize)
        assert(playerWithBet.stack == testStack - testBetSize && playerWithBet.currentBet == testBetSize + testCurrentBet)
    }

    @Test
    fun `Player withBet method throw NotEnoughChipsExcetpion when betSize is larger than stack`() {
        assertThrows<NotEnoughChipsException> {
            testPlayer.withBet(1001)
        }
    }
}
