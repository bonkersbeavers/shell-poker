package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PairTest {

    private val strongPair = Pair(
            setOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.KING, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.CLUBS)
            )
    )

    private val weakPairStrongKicker = Pair(
            setOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.CLUBS)
            )
    )

    private val weakPairWeakKicker = Pair(
            setOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.ACE, CardSuit.DIAMONDS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.TWO, CardSuit.HEARTS)
            )
    )

    private val weakPairWeakKicker2 = Pair(
            setOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.ACE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.TWO, CardSuit.HEARTS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS)
            )
    )

    @Test
    fun `Pair 'greater than' comparison should be true when first hand has stronger pair`() {
        assert(strongPair > weakPairStrongKicker)
    }

    @Test
    fun `Pair 'greater than' comparison should be true when first hand has better kickers`() {
        assert(weakPairStrongKicker > weakPairWeakKicker)
    }

    @Test
    fun `Pair compareTo should be 0 when both hands contain equally strong pair and kickers`() {
        assert(weakPairWeakKicker.compareTo(weakPairWeakKicker2) == 0)
    }

    @Test
    fun `Pair instantiation should fail if there is no pair in the hand`() {
        assertThrows<AssertionError> {
            Pair(
                    setOf(
                            Card(CardRank.FIVE, CardSuit.CLUBS),
                            Card(CardRank.ACE, CardSuit.DIAMONDS),
                            Card(CardRank.JACK, CardSuit.DIAMONDS),
                            Card(CardRank.TWO, CardSuit.HEARTS),
                            Card(CardRank.EIGHT, CardSuit.CLUBS)
                    )
            )
        }
    }
}
