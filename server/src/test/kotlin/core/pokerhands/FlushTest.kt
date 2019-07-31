package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlushTest {

    private val strongest = Flush(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.QUEEN, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.SPADES),
                    Card(CardRank.FOUR, CardSuit.SPADES)
            )
    )

    private val mediocre1 = Flush(
            listOf(
                    Card(CardRank.KING, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.QUEEN, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.SPADES),
                    Card(CardRank.FOUR, CardSuit.SPADES)
            )
    )

    private val mediocre2 = Flush(
            listOf(
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.HEARTS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.FOUR, CardSuit.HEARTS)
            )
    )

    private val weakerByOneKicker = Flush(
            listOf(
                    Card(CardRank.ACE, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.CLUBS),
                    Card(CardRank.QUEEN, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.THREE, CardSuit.CLUBS)
            )
    )

    @Test
    fun `Flush 'greater than' comparision should be true when first hand is stronger`() {
        assert(strongest > weakerByOneKicker)
        assert(strongest > mediocre1)
    }

    @Test
    fun `Flush compareTo should return 0 when both hands contain equally strong kickers`() {
        assert(mediocre1.compareTo(mediocre2) == 0)
    }

    @Test
    fun `Flush instantiation should fail when cards are not single suited`() {
        assertThrows<AssertionError> {
            Flush(
                    listOf(
                            Card(CardRank.FIVE, CardSuit.CLUBS),
                            Card(CardRank.ACE, CardSuit.CLUBS),
                            Card(CardRank.JACK, CardSuit.DIAMONDS),
                            Card(CardRank.TWO, CardSuit.HEARTS),
                            Card(CardRank.EIGHT, CardSuit.CLUBS)
                    )
            )
        }
    }
}
