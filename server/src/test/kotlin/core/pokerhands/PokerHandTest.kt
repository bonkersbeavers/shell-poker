package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

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
    fun `isValidHand should be true for 5 distinct cards`() {
        assert(isValidHand(validHand))
    }

    private val fourCardHand = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS),
            Card(CardRank.TWO, CardSuit.HEARTS),
            Card(CardRank.TWO, CardSuit.SPADES)
    )

    @Test
    fun `isValidHand should be false if there are 4 cards`() {
        assert(!isValidHand(fourCardHand))
    }

    private val repeatedCardsHand = listOf(
            Card(CardRank.ACE, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS),
            Card(CardRank.TWO, CardSuit.HEARTS),
            Card(CardRank.TWO, CardSuit.SPADES),
            Card(CardRank.ACE, CardSuit.DIAMONDS)
    )

    @Test
    fun `isValidHand should be false if there are any repeated cards`() {
        assert(!isValidHand(repeatedCardsHand))
    }
}