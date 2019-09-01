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
    fun `toPokerHand should throw an exception if cards number is different than 5`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS)
        )

        assertThrows<AssertionError> {
            cards.toPokerHand()
        }
    }

    @Test
    fun `toPokerHand should properly identify high card`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.FIVE, CardSuit.SPADES)
        )

        assert(cards.toPokerHand() is HighCard)
    }

    @Test
    fun `toPokerHand should properly identify pair`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.SPADES)
        )

        assert(cards.toPokerHand() is Pair)
    }

    @Test
    fun `toPokerHand should properly identify two pair`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.QUEEN, CardSuit.SPADES)
        )

        assert(cards.toPokerHand() is TwoPair)
    }

    @Test
    fun `toPokerHand should properly identify three of a kind`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.SPADES)
        )

        assert(cards.toPokerHand() is ThreeOfAKind)
    }

    @Test
    fun `toPokerHand should properly identify straight`() {
        val cards1 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.TEN, CardSuit.SPADES)
        )

        assert(cards1.toPokerHand() is Straight)

        val cards2 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.THREE, CardSuit.SPADES),
                Card(CardRank.TWO, CardSuit.DIAMONDS),
                Card(CardRank.FOUR, CardSuit.DIAMONDS),
                Card(CardRank.FIVE, CardSuit.SPADES)
        )

        assert(cards2.toPokerHand() is Straight)
    }

    @Test
    fun `toPokerHand should properly identify flush`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.KING, CardSuit.DIAMONDS),
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.DIAMONDS)
        )

        assert(cards.toPokerHand() is Flush)
    }

    @Test
    fun `toPokerHand should properly identify full house`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.HEARTS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.ACE, CardSuit.CLUBS)
        )

        assert(cards.toPokerHand() is FullHouse)
    }

    @Test
    fun `toPokerHand should properly identify four of a kind`() {
        val cards = setOf(
                Card(CardRank.JACK, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.HEARTS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.ACE, CardSuit.CLUBS)
        )

        assert(cards.toPokerHand() is FourOfAKind)
    }

    @Test
    fun `toPokerHand should properly identify straight flush`() {
        val cards1 = setOf(
                Card(CardRank.NINE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.TEN, CardSuit.CLUBS)
        )

        assert(cards1.toPokerHand() is StraightFlush)

        val cards2 = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.FOUR, CardSuit.CLUBS),
                Card(CardRank.FIVE, CardSuit.CLUBS)
        )

        assert(cards2.toPokerHand() is StraightFlush)
    }

    @Test
    fun `toPokerHand should properly identify royal flush`() {
        val cards = setOf(
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.CLUBS),
                Card(CardRank.QUEEN, CardSuit.CLUBS),
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.TEN, CardSuit.CLUBS)
        )

        assert(cards.toPokerHand() is RoyalFlush)
    }

    @Test
    fun `best5CardHand should create the best poker hand out of given set of cards`() {

        val cards = setOf(
                Card(CardRank.JACK, CardSuit.HEARTS),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.DIAMONDS),
                Card(CardRank.SIX, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.SIX, CardSuit.HEARTS)
        )

        val properHandCards = setOf(
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.TWO, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.DIAMONDS),
                Card(CardRank.SIX, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.HEARTS)
        )

        val hand = cards.best5CardHand()

        assert(hand is TwoPair)
        assert(hand.cards == properHandCards)
    }

    @Test
    fun `best5CardHand should create one of the best hands if multiple choices are possible`() {

        val cards = setOf(
                Card(CardRank.ACE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.DIAMONDS),
                Card(CardRank.SIX, CardSuit.CLUBS),
                Card(CardRank.KING, CardSuit.DIAMONDS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.SIX, CardSuit.HEARTS)
        )

        val possibleProperHand = FullHouse(setOf(
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.ACE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.HEARTS)
        ))

        assert(cards.best5CardHand().compareTo(possibleProperHand) == 0)
    }

    @Test
    fun `calling best5CardHand should throw an exception if the set size is smaller than 5`() {

        val cards = setOf(
                Card(CardRank.ACE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.DIAMONDS),
                Card(CardRank.SIX, CardSuit.CLUBS)
        )

        assertThrows<AssertionError> { cards.best5CardHand() }
    }
}
