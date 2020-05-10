package core.flowUtils

import core.RoomSettings
import core.handflow.HandState
import core.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionsTest {

    private val roomSettings = RoomSettings(seatsNumber = 5)

    @Test
    fun `button and blinds should move clockwise by one seat if players list doesn't change between hands`() {

        val builder = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        Player(position = 1, stack = 500), // SB
                        Player(position = 2, stack = 500), // BB
                        Player(position = 3, stack = 500)
                ),
                positions = Positions(0, 1, 2)
        )

        val updatedBuilder1 = shiftPositions(builder, roomSettings)
        assert(updatedBuilder1.positions!! == Positions(1, 2, 3))

        val updatedBuilder2 = shiftPositions(updatedBuilder1, roomSettings)
        assert(updatedBuilder2.positions!! == Positions(2, 3, 0))
    }

    @Test
    fun `button and small blind should point to the same seat in heads up game`() {

        val builder = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN + SB
                        Player(position = 1, stack = 500) // BB
                ),
                positions = Positions(0, 0, 1)
        )

        val updatedBuilder1 = shiftPositions(builder, roomSettings)
        assert(updatedBuilder1.positions!! == Positions(1, 1, 0))

        val updatedBuilder2 = shiftPositions(updatedBuilder1, roomSettings)
        assert(updatedBuilder2.positions!! == Positions(0, 0, 1))
    }

    @Test
    fun `big blind should always move forward when players number changes`() {

        val builder1 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        // 1 - (empty) SB
                        Player(position = 2, stack = 500) // BB
                ),
                positions = Positions(0, 1, 2)
        )
        val updatedBuilder1 = shiftPositions(builder1, roomSettings)
        assert(updatedBuilder1.positions!! == Positions(2, 2, 0))

        val builder2 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        Player(position = 1, stack = 500) // SB
                        // 2 - (empty) BB
                ),
                positions = Positions(0, 1, 2)
        )
        val updatedBuilder2 = shiftPositions(builder2, roomSettings)
        assert(updatedBuilder2.positions!! == Positions(1, 1, 0))
    }

    @Test
    fun `big blind should move forward, BTN and SB should point to the other player when players number drops to 2`() {

        val builder1 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        // 1 - (empty) SB
                        Player(position = 2, stack = 500) // BB
                ),
                positions = Positions(0, 1, 2)
        )
        val updatedBuilder1 = shiftPositions(builder1, roomSettings)
        assert(updatedBuilder1.positions!! == Positions(2, 2, 0))

        val builder2 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        Player(position = 1, stack = 500) // SB
                        // 2 - (empty) BB
                ),
                positions = Positions(0, 1, 2)
        )
        val updatedBuilder2 = shiftPositions(builder2, roomSettings)
        assert(updatedBuilder2.positions!! == Positions(1, 1, 0))
    }

    @Test
    fun `SB must move forward, even if it is placed at an empty seat as a result`() {

        val builder1 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500),
                        // 1 - (empty)
                        Player(position = 2, stack = 500), // BTN
                        Player(position = 3, stack = 500) // SB
                        // 4 - (empty) BB
                ),
                positions = Positions(2, 3, 4)
        )
        val updatedBuilder1 = shiftPositions(builder1, roomSettings)
        assert(updatedBuilder1.positions!! == Positions(3, 4, 0))

        val builder2 = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500),
                        Player(position = 1, stack = 500), // BTN
                        // 2 - (empty) SB
                        Player(position = 3, stack = 500) // BB
                        // 4 - (empty)
                ),
                positions = Positions(1, 2, 3)
        )
        val updatedBuilder2 = shiftPositions(builder2, roomSettings)
        assert(updatedBuilder2.positions!! == Positions(2, 3, 0))
    }

    @Test
    fun `BTN and SB must be placed according to BB, even if it means that they skip some players`() {

        val builder = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN
                        Player(position = 1, stack = 500), // SB
                        Player(position = 2, stack = 500), // new player
                        Player(position = 3, stack = 500), // new player
                        Player(position = 4, stack = 500) // BB
                ),
                positions = Positions(0, 1, 4)
        )
        val updatedBuilder = shiftPositions(builder, roomSettings)
        assert(updatedBuilder.positions!! == Positions(3, 4, 0))
    }

    @Test
    fun `BTN should stay in place when players number changes from 2 to 3`() {

        val builder = HandState.ImmutableBuilder(
                players = listOf(
                        Player(position = 0, stack = 500), // BTN / SB
                        Player(position = 1, stack = 500), // BB
                        Player(position = 2, stack = 500) // new player
                ),
                positions = Positions(0, 0, 1)
        )
        val updatedBuilder = shiftPositions(builder, roomSettings)
        assert(updatedBuilder.positions!! == Positions(0, 1, 2))
    }

    @Test
    fun `BTN and SB may both point to empty seats in some cases`() {

        val builder = HandState.ImmutableBuilder(
                players = listOf(
                        // 0 - (empty) SB
                        // 1 - (empty) BB
                        Player(position = 2, stack = 500),
                        Player(position = 3, stack = 500),
                        Player(position = 4, stack = 500) // BTN
                ),
                positions = Positions(4, 0, 1)
        )
        val updatedBuilder = shiftPositions(builder, roomSettings)
        assert(updatedBuilder.positions!! == Positions(0, 1, 2))
    }
}
