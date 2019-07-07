package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoyalFlushTest {

    private val first = RoyalFlush(
            listOf(
                    Card(CardRank.ACE, CardSuit.HEARTS),
                    Card(CardRank.KING, CardSuit.HEARTS),
                    Card(CardRank.QUEEN, CardSuit.HEARTS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TEN, CardSuit.HEARTS)
            )
    )

    private val second = RoyalFlush(
            listOf(
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
                    listOf(
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
                    listOf(
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