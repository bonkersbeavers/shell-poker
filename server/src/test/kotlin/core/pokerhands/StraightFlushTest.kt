package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StraightFlushTest {

    private val strongest = StraightFlush(
            listOf(
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.HEARTS),
                    Card(CardRank.NINE, CardSuit.HEARTS)
            )
    )

    private val mediocre1 = StraightFlush(
            listOf(
                    Card(CardRank.QUEEN, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.NINE, CardSuit.SPADES),
                    Card(CardRank.EIGHT, CardSuit.SPADES)
            )
    )

    private val mediocre1same = StraightFlush(
            listOf(
                    Card(CardRank.QUEEN, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.CLUBS),
                    Card(CardRank.NINE, CardSuit.CLUBS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS)
            )
    )

    private val weakest = Straight(
            listOf(
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.FOUR, CardSuit.HEARTS),
                    Card(CardRank.THREE, CardSuit.HEARTS),
                    Card(CardRank.TWO, CardSuit.HEARTS),
                    Card(CardRank.ACE, CardSuit.HEARTS)
            )
    )

    @Test
    fun `StraightFlush 'greater than' comparision should be true when first hand is stronger`() {
        assert(strongest > mediocre1)
        assert(mediocre1 > weakest)
        assert(strongest > weakest)
    }

    @Test
    fun `StraightFlush compareTo should be 0 when both hands are equally strong`() {
        assert(mediocre1.compareTo(mediocre1same) == 0)
    }

    @Test
    fun `StraightFlush instantiation should fail if there is no straight flush on hand`() {
        assertThrows<AssertionError> {
            StraightFlush(
                    listOf(
                            Card(CardRank.QUEEN, CardSuit.CLUBS),
                            Card(CardRank.JACK, CardSuit.DIAMONDS),
                            Card(CardRank.TEN, CardSuit.CLUBS),
                            Card(CardRank.NINE, CardSuit.CLUBS),
                            Card(CardRank.EIGHT, CardSuit.CLUBS)
                    )
            )
        }

        assertThrows<AssertionError> {
            StraightFlush(
                    listOf(
                            Card(CardRank.SIX, CardSuit.HEARTS),
                            Card(CardRank.FOUR, CardSuit.HEARTS),
                            Card(CardRank.THREE, CardSuit.HEARTS),
                            Card(CardRank.TWO, CardSuit.HEARTS),
                            Card(CardRank.ACE, CardSuit.HEARTS)
                    )
            )
        }
    }
}
