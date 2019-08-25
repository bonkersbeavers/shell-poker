package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PokerHandTest {

    private val validHand = setOf(
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

    private val fourCardHand = setOf(
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

    private val repeatedCardsHand = setOf(
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
