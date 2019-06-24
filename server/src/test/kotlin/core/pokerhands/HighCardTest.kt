package core.pokerhands

import core.Card
import core.CardRank
import core.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HighCardTest {

    private val strongerHand = HighCard(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.KING, CardSuit.DIAMONDS),
                    Card(CardRank.JACK, CardSuit.HEARTS),
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.FIVE, CardSuit.CLUBS)
            )
    )

    private val weakerHandA = HighCard(
            listOf(
                    Card(CardRank.ACE, CardSuit.SPADES),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.FIVE, CardSuit.HEARTS),
                    Card(CardRank.TWO, CardSuit.SPADES),
                    Card(CardRank.EIGHT, CardSuit.CLUBS)
            )
    )

    private val weakerHandB = HighCard(
            listOf(
                    Card(CardRank.ACE, CardSuit.CLUBS),
                    Card(CardRank.JACK, CardSuit.DIAMONDS),
                    Card(CardRank.EIGHT, CardSuit.CLUBS),
                    Card(CardRank.FIVE, CardSuit.DIAMONDS),
                    Card(CardRank.TWO, CardSuit.HEARTS)
            )
    )

    @Test
    fun `HighCard 'greater than' comparison should be true when first hand is stronger`() {
        assert(strongerHand > weakerHandA)
    }

    @Test
    fun `HighCard 'greater than or equal' comparison should be true when both hands contain equally strong kickers`() {
        assert(weakerHandA >= weakerHandB)
    }

    @Test
    fun `HighCard 'less than or equal' comparison should be true when both hands contain equally strong kickers`() {
        assert(weakerHandA <= weakerHandB)
    }
}