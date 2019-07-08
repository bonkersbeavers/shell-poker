package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PokerHandTest {

    private val validHand = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS),
            Card(CardRank.TWO, CardSuit.HEARTS),
            Card(CardRank.TWO, CardSuit.SPADES),
            Card(CardRank.FIVE, CardSuit.CLUBS)
    )

    @Test
    fun `constructor assertion should succeed for 5 distinct cards`() {
        HighCard(validHand)
    }

    private val fourCardHand = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS),
            Card(CardRank.TWO, CardSuit.HEARTS),
            Card(CardRank.TWO, CardSuit.SPADES)
    )

    @Test
    fun `constructor assertion should fail if there are 4 cards`() {
        assertThrows<AssertionError> {
            HighCard(fourCardHand)
        }
    }

    private val repeatedCardsHand = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS),
            Card(CardRank.TWO, CardSuit.HEARTS),
            Card(CardRank.TWO, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS)
    )

    @Test
    fun `constructor assertion should fail if there are any repeated cards`() {
        assertThrows<AssertionError> {
            HighCard(repeatedCardsHand)
        }
    }
}
