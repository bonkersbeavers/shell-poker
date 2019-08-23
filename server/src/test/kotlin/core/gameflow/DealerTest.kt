package core.gameflow
import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.cards.baseDeck
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DealerTest {
    private val seed = 123

    @Test
    fun `Dealer should shuffle deck and reset its iterator`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null
        )

        val dealer = Dealer()
        dealer.shuffle(seed)

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

    @Test
    fun `Dealer setColdDeck should set a pre arranged deck as a deckIterator`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null
        )

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        val newState = dealer.dealHoleCards(state)

        assert(newState.players[0].holeCards == listOf(
                Card(CardRank.ACE, CardSuit.HEARTS),
                Card(CardRank.TWO, CardSuit.HEARTS)
        ))

        assert(newState.players[1].holeCards == listOf(
                Card(CardRank.THREE, CardSuit.HEARTS),
                Card(CardRank.FOUR, CardSuit.HEARTS)
        ))

        assert(newState.players[2].holeCards == listOf(
                Card(CardRank.FIVE, CardSuit.HEARTS),
                Card(CardRank.SIX, CardSuit.HEARTS)
        ))
    }

    @Test
    fun `Dealer deal streets methods should act accordingly`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null
        )

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        val newState = dealer.dealHoleCards(state)

        val afterFlopState = dealer.dealFlop(newState)

        assert(afterFlopState.communityCards == listOf(
                Card(CardRank.SEVEN, CardSuit.HEARTS),
                Card(CardRank.EIGHT, CardSuit.HEARTS),
                Card(CardRank.NINE, CardSuit.HEARTS)
        ))

        val afterTurnState = dealer.dealTurn(afterFlopState)

        assert(afterTurnState.communityCards == listOf(
                Card(CardRank.SEVEN, CardSuit.HEARTS),
                Card(CardRank.EIGHT, CardSuit.HEARTS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.TEN, CardSuit.HEARTS)
        ))

        val afterRiverState = dealer.dealRiver(afterTurnState)

        assert(afterRiverState.communityCards == listOf(
                Card(CardRank.SEVEN, CardSuit.HEARTS),
                Card(CardRank.EIGHT, CardSuit.HEARTS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.TEN, CardSuit.HEARTS),
                Card(CardRank.JACK, CardSuit.HEARTS)
        ))
    }

    @Test
    fun `Dealer should only deal cards if all players hole cards are empty`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0, holeCards = listOf(
                Card(CardRank.SEVEN, CardSuit.HEARTS),
                Card(CardRank.EIGHT, CardSuit.HEARTS)
        ))
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null
        )

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        assertThrows<AssertionError> {
            dealer.dealHoleCards(state)
        }
    }

    @Test
    fun `Dealer should only deal flop if community cards are empty`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null,
                communityCards = listOf(Card(CardRank.ACE, CardSuit.SPADES))
        )

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        assertThrows<AssertionError> {
            dealer.dealFlop(state)
        }
    }

    @Test
    fun `Dealer should only deal turn if there are exactly 3 community cards and river if there are 4`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                buttonPosition = 0,
                activePlayer = null,
                communityCards = listOf(Card(CardRank.ACE, CardSuit.SPADES))
        )

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        assertThrows<AssertionError> {
            dealer.dealTurn(state)
            dealer.dealRiver(state)
        }
    }

    @Test
    fun `Dealer set cold deck should only set a valid deck`() {
        val dealer = Dealer()

        assertThrows<AssertionError> {
            dealer.setColdDeck(baseDeck + Card(CardRank.ACE, CardSuit.SPADES))
        }
    }
}
