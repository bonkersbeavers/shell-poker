package core.gameflow

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.bettinground.ActionType
import core.gameflow.handstate.HandState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShowdownTest {

    private val mockBlinds = Blinds(50, 100)

    @Test
    fun `player can muck his cards if no other players are in the hand`() {
        val player0 = Player(position = 0, stack = 100, lastAction = ActionType.FOLD)
        val player1 = Player(position = 1, stack = 100, lastAction = ActionType.FOLD)
        val player2 = Player(position = 2, stack = 0, lastAction = ActionType.ALL_IN)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = mockBlinds,
                positions = Positions(0, 1, 2),
                bettingRound = BettingRound.TURN,
                communityCards = listOf(
                        Card(CardRank.ACE, CardSuit.HEARTS),
                        Card(CardRank.TEN, CardSuit.SPADES),
                        Card(CardRank.NINE, CardSuit.SPADES),
                        Card(CardRank.EIGHT, CardSuit.CLUBS)
                ),
                lastAggressor = player2,
                activePlayer = null
        ).build()

        val showdown = resolveShowdown(state)
        assert(showdown == listOf(MuckCards(player2.id)))
    }

    @Test
    fun `all players show their hands in proper order after all-ins before the river`() {
        val player0 = Player(position = 0, stack = 0, lastAction = ActionType.CALL)
        val player1 = Player(position = 1, stack = 0, lastAction = ActionType.ALL_IN)
        val player2 = Player(position = 2, stack = 0, lastAction = ActionType.CALL)

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2),
                blinds = mockBlinds,
                positions = Positions(0, 1, 2),
                bettingRound = BettingRound.TURN,
                communityCards = listOf(
                        Card(CardRank.ACE, CardSuit.HEARTS),
                        Card(CardRank.TEN, CardSuit.SPADES),
                        Card(CardRank.NINE, CardSuit.SPADES),
                        Card(CardRank.EIGHT, CardSuit.CLUBS)
                ),
                lastAggressor = player1,
                activePlayer = null
        ).build()

        val showdown = resolveShowdown(state)
        assert(showdown == listOf(
                ShowCards(player1.id),
                ShowCards(player2.id),
                ShowCards(player0.id)
        ))
    }

    @Test
    fun `players should show their cards only if they are winning in regular scenario`() {
        val player0 = Player(
                position = 0, stack = 500,
                chipsInPot = 500, lastAction = ActionType.CALL,
                holeCards = listOf(
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES)
                ) // flush
        )

        val player1 = Player(
                position = 1, stack = 500,
                chipsInPot = 500, lastAction = ActionType.BET,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.DIAMONDS),
                        Card(CardRank.JACK, CardSuit.SPADES)
                ) // straight
        )

        val player2 = Player(
                position = 2, stack = 500,
                chipsInPot = 500, lastAction = ActionType.CALL,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                ) // two pair
        )

        val player3 = Player(
                position = 3, stack = 500,
                chipsInPot = 500, lastAction = ActionType.FOLD
        ) // not in hand

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = mockBlinds,
                positions = Positions(3, 0, 1),
                bettingRound = BettingRound.RIVER,
                communityCards = listOf(
                        Card(CardRank.ACE, CardSuit.HEARTS),
                        Card(CardRank.TEN, CardSuit.SPADES),
                        Card(CardRank.NINE, CardSuit.SPADES),
                        Card(CardRank.EIGHT, CardSuit.CLUBS),
                        Card(CardRank.SEVEN, CardSuit.SPADES)
                ),
                lastAggressor = player1,
                activePlayer = null
        ).build()

        val showdown = resolveShowdown(state)
        assert(showdown == listOf(
                ShowCards(player1.id),
                MuckCards(player2.id),
                ShowCards(player0.id)
        ))
    }

    @Test
    fun `showdown order should be the same as betting order if no bet was made on the river`() {
        val player0 = Player(
                position = 0, stack = 500, lastAction = ActionType.CHECK,
                holeCards = listOf(
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES)
                ) // flush
        )

        val player1 = Player(
                position = 1, stack = 500, lastAction = ActionType.CHECK,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.DIAMONDS),
                        Card(CardRank.JACK, CardSuit.SPADES)
                ) // straight
        )

        val player2 = Player(
                position = 2, stack = 500, lastAction = ActionType.CHECK,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                ) // two pair
        )

        val player3 = Player(
                position = 3, stack = 500,
                chipsInPot = 500, lastAction = ActionType.FOLD
        ) // not in hand

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = mockBlinds,
                positions = Positions(3, 0, 1),
                bettingRound = BettingRound.RIVER,
                communityCards = listOf(
                        Card(CardRank.ACE, CardSuit.HEARTS),
                        Card(CardRank.TEN, CardSuit.SPADES),
                        Card(CardRank.NINE, CardSuit.SPADES),
                        Card(CardRank.EIGHT, CardSuit.CLUBS),
                        Card(CardRank.SEVEN, CardSuit.SPADES)
                ),
                lastAggressor = null,
                activePlayer = null
        ).build()

        val showdown = resolveShowdown(state)
        assert(showdown == listOf(
                ShowCards(player0.id),
                MuckCards(player1.id),
                MuckCards(player2.id)
        ))
    }

    @Test
    fun `players should show their cards even if they are losing, but to someone who put less chips into the pot`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 500, lastAction = ActionType.ALL_IN,
                holeCards = listOf(
                        Card(CardRank.SIX, CardSuit.CLUBS),
                        Card(CardRank.THREE, CardSuit.SPADES)
                ) // low straight
        )

        val player1 = Player(
                position = 1, stack = 200,
                chipsInPot = 700, lastAction = ActionType.CALL,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.DIAMONDS),
                        Card(CardRank.KING, CardSuit.SPADES)
                ) // pair
        )

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 700, lastAction = ActionType.ALL_IN,
                holeCards = listOf(
                        Card(CardRank.ACE, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                ) // two pair
        )

        val player3 = Player(
                position = 3, stack = 500,
                chipsInPot = 500, lastAction = ActionType.FOLD
        ) // not in hand

        val state = HandState.ImmutableBuilder(
                players = listOf(player0, player1, player2, player3),
                blinds = mockBlinds,
                positions = Positions(3, 0, 1),
                bettingRound = BettingRound.RIVER,
                communityCards = listOf(
                        Card(CardRank.ACE, CardSuit.HEARTS),
                        Card(CardRank.TEN, CardSuit.SPADES),
                        Card(CardRank.NINE, CardSuit.SPADES),
                        Card(CardRank.EIGHT, CardSuit.CLUBS),
                        Card(CardRank.SEVEN, CardSuit.SPADES)
                ),
                lastAggressor = player0,
                activePlayer = null
        ).build()

        val showdown = resolveShowdown(state)
        assert(showdown == listOf(
                ShowCards(player0.id),
                ShowCards(player1.id),
                ShowCards(player2.id)
        ))
    }
}
