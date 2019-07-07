package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StraightTest {

    private val strongest = Straight(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.DIAMONDS)
            )
    )

    private val mediocre1 = Straight(
            listOf(
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.DIAMONDS),
                    Card(CardRank.NINE, CardSuit.SPADES)
            )
    )

    private val mediocre2 = Straight(
            listOf(
                    Card(CardRank.SEVEN, CardSuit.HEARTS),
                    Card(CardRank.SIX, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.FOUR, CardSuit.DIAMONDS),
                    Card(CardRank.THREE, CardSuit.SPADES)
            )
    )

    private val weakest = Straight(
            listOf(
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.FOUR, CardSuit.SPADES),
                    Card(CardRank.THREE, CardSuit.CLUBS),
                    Card(CardRank.TWO, CardSuit.DIAMONDS),
                    Card(CardRank.ACE, CardSuit.SPADES)
            )
    )

    @Test
    fun `Straight 'greater than' comparision should be true when first hand is stronger`() {
        assert(strongest > mediocre1)
        assert(mediocre2 > weakest)
        assert(strongest > weakest)
    }
}