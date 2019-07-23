package core.gameflow

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandStateTest {

    private val chipsA = 150
    private val playerA = Player(
            position = 0,
            stack = 0,
            chipsInPot = chipsA
    )

    private val chipsB = 300
    private val playerB = Player(
            position = 1,
            stack = 0,
            chipsInPot = chipsB
    )

    private val chipsC = 200
    private val playerC = Player(
            position = 2,
            stack = 0,
            chipsInPot = chipsC
    )

    private val mockBlinds = Blinds(0, 0)

    private val handState = HandState(
            players = listOf(playerA, playerB, playerC),
            blinds = mockBlinds,
            buttonPosition = 0,
            activePlayer = playerB
    )

    private val findHandState1 = HandState(
            players = listOf(
                    Player(position = 0, stack = 0, folded = true),
                    Player(position = 1, currentBet = 1000, stack = 0), //all in
                    Player(position = 2, stack = 0),
                    Player(position = 3, stack = 0)
            ),
            blinds = mockBlinds,
            buttonPosition = 0,
            activePlayer = playerB
    )

    private val findHandState2 = HandState(
            players = listOf(
                    Player(position = 0, stack = 0, folded = true),
                    Player(position = 1, stack = 0, folded = true),
                    Player(position = 1, stack = 0, folded = true),
                    Player(position = 3, stack = 0)
            ),
            blinds = mockBlinds,
            buttonPosition = 0,
            activePlayer = playerB
    )

    private val findHandState3 = HandState(
            players = listOf(
                    Player(position = 0, stack = 0, folded = true),
                    Player(position = 1, stack = 0, folded = true),
                    Player(position = 1, stack = 0, folded = true),
                    Player(position = 3, stack = 0, folded = true)
            ),
            blinds = mockBlinds,
            buttonPosition = 0,
            activePlayer = playerB
    )

    @Test
    fun `pot member should return the sum of all players' chipsInPot`() {
        assert(handState.pot == chipsA + chipsB + chipsC)
    }

    @Test
    fun `updateActivePlayer should properly substitute active player with new instance`() {
        val update = playerB.copy(chipsInPot = 500)
        val newState = handState.updateActivePlayer(update)
        assert(newState.activePlayer == update)
        assert(newState.pot == chipsA + 500 + chipsC)
    }

    @Test
    fun `HandState instantiation should fail if activePlayer is not present in players list`() {
        assertThrows<AssertionError> {
            HandState(
                    players = listOf(playerA, playerB),
                    blinds = mockBlinds,
                    buttonPosition = 0,
                    activePlayer = playerC
            )
        }
    }

    @Test
    fun `HandState instantiation should fail if players' positions are not unique`() {
        val illegalPlayer = Player(
                position = 2,
                stack = 0
        )

        assertThrows<AssertionError> {
            HandState(
                    players = listOf(playerA, playerB, playerC, illegalPlayer),
                    blinds = mockBlinds,
                    buttonPosition = 0,
                    activePlayer = playerC
            )
        }
    }

    @Test
    fun `findNextPlayer should find next player`() {
        assert(findHandState1.findNextPlayer(3) == findHandState1.players[0])
        assert(findHandState1.findNextPlayer(1) == findHandState1.players[2])
    }

    @Test
    fun `findNextDecisivePlayer should find next active player`() {
        assert(findHandState1.findNextDecisivePlayer(3) == findHandState1.players[2])
        assert(findHandState1.findNextDecisivePlayer(0) == findHandState1.players[2])
        assert(findHandState1.findNextDecisivePlayer(2) == findHandState1.players[3])

        assert(findHandState2.findNextDecisivePlayer(3) == findHandState2.players[3])

        assert(findHandState3.findNextDecisivePlayer(3) == null)
    }

    @Test
    fun `findPrevPlayer should find previous player`() {
        assert(findHandState1.findPrevPlayer(2) == findHandState1.players[1])
        assert(findHandState1.findPrevPlayer(3) == findHandState1.players[2])
        assert(findHandState1.findPrevPlayer(0) == findHandState1.players[3])
    }

    @Test
    fun `findPrevActivePlayer should find previous active player`() {
        assert(findHandState1.findPrevDecisivePlayer(2) == findHandState1.players[3])
        assert(findHandState1.findPrevDecisivePlayer(3) == findHandState1.players[2])

        assert(findHandState2.findPrevDecisivePlayer(3) == findHandState2.players[3])

        assert(findHandState3.findPrevDecisivePlayer(0) == null)
    }
}
