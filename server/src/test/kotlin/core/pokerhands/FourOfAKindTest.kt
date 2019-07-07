package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FourOfAKindTest {

    private val strongest = FourOfAKind(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.ACE, CardSuit.HEARTS),
                    Card(CardRank.ACE, CardSuit.DIAMONDS),
                    Card(CardRank.ACE, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.DIAMONDS)
            )
    )

    private val mediocre1 = FourOfAKind(
            listOf(
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.KING, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.CLUBS),
                    Card(CardRank.KING, CardSuit.DIAMONDS),
                    Card(CardRank.NINE, CardSuit.SPADES)
            )
    )

    private val mediocre2 = FourOfAKind(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.ACE, CardSuit.HEARTS),
                    Card(CardRank.ACE, CardSuit.DIAMONDS),
                    Card(CardRank.ACE, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.DIAMONDS)
            )
    )


    @Test
    fun `FourOfAKind 'greater than' comparision should be true when first hand is stronger`() {
        assert(strongest > mediocre1)
        assert(strongest > mediocre2)
        assert(mediocre2 > mediocre1)
    }
}