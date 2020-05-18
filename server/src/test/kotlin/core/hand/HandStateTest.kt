package core.hand

import core.hand.player.betting.ActionType
import core.cards.Card
import core.cards.CardRank
import core.cards.CardSuit
import core.flowUtils.BettingRound
import core.flowUtils.Blinds
import core.flowUtils.Positions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateTest {
    private val mockCard = Card(CardRank.ACE, CardSuit.SPADES)

    @Test
    fun `betting round should return proper value based on player's hole cards and community cards`() {
        val playerWithoutCards = PlayerState(seat = 0, stack = 100)
        val playerWithCards = playerWithoutCards.copy(cards = Pair(mockCard, mockCard))

        val baseState = HandState(
                playersStates = listOf(playerWithoutCards, playerWithoutCards, playerWithoutCards),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                communityCards = emptyList(),
                pots = emptyList(),
                lastLegalBet = 100,
                extraBet = 0,
                minRaise = 200
        )
        assert(baseState.bettingRound == null)

        val preFlopState = baseState.copy(playersStates = listOf(playerWithCards, playerWithCards, playerWithCards))
        assert(preFlopState.bettingRound == BettingRound.PRE_FLOP)

        val flopState = preFlopState.copy(communityCards = listOf(mockCard, mockCard, mockCard))
        assert(flopState.bettingRound == BettingRound.FLOP)

        val turnState = flopState.copy(communityCards = listOf(mockCard, mockCard, mockCard, mockCard))
        assert(turnState.bettingRound == BettingRound.TURN)

        val riverState = turnState.copy(communityCards = listOf(mockCard, mockCard, mockCard, mockCard, mockCard))
        assert(riverState.bettingRound == BettingRound.RIVER)
    }

    @Test
    fun `lastAggressor should be determined by player's actions and bet sizes`() {
        val baseState = HandState(
                playersStates = emptyList(),
                blinds = Blinds(50, 100),
                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
                communityCards = emptyList(),
                pots = emptyList(),
                lastLegalBet = 100,
                extraBet = 0,
                minRaise = 200
        )

        val noAggressorState = baseState.copy(
                playersStates = listOf(
                        PlayerState(seat = 0, stack = 100, currentBet = 0, currentActionType = null),
                        PlayerState(seat = 1, stack = 100, currentBet = 50, currentActionType = ActionType.POST),
                        PlayerState(seat = 2, stack = 100, currentBet = 100, currentActionType = ActionType.POST)
                ),
                lastLegalBet = 100
        )
        assert(noAggressorState.lastAggressor == null)

        val aggressor1 = PlayerState(seat = 0, stack = 100, currentBet = 200, currentActionType = ActionType.BET)
        val aggressorState1 = baseState.copy(
                playersStates = listOf(
                        aggressor1,
                        PlayerState(seat = 1, stack = 100, currentBet = 50, currentActionType = ActionType.POST),
                        PlayerState(seat = 2, stack = 100, currentBet = 100, currentActionType = ActionType.POST)
                ),
                lastLegalBet = 200
        )
        assert(aggressorState1.lastAggressor == aggressor1)

        val aggressor2 = PlayerState(seat = 1, stack = 100, currentBet = 500, currentActionType = ActionType.RAISE)
        val aggressorState2 = baseState.copy(
                playersStates = listOf(
                        PlayerState(seat = 0, stack = 100, currentBet = 200, currentActionType = ActionType.BET),
                        aggressor2,
                        PlayerState(seat = 2, stack = 100, currentBet = 0, currentActionType = ActionType.CHECK)
                ),
                lastLegalBet = 500
        )
        assert(aggressorState2.lastAggressor == aggressor2)
    }

//    @Test
//    fun `activePlayer should be determined by player's actions and bet sizes`() {
//        val baseState = HandState(
//                playersStates = emptyList(),
//                blinds = Blinds(50, 100),
//                positions = Positions(button = 0, smallBlind = 1, bigBlind = 2),
//                communityCards = emptyList(),
//                pots = emptyList(),
//                lastLegalBet = 100,
//                extraBet = 0,
//                minRaise = 200
//        )
//
//        val noActivePlayerState = baseState.copy(
//                playersStates = listOf(
//                        PlayerState(seat = 0, stack = 100, currentBet = 300, currentActionType = ActionType.CALL),
//                        PlayerState(seat = 1, stack = 100, currentBet = 300, currentActionType = ActionType.RAISE),
//                        PlayerState(seat = 2, stack = 100, currentBet = 300, currentActionType = ActionType.CALL)
//                ),
//                lastLegalBet = 300
//        )
//        assert(noActivePlayerState.activePlayer == null)
//
//        val activePlayer1 = PlayerState(seat = 0, stack = 100, currentBet = 300, currentActionType = ActionType.POST)
//        val activePlayerState1 = baseState.copy(
//                playersStates = listOf(
//                        PlayerState(seat = 0, stack = 100, currentBet = 300, currentActionType = ActionType.CALL),
//                        PlayerState(seat = 1, stack = 100, currentBet = 300, currentActionType = ActionType.CALL),
//                        activePlayer1
//                ),
//                lastLegalBet = 300
//        )
//        assert(activePlayerState1.activePlayer == activePlayer1)
//
//        val activePlayer1 = PlayerState(seat = 0, stack = 100, currentBet = 300, currentActionType = ActionType.POST)
//        val activePlayerState1 = baseState.copy(
//                playersStates = listOf(
//                        PlayerState(seat = 0, stack = 100, currentBet = 300, currentActionType = ActionType.CALL),
//                        PlayerState(seat = 1, stack = 100, currentBet = 300, currentActionType = ActionType.CALL),
//                        activePlayer1
//                ),
//                lastLegalBet = 300
//        )
//        assert(activePlayerState1.activePlayer == activePlayer1)
//    }
}
