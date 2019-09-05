package core.gameflow

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.bettinground.ActionType
import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PotTest {

    private val baseBuilder = HandState.ImmutableBuilder(
            blinds = Blinds(10, 20)
    )

    @Test
    fun `player with best hand should win the pot`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.FIVE, CardSuit.HEARTS), Card(CardRank.FIVE, CardSuit.SPADES)),
                lastAction = ActionType.BET
        ) // weak hand, plays for the whole pot

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 300,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.ACE, CardSuit.SPADES)),
                lastAction = ActionType.FOLD
        ) // not in game

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.KING, CardSuit.HEARTS), Card(CardRank.KING, CardSuit.SPADES)),
                lastAction = ActionType.CALL
        ) // strongest hand, plays for the whole pot

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2),
                positions = Positions(0, 1, 2),
                communityCards = listOf(
                        Card(CardRank.TWO, CardSuit.HEARTS),
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES),
                        Card(CardRank.SEVEN, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                )
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == sequenceOf(
                PotResult(player2.id, 1300) // player2 should win the whole pot
        ))
    }

    @Test
    fun `pot should be split if two players have equally strong hands, chips awarding order should be the same as betting order`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.BET
        ) // strongest hand, plays for the whole pot

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 300,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.ACE, CardSuit.SPADES)),
                lastAction = ActionType.FOLD
        ) // not in game

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.KING, CardSuit.HEARTS), Card(CardRank.KING, CardSuit.SPADES)),
                lastAction = ActionType.CALL
        ) // strongest hand, plays for the whole pot

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2),
                positions = Positions(0, 1, 2),
                communityCards = listOf(
                        Card(CardRank.TWO, CardSuit.HEARTS),
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES),
                        Card(CardRank.SEVEN, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                )
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == sequenceOf(
                PotResult(player2.id, 650), // player2 should win half of the pot
                PotResult(player0.id, 650) // player0 should win half of the pot
        ))
    }

    @Test
    fun `odd chips should be awarded in betting order if the pot cannot be split equally`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 400,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.BET
        ) // strongest hand, plays for the whole pot

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 400,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.DIAMONDS), Card(CardRank.QUEEN, CardSuit.CLUBS)),
                lastAction = ActionType.CALL
        ) // strongest hand, plays for the whole pot

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 400,
                holeCards = listOf(Card(CardRank.JACK, CardSuit.DIAMONDS), Card(CardRank.JACK, CardSuit.CLUBS)),
                lastAction = ActionType.CALL
        ) // strongest hand, plays for the whole pot

        val player3 = Player(
                position = 3, stack = 0,
                chipsInPot = 101,
                holeCards = listOf(Card(CardRank.TEN, CardSuit.DIAMONDS), Card(CardRank.TEN, CardSuit.CLUBS)),
                lastAction = ActionType.FOLD
        ) // not in game

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2, player3),
                positions = Positions(0, 1, 2),
                communityCards = listOf(
                        Card(CardRank.TWO, CardSuit.HEARTS),
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.TWO, CardSuit.CLUBS),
                        Card(CardRank.TWO, CardSuit.DIAMONDS),
                        Card(CardRank.ACE, CardSuit.SPADES)
                )
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == sequenceOf(
                PotResult(player1.id, 434), // player1 should win third of the pot + one chip
                PotResult(player2.id, 434), // player2 should win third of the pot + one chip
                PotResult(player0.id, 433) // player0 should win third of the pot
        ))
    }

    @Test
    fun `when there are side pots, they should be resolved from the last one to the main one`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 300,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.ALL_IN
        ) // strongest hand, plays for the main pot

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.FIVE, CardSuit.SPADES)),
                lastAction = ActionType.BET
        ) // weakest hand, plays for the whole pot

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.JACK, CardSuit.SPADES)),
                lastAction = ActionType.CALL
        ) // medium hand, plays for the whole pot

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2),
                positions = Positions(0, 1, 2),
                communityCards = listOf(
                        Card(CardRank.TWO, CardSuit.HEARTS),
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES),
                        Card(CardRank.SEVEN, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                )
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == sequenceOf(
                PotResult(player2.id, 400, potNumber = 1), // player2 should win the side pot
                PotResult(player0.id, 900) // player0 should win the main pot
        ))
    }

    @Test
    fun `case with multiple side pots and non-equally-splittable chips amount`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 100,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.ALL_IN
        ) // strongest hand, plays for the main pot

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 300,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.JACK, CardSuit.HEARTS)),
                lastAction = ActionType.ALL_IN
        ) // weakest hand, plays for the main pot and 1st side pot

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.ACE, CardSuit.CLUBS), Card(CardRank.JACK, CardSuit.CLUBS)),
                lastAction = ActionType.ALL_IN
        ) // medium hand, plays for the main pot, 1st side pot and 2nd side pot

        val player3 = Player(
                position = 3, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.ACE, CardSuit.DIAMONDS), Card(CardRank.JACK, CardSuit.DIAMONDS)),
                lastAction = ActionType.ALL_IN
        ) // medium hand, plays for the main pot, 1st side pot and 2nd side pot

        val player4 = Player(
                position = 4, stack = 0,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.ACE, CardSuit.SPADES), Card(CardRank.JACK, CardSuit.SPADES)),
                lastAction = ActionType.ALL_IN
        ) // medium hand, plays for the main pot, 1st side pot and 2nd side pot

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2, player3, player4),
                positions = Positions(0, 1, 2),
                communityCards = listOf(
                        Card(CardRank.TWO, CardSuit.HEARTS),
                        Card(CardRank.TWO, CardSuit.SPADES),
                        Card(CardRank.THREE, CardSuit.SPADES),
                        Card(CardRank.SEVEN, CardSuit.CLUBS),
                        Card(CardRank.TEN, CardSuit.CLUBS)
                )
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == sequenceOf(
                PotResult(player2.id, 200, potNumber = 2), // player2 should win third of the 2nd side pot
                PotResult(player3.id, 200, potNumber = 2), // player3 should win third of the 2nd side pot
                PotResult(player4.id, 200, potNumber = 2), // player4 should win third of the 2nd side pot

                PotResult(player2.id, 267, potNumber = 1), // player2 should win third of the 1st side pot + one chip
                PotResult(player3.id, 267, potNumber = 1), // player3 should win third of the 1st side pot + one chip
                PotResult(player4.id, 266, potNumber = 1), // player4 should win third of the 1st side pot

                PotResult(player0.id, 500, potNumber = 0) // player0 should win the main pot
        ))
    }
}
