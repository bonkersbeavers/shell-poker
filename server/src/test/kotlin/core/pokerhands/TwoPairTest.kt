package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TwoPairTest {

    private val strongTwoPair = TwoPair(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS)
            )
    )

    private val weakTwoPairStrongKicker = TwoPair(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS)
            )
    )

    private val weakTwoPairWeakKicker = TwoPair(
            listOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.HEARTS)
            )
    )

    private val weakTwoPairWeakKicker2 = TwoPair(
            listOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS)
            )
    )

    @Test
    fun `TwoPair 'greater than' comparison should be true when first hand has stronger pair`() {
        assert(strongTwoPair > weakTwoPairStrongKicker)
    }

    @Test
    fun `TwoPair 'greater than' comparison should be true when first hand has better kickers`() {
        assert(weakTwoPairStrongKicker > weakTwoPairWeakKicker)
    }

    @Test
    fun `TwoPair compareTo should be 0 when both hands contain equally strong pairs and kickers`() {
        assert(weakTwoPairWeakKicker.compareTo(weakTwoPairWeakKicker2) == 0)
    }

    @Test
    fun `TwoPair instantiation should fail if there are no two pairs in the hand`() {
        assertThrows<AssertionError> {
            TwoPair(
                    listOf(
                            Card(CardRank.FIVE, CardSuit.CLUBS),
                            Card(CardRank.ACE, CardSuit.DIAMONDS),
                            Card(CardRank.JACK, CardSuit.DIAMONDS),
                            Card(CardRank.KING, CardSuit.HEARTS),
                            Card(CardRank.ACE, CardSuit.CLUBS)
                    )
            )
        }
    }
}
