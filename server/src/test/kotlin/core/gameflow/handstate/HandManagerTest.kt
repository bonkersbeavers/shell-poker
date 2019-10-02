package core.gameflow.handstate

import core.bettinground.*
import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.gameflow.Blinds
import core.gameflow.Positions
import core.gameflow.RoomSettings
import core.gameflow.gamestate.GameState
import core.gameflow.player.PlayerStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import testutil.fake.FakeCommunicator
import io.mockk.spyk
import io.mockk.verifySequence
import testutil.fake.FakeHoldemDealer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandManagerTest {

    private fun getSampleGameState(playersNumber: Int, stackSize: Int, buttonPosition: Int): GameState {
        val blinds = Blinds(50, 100)
        val positions = Positions(
                buttonPosition,
                (buttonPosition + 1) % playersNumber,
                (buttonPosition + 2) % playersNumber
        )
        val players = (0 until playersNumber).map { PlayerStatus(position = it, stack = stackSize) }
        return GameState(
                playersStatuli = players,
                blinds = blinds,
                positions = positions
        )
    }

    @Test
    fun `classic showdown after river`() {

        val holeCards = listOf(
                Pair(Card(CardRank.KING, CardSuit.SPADES), Card(CardRank.SIX, CardSuit.DIAMONDS)),
                Pair(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.SIX, CardSuit.HEARTS)),
                Pair(Card(CardRank.SEVEN, CardSuit.CLUBS), Card(CardRank.FOUR, CardSuit.HEARTS)),
                Pair(Card(CardRank.QUEEN, CardSuit.CLUBS), Card(CardRank.TEN, CardSuit.SPADES))
        )
        val communityCards = listOf(
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.HEARTS)
        )
        val fakeDealerSpy = spyk(FakeHoldemDealer(holeCards, communityCards))

        val preFlopActions = listOf(
                Raise(300), // player 3
                Fold, // player 0
                Call, // player 1
                Fold // player 2
        )
        val flopActions = listOf(
                Check, // player 1
                Bet(300), // player 3
                Call // player 1
        )
        val turnActions = listOf(
                Check, // player 1
                Bet(200), // player 3
                Call // player 1
        )
        val riverActions = listOf(
                Check, // player 1
                Check // player 3, end of action
        )
        val fakeCommunicator = FakeCommunicator(preFlopActions + flopActions + turnActions + riverActions)

        val startingState = getSampleGameState(playersNumber = 4, stackSize = 1000, buttonPosition = 3)
        val roomSettings = RoomSettings(tableSeatsNumber = 4)

        val handManager = HandManager(fakeDealerSpy, fakeCommunicator, roomSettings)
        val finalState = handManager.playHand(startingState)

        assert(finalState.positions == Positions(0, 1, 2))
        assert(finalState.playersStatuli == listOf(
                PlayerStatus(0, 1000),
                PlayerStatus(1, 200),
                PlayerStatus(2, 900),
                PlayerStatus(3, 1900)
        ))

        // check that dealer has dealt hole cards and all three streets
        verifySequence {
            fakeDealerSpy.shuffle()
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
        }
    }

    @Test
    fun `everyone folded after one player's bet`() {

        val holeCards = listOf(
                Pair(Card(CardRank.KING, CardSuit.SPADES), Card(CardRank.SIX, CardSuit.DIAMONDS)),
                Pair(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.SIX, CardSuit.HEARTS)),
                Pair(Card(CardRank.SEVEN, CardSuit.CLUBS), Card(CardRank.FOUR, CardSuit.HEARTS)),
                Pair(Card(CardRank.QUEEN, CardSuit.CLUBS), Card(CardRank.TEN, CardSuit.SPADES))
        )
        val communityCards = listOf(
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.HEARTS)
        )
        val fakeDealerSpy = spyk(FakeHoldemDealer(holeCards, communityCards))

        val preFlopActions = listOf(
                Raise(300), // player 3
                Fold, // player 0
                Call, // player 1
                Fold // player 2
        )
        val flopActions = listOf(
                Check, // player 1
                Bet(300), // player 3
                Call // player 1
        )
        val turnActions = listOf(
                Check, // player 1
                AllIn, // player 3
                Fold // player 1, end of action
        )
        val fakeCommunicator = FakeCommunicator(preFlopActions + flopActions + turnActions)

        val startingState = getSampleGameState(playersNumber = 4, stackSize = 1000, buttonPosition = 3)
        val roomSettings = RoomSettings(tableSeatsNumber = 4)

        val handManager = HandManager(fakeDealerSpy, fakeCommunicator, roomSettings)
        val finalState = handManager.playHand(startingState)

        assert(finalState.positions == Positions(0, 1, 2))
        assert(finalState.playersStatuli == listOf(
                PlayerStatus(0, 1000),
                PlayerStatus(1, 400),
                PlayerStatus(2, 900),
                PlayerStatus(3, 1700)
        ))

        // check that dealer has dealt hole cards and exactly two streets
        verifySequence {
            fakeDealerSpy.shuffle()
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
        }
    }

    @Test
    fun `all-ins before river`() {

        val holeCards = listOf(
                Pair(Card(CardRank.KING, CardSuit.SPADES), Card(CardRank.SIX, CardSuit.DIAMONDS)),
                Pair(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.SIX, CardSuit.HEARTS)),
                Pair(Card(CardRank.SEVEN, CardSuit.CLUBS), Card(CardRank.FOUR, CardSuit.HEARTS)),
                Pair(Card(CardRank.QUEEN, CardSuit.CLUBS), Card(CardRank.TEN, CardSuit.SPADES))
        )
        val communityCards = listOf(
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.HEARTS)
        )
        val fakeDealerSpy = spyk(FakeHoldemDealer(holeCards, communityCards))

        val preFlopActions = listOf(
                Raise(300), // player 3
                Fold, // player 0
                Call, // player 1
                Fold // player 2
        )
        val flopActions = listOf(
                Check, // player 1
                AllIn, // player 3
                Call // player 1
        )
        val fakeCommunicator = FakeCommunicator(preFlopActions + flopActions)

        val startingState = getSampleGameState(playersNumber = 4, stackSize = 1000, buttonPosition = 3)
        val roomSettings = RoomSettings(tableSeatsNumber = 4)

        val handManager = HandManager(fakeDealerSpy, fakeCommunicator, roomSettings)
        val finalState = handManager.playHand(startingState)

        assert(finalState.positions == Positions(0, 1, 2))
        assert(finalState.playersStatuli == listOf(
                PlayerStatus(0, 1000),
                PlayerStatus(1, 0),
                PlayerStatus(2, 900),
                PlayerStatus(3, 2100)
        ))

        // check that dealer has dealt hole cards and all three streets
        verifySequence {
            fakeDealerSpy.shuffle()
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
        }
    }

    @Test
    fun `invalid actions requested during the hand`() {

        val holeCards = listOf(
                Pair(Card(CardRank.KING, CardSuit.SPADES), Card(CardRank.SIX, CardSuit.DIAMONDS)),
                Pair(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.SIX, CardSuit.HEARTS)),
                Pair(Card(CardRank.SEVEN, CardSuit.CLUBS), Card(CardRank.FOUR, CardSuit.HEARTS)),
                Pair(Card(CardRank.QUEEN, CardSuit.CLUBS), Card(CardRank.TEN, CardSuit.SPADES))
        )
        val communityCards = listOf(
                Card(CardRank.QUEEN, CardSuit.DIAMONDS),
                Card(CardRank.NINE, CardSuit.HEARTS),
                Card(CardRank.ACE, CardSuit.SPADES),
                Card(CardRank.THREE, CardSuit.CLUBS),
                Card(CardRank.TWO, CardSuit.HEARTS)
        )
        val fakeDealerSpy = spyk(FakeHoldemDealer(holeCards, communityCards))

        val preFlopActions = listOf(
                Raise(300), // player 3
                Check, // player 0, invalid
                Fold, // player 0
                Call, // player 1
                Fold // player 2
        )
        val flopActions = listOf(
                Check, // player 1
                AllIn, // player 3
                Raise(2000), // player 1, invalid
                Call // player 1
        )
        val fakeCommunicator = FakeCommunicator(preFlopActions + flopActions)

        val startingState = getSampleGameState(playersNumber = 4, stackSize = 1000, buttonPosition = 3)
        val roomSettings = RoomSettings(tableSeatsNumber = 4)

        val handManager = HandManager(fakeDealerSpy, fakeCommunicator, roomSettings)
        val finalState = handManager.playHand(startingState)

        assert(finalState.positions == Positions(0, 1, 2))
        assert(finalState.playersStatuli == listOf(
                PlayerStatus(0, 1000),
                PlayerStatus(1, 0),
                PlayerStatus(2, 900),
                PlayerStatus(3, 2100)
        ))

        // check that dealer has dealt hole cards and all three streets
        verifySequence {
            fakeDealerSpy.shuffle()
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
            fakeDealerSpy.deal(any())
        }
    }

    @Test
    fun `new player's ids list should be cleared after playing hand`() {

        val holeCards = listOf(
                Pair(Card(CardRank.KING, CardSuit.SPADES), Card(CardRank.SIX, CardSuit.DIAMONDS)),
                Pair(Card(CardRank.JACK, CardSuit.HEARTS), Card(CardRank.SIX, CardSuit.HEARTS)),
                Pair(Card(CardRank.SEVEN, CardSuit.CLUBS), Card(CardRank.FOUR, CardSuit.HEARTS))
        )
        val communityCards = emptyList<Card>()
        val fakeDealer = FakeHoldemDealer(holeCards, communityCards)

        val preFlopActions = listOf(
                Fold,
                Fold,
                Fold
        )

        val fakeCommunicator = FakeCommunicator(preFlopActions)

        val startingState = getSampleGameState(playersNumber = 3, stackSize = 1000, buttonPosition = 3)
                .copy(newPlayersIds = listOf(0, 2))

        val roomSettings = RoomSettings(tableSeatsNumber = 4)

        val handManager = HandManager(fakeDealer, fakeCommunicator, roomSettings)
        val finalState = handManager.playHand(startingState)

        assert(finalState.newPlayersIds.isEmpty())
    }
}
