package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.lang.AssertionError

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandUtilsTest {

    @Test
    fun `makeHand should throw an exception if cards number is different than 5`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS)
        )

        assertThrows<AssertionError> {
            makeHand(cards)
        }
    }

    @Test
    fun `makeHand should properly identify high card`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.FIVE, CardSuit.SPADES)
        )

        assert(makeHand(cards) is HighCard)
    }

    @Test
    fun `makeHand should properly identify pair`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.SPADES)
        )

        assert(makeHand(cards) is Pair)
    }

    @Test
    fun `makeHand should properly identify two pair`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.QUEEN, CardSuit.SPADES)
        )

        assert(makeHand(cards) is TwoPair)
    }

    @Test
    fun `makeHand should properly identify three of a kind`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.SPADES)
        )

        assert(makeHand(cards) is ThreeOfAKind)
    }

    @Test
    fun `makeHand should properly identify straight`() {
        val cards1 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.TEN, CardSuit.SPADES)
        )

        assert(makeHand(cards1) is Straight)

        val cards2 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.THREE, CardSuit.SPADES),
                Card(CardRank.TWO, CardSuit.DIAMONDS),
                Card(CardRank.FOUR, CardSuit.DIAMONDS),
                Card(CardRank.FIVE, CardSuit.SPADES)
        )

        assert(makeHand(cards2) is Straight)
    }

    @Test
    fun `makeHand should properly identify flush`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.KING, CardSuit.DIAMONDS),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.DIAMONDS)
        )

        assert(makeHand(cards) is Flush)
    }

    @Test
    fun `makeHand should properly identify full house`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.HEARTS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.ACE, CardSuit.CLUBS)
        )

        assert(makeHand(cards) is FullHouse)
    }

    @Test
    fun `makeHand should properly identify four of a kind`() {
        val cards = setOf(
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.HEARTS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.ACE, CardSuit.CLUBS)
        )

        assert(makeHand(cards) is FourOfAKind)
    }

    @Test
    fun `makeHand should properly identify straight flush`() {
        val cards1 = setOf(
                Card(CardRank.NINE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.TEN, CardSuit.CLUBS)
        )

        assert(makeHand(cards1) is StraightFlush)

        val cards2 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.FOUR, CardSuit.CLUBS),
                Card(CardRank.FIVE, CardSuit.CLUBS)
        )

        assert(makeHand(cards2) is StraightFlush)
    }

    @Test
    fun `makeHand should properly identify royal flush`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.TEN, CardSuit.CLUBS)
        )

        assert(makeHand(cards) is RoyalFlush)
    }
}