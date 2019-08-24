package core.pokerhands

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoyalFlushTest {

    private val first = RoyalFlush(
            setOf(
                    Card(CardRank.ACE, CardSuit.HEARTS),
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.HEARTS)
            )
    )

    private val second = RoyalFlush(
            setOf(
                    Card(CardRank.ACE, CardSuit.CLUBS),
                    Card(CardRank.KING, CardSuit.CLUBS),
                    Card(CardRank.QUEEN, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.CLUBS),
                    Card(CardRank.TEN, CardSuit.CLUBS)
            )
    )

    @Test
    fun `RoyalFlush compareTo should be 0 always`() {
        assert(first.compareTo(second) == 0)
    }

    @Test
    fun `StraightFlush instantiation should fail if there is no royal flush on hand`() {
        assertThrows<AssertionError> {
            RoyalFlush(
                    setOf(
                            Card(CardRank.KING, CardSuit.HEARTS),
                            Card(CardRank.QUEEN, CardSuit.HEARTS),
                            Card(CardRank.JACK, CardSuit.HEARTS),
                            Card(CardRank.TEN, CardSuit.HEARTS),
                            Card(CardRank.NINE, CardSuit.HEARTS)
                    )
            )
        }

        assertThrows<AssertionError> {
            RoyalFlush(
                    setOf(
                            Card(CardRank.ACE, CardSuit.DIAMONDS),
                            Card(CardRank.KING, CardSuit.CLUBS),
                            Card(CardRank.QUEEN, CardSuit.CLUBS),
                            Card(CardRank.JACK, CardSuit.CLUBS),
                            Card(CardRank.TEN, CardSuit.CLUBS)
                    )
            )
        }
    }
}
