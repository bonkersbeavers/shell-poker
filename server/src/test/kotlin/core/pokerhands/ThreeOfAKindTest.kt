package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ThreeOfAKindTest {

    private val strongThree = ThreeOfAKind(
            setOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.DIAMONDS),
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.KING, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.CLUBS)
            )
    )

    private val weakThreeStrongKicker = ThreeOfAKind(
            setOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.CLUBS)
            )
    )

    private val weakThreeWeakKicker = ThreeOfAKind(
            setOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.THREE, CardSuit.DIAMONDS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS)
            )
    )

    private val weakThreeWeakKicker2 = ThreeOfAKind(
            setOf(
                    Card(CardRank.FIVE, CardSuit.CLUBS),
                    Card(CardRank.EIGHT, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.THREE, CardSuit.CLUBS)
            )
    )

    @Test
    fun `Three of a kind 'greater than' comparison should be true when first hand has stronger three`() {
        assert(strongThree > weakThreeStrongKicker)
    }

    @Test
    fun `Three of a kind 'greater than' comparison should be true when first hand has better kickers`() {
        assert(weakThreeStrongKicker > weakThreeWeakKicker)
    }

    @Test
    fun `Three of a kind compareTo should be 0 when both hands contain equally strong three and kickers`() {
        assert(weakThreeWeakKicker.compareTo(weakThreeWeakKicker2) == 0)
    }

    @Test
    fun `Three of a kind instantiation should fail if there are no three cards with the same rank in the hand`() {
        assertThrows<AssertionError> {
            ThreeOfAKind(
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
