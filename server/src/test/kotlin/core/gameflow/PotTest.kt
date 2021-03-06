package core.gameflow

import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.bettinground.ActionType
import core.gameflow.handstate.HandState
import core.gameflow.player.Player
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PotTest {

    private val baseBuilder = HandState.ImmutableBuilder(
            blinds = Blinds(10, 20)
    )

    @Nested
    @DisplayName("in simple case with one winner")
    inner class SimpleCasePot {

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

        @Test
        fun `there should be only one, main pot`() {
            val pots = state.pots()
            assert(pots.size == 1)

            val mainPot = pots.first()

            assert(mainPot.players == setOf(player0, player2))
            assert(mainPot.size == 1300)
        }

        @Test
        fun `player with best hand should win the pot`() {
            val potResults = resolvePot(state)
            assert(potResults == listOf(
                    PotResult(1300, player2.id) // player2 should win the whole pot
            ))
        }

        @Test
        fun `distributing the pot should transfer all chips to the winning player and clear chips in pot`() {
            val newState = distributePot(state)
            val players = newState.players

            assert(players[0].stack == 0)
            assert(players[0].chipsInPot == 0)

            assert(players[1].stack == 0)
            assert(players[1].chipsInPot == 0)

            assert(players[2].stack == 1300)
            assert(players[2].chipsInPot == 0)
        }
    }

    @Nested
    @DisplayName("when there are multiple winners")
    inner class BasicSplitPot {
        val player0 = Player(
                position = 0, stack = 200,
                chipsInPot = 500,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.BET
        ) // strongest hand, plays for the whole pot

        val player1 = Player(
                position = 1, stack = 500,
                chipsInPot = 300,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.ACE, CardSuit.SPADES)),
                lastAction = ActionType.FOLD
        ) // not in game

        val player2 = Player(
                position = 2, stack = 100,
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

        @Test
        fun `pot should be split if two players have equally strong hands, chips awarding order should be the same as betting order`() {
            val potResults = resolvePot(state)
            assert(potResults == listOf(
                    PotResult(650, player2.id), // player2 should win half of the pot
                    PotResult(650, player0.id) // player0 should win half of the pot
            ))
        }

        @Test
        fun `each player should get the same amount of chips from the main pot`() {
            val newState = distributePot(state)
            val players = newState.players

            assert(players[0].stack == 200 + 650)
            assert(players[0].chipsInPot == 0)

            assert(players[1].stack == 500)
            assert(players[1].chipsInPot == 0)

            assert(players[2].stack == 100 + 650)
            assert(players[2].chipsInPot == 0)
        }
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
        assert(potResults == listOf(
                PotResult(434, player1.id), // player1 should win third of the pot + one chip
                PotResult(434, player2.id), // player2 should win third of the pot + one chip
                PotResult(433, player0.id) // player0 should win third of the pot
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
        assert(potResults == listOf(
                PotResult(400, player2.id, potNumber = 1), // player2 should win the side pot
                PotResult(900, player0.id) // player0 should win the main pot
        ))
    }

    @Nested
    @DisplayName("when there are multiple side pots")
    inner class MultipleSidePots {
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

        @Test
        fun `there should be main pot and side pots`() {
            val pots = state.pots()
            assert(pots.size == 3)

            val mainPot = pots.first()
            assert(mainPot.players == setOf(player0, player1, player2, player3, player4))
            assert(mainPot.size == 500)
            assert(mainPot.potNumber == 0)

            val firstSidePot = pots[1]
            assert(firstSidePot.players == setOf(player1, player2, player3, player4))
            assert(firstSidePot.size == 800)
            assert(firstSidePot.potNumber == 1)

            val secondSidePot = pots[2]
            assert(secondSidePot.players == setOf(player2, player3, player4))
            assert(secondSidePot.size == 600)
            assert(secondSidePot.potNumber == 2)
        }

        @Test
        fun `each side pot should be resolved separately starting from the last one`() {
            val potResults = resolvePot(state)
            assert(potResults == listOf(
                    PotResult(200, player2.id, potNumber = 2), // player2 should win third of the 2nd side pot
                    PotResult(200, player3.id, potNumber = 2), // player3 should win third of the 2nd side pot
                    PotResult(200, player4.id, potNumber = 2), // player4 should win third of the 2nd side pot

                    PotResult(267, player2.id, potNumber = 1), // player2 should win third of the 1st side pot + one chip
                    PotResult(267, player3.id, potNumber = 1), // player3 should win third of the 1st side pot + one chip
                    PotResult(266, player4.id, potNumber = 1), // player4 should win third of the 1st side pot

                    PotResult(500, player0.id, potNumber = 0) // player0 should win the main pot
            ))
        }
    }

    @Test
    fun `pot should be successfully resolved when there is only one player left in game and there are no community cards`() {
        val player0 = Player(
                position = 0, stack = 0,
                chipsInPot = 100,
                holeCards = listOf(Card(CardRank.KING, CardSuit.DIAMONDS), Card(CardRank.KING, CardSuit.CLUBS)),
                lastAction = ActionType.ALL_IN
        )

        val player1 = Player(
                position = 1, stack = 0,
                chipsInPot = 10,
                holeCards = listOf(Card(CardRank.QUEEN, CardSuit.HEARTS), Card(CardRank.JACK, CardSuit.HEARTS)),
                lastAction = ActionType.FOLD
        )

        val player2 = Player(
                position = 2, stack = 0,
                chipsInPot = 20,
                holeCards = listOf(Card(CardRank.ACE, CardSuit.CLUBS), Card(CardRank.JACK, CardSuit.CLUBS)),
                lastAction = ActionType.FOLD
        )

        val state = baseBuilder.copy(
                players = listOf(player0, player1, player2),
                positions = Positions(0, 1, 2),
                communityCards = emptyList()
        ).build()

        val potResults = resolvePot(state)
        assert(potResults == listOf(
                PotResult(130, player0.id, potNumber = 0) // player0 should win all chips
        ))
    }
}
