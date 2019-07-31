package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FullHouseTest {

    private val strongFullHouse = FullHouse(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.DIAMONDS),
                    Card(CardRank.ACE, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.ACE, CardSuit.CLUBS)
            )
    )

    private val mediocreFullHouse = FullHouse(
            listOf(
                    Card(CardRank.JACK, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.SPADES),
                    Card(CardRank.TEN, CardSuit.CLUBS)
            )
    )

    private val weakFullHouse = FullHouse(
            listOf(
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.TWO, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS)
            )
    )

    private val weakFullHouse2 = FullHouse(
            listOf(
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.TWO, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.CLUBS)
            )
    )

    @Test
    fun `FullHouse 'greater than' comparison should be true when first hand has stronger set`() {
        assert(strongFullHouse > mediocreFullHouse)
    }

    @Test
    fun `FullHouse 'greater than' comparison should be true when both sets are equal and first hand has stronger pair`() {
        assert(mediocreFullHouse > weakFullHouse)
    }

    @Test
    fun `FullHouse compareTo should be 0 when both hands contain equally strong sets and pairs`() {
        assert(weakFullHouse.compareTo(weakFullHouse2) == 0)
    }

    @Test
    fun `FullHouse instantiation should fail if there are no pair and set in the hand`() {
        assertThrows<AssertionError> {
            FullHouse(
                    listOf(
                            Card(CardRank.FIVE, CardSuit.CLUBS),
                            Card(CardRank.ACE, CardSuit.DIAMONDS),
                            Card(CardRank.JACK, CardSuit.DIAMONDS),
                            Card(CardRank.ACE, CardSuit.HEARTS),
                            Card(CardRank.ACE, CardSuit.CLUBS)
                    )
            )
        }
    }
}
