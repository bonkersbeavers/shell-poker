package core.gameflow
import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.cards.baseDeck
import core.gameflow.dealer.Dealer
import core.gameflow.handstate.HandState
import core.gameflow.handstate.rebuild
import core.gameflow.player.Player
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DealerTest {
    private val seed = 123

    @Test
    fun `Dealer should shuffle deck and reset its iterator`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = null,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val dealer = Dealer()
        dealer.shuffle(seed)

        val newState = dealer.dealHoleCards(state)

        assert(newState.players[0].holeCards == listOf(
                Card(CardRank.JACK, CardSuit.CLUBS),
                Card(CardRank.EIGHT, CardSuit.HEARTS)
        ))

        assert(newState.players[1].holeCards == listOf(
                Card(CardRank.KING, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.CLUBS)
        ))

        assert(newState.players[2].holeCards == listOf(
                Card(CardRank.EIGHT, CardSuit.CLUBS),
                Card(CardRank.SEVEN, CardSuit.SPADES)
        ))

        val newSeed = 234
        dealer.shuffle(newSeed)

        val newState2 = dealer.dealHoleCards(state)

        assert(newState2.players[0].holeCards == listOf(
                Card(CardRank.EIGHT, CardSuit.HEARTS),
                Card(CardRank.QUEEN, CardSuit.HEARTS)
        ))

        assert(newState2.players[1].holeCards == listOf(
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.JACK, CardSuit.DIAMONDS)
        ))

        assert(newState2.players[2].holeCards == listOf(
                Card(CardRank.FIVE, CardSuit.CLUBS),
                Card(CardRank.SIX, CardSuit.HEARTS)
        ))
    }

    @Test
    fun `automatic deal should properly determine which street should be dealt`() {
        val players = listOf(
                Player(position = 0, stack = 500),
                Player(position = 1, stack = 500),
                Player(position = 2, stack = 500)
        )

        val initialState = HandState.ImmutableBuilder(
                players = players,
                blinds = Blinds(10, 20),
                positions = Positions(0, 1, 2),
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val dealer = Dealer()
        dealer.shuffle()

        val preFlopState = dealer.deal(initialState)
        val flopState = dealer.deal(preFlopState.rebuild(bettingRound = BettingRound.FLOP))
        val turnState = dealer.deal(flopState.rebuild(bettingRound = BettingRound.TURN))
        val riverState = dealer.deal(turnState.rebuild(bettingRound = BettingRound.RIVER))

        assert(preFlopState.players.all { it.holeCards.size == 2 })
        assert(preFlopState.communityCards.isEmpty())

        assert(flopState.communityCards.size == 3)
        assert(turnState.communityCards.size == 4)
        assert(riverState.communityCards.size == 5)
    }

    @Test
    fun `Dealer setColdDeck should set a pre arranged deck as a deckIterator`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = null,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        val newState = dealer.dealHoleCards(state)

        assert(newState.players[0].holeCards == listOf(
                Card(CardRank.TWO, CardSuit.SPADES),
                Card(CardRank.THREE, CardSuit.SPADES)
        ))

        assert(newState.players[1].holeCards == listOf(
                Card(CardRank.FOUR, CardSuit.SPADES),
                Card(CardRank.FIVE, CardSuit.SPADES)
        ))

        assert(newState.players[2].holeCards == listOf(
                Card(CardRank.SIX, CardSuit.SPADES),
                Card(CardRank.SEVEN, CardSuit.SPADES)
        ))
    }

    @Test
    fun `Dealer deal streets methods should act accordingly`() {
        val player0 = Player(position = 0, stack = 0)
        val player1 = Player(position = 1, stack = 0)
        val player2 = Player(position = 2, stack = 0)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                activePlayer = null,
                bettingRound = BettingRound.PRE_FLOP
        ).build()

        val dealer = Dealer()
        dealer.setColdDeck(baseDeck)

        val newState = dealer.dealHoleCards(state)

        val updatedBettingRoundState1 = newState.rebuild(bettingRound = BettingRound.FLOP)

        val afterFlopState = dealer.dealFlop(updatedBettingRoundState1)

        assert(afterFlopState.communityCards == listOf(
                Card(CardRank.EIGHT, CardSuit.SPADES),
                Card(CardRank.NINE, CardSuit.SPADES),
                Card(CardRank.TEN, CardSuit.SPADES)
        ))

        val updatedBettingRoundState2 = afterFlopState.rebuild(bettingRound = BettingRound.TURN)

        val afterTurnState = dealer.dealTurn(updatedBettingRoundState2)

        assert(afterTurnState.communityCards == listOf(
                Card(CardRank.EIGHT, CardSuit.SPADES),
                Card(CardRank.NINE, CardSuit.SPADES),
                Card(CardRank.TEN, CardSuit.SPADES),
                Card(CardRank.JACK, CardSuit.SPADES)
        ))

        val updatedBettingRoundState3 = afterTurnState.rebuild(bettingRound = BettingRound.RIVER)

        val afterRiverState = dealer.dealRiver(updatedBettingRoundState3)

        assert(afterRiverState.communityCards == listOf(
                Card(CardRank.EIGHT, CardSuit.SPADES),
                Card(CardRank.NINE, CardSuit.SPADES),
                Card(CardRank.TEN, CardSuit.SPADES),
                Card(CardRank.JACK, CardSuit.SPADES),
                Card(CardRank.QUEEN, CardSuit.SPADES)
        ))
    }

    @Test
    fun `Dealer set cold deck should only set a valid deck`() {
        val dealer = Dealer()

        assertThrows<AssertionError> {
            dealer.setColdDeck(baseDeck + Card(CardRank.ACE, CardSuit.SPADES))
        }
    }
}
