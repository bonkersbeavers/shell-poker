package core.gameflow
import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
//import java.lang.AssertionError

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DealerTest {
    private val seed = 123

    @Test
    fun `Dealer should shuffle deck and reset its iterator`() {
        val dealer = Dealer()
        dealer.shuffle(123)

        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = player1
        )

        val newState = dealer.dealHoleCards(state)

        assert(newState.players[0].holeCards == listOf(
                Card(CardRank.TEN, CardSuit.SPADES),
                Card(CardRank.SEVEN, CardSuit.DIAMONDS)
        ))

        assert(newState.players[1].holeCards == listOf(
                Card(CardRank.QUEEN, CardSuit.CLUBS),
                Card(CardRank.EIGHT, CardSuit.SPADES)
        ))

        assert(newState.players[2].holeCards == listOf(
                Card(CardRank.SEVEN, CardSuit.SPADES),
                Card(CardRank.SIX, CardSuit.HEARTS)
        ))


        val newSeed = 234
        dealer.shuffle(newSeed)

        val newState2 = dealer.dealHoleCards(state)

        assert(newState2.players[0].holeCards == listOf(
                Card(CardRank.SEVEN, CardSuit.DIAMONDS),
                Card(CardRank.JACK, CardSuit.DIAMONDS)
        ))

        assert(newState2.players[1].holeCards == listOf(
                Card(CardRank.TEN, CardSuit.HEARTS),
                Card(CardRank.TEN, CardSuit.CLUBS)
        ))

        assert(newState2.players[2].holeCards == listOf(
                Card(CardRank.FOUR, CardSuit.SPADES),
                Card(CardRank.FIVE, CardSuit.DIAMONDS)
        ))
    }
}
