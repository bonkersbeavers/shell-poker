import core.RoomSettings
import core.hand.blinds.Blinds
import core.hand.positions.Positions
import core.hand.HandManager
import core.hand.HandState
import core.hand.LocalPlayerAdapter
import core.hand.Player

fun main() {

    val adapter = LocalPlayerAdapter()

    val settings = RoomSettings(4)
    val blinds = Blinds(50, 100)
    val positions = Positions(0, 1, 2)
    val players = (0 until 3).map { Player(seat = it, stack = 1000) }

    val manager = HandManager(settings, adapter)
    var state = HandState(players, positions, blinds)

    while (true) {
        state = manager.playHand(state)
    }
}
