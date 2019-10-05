package core.gameflow.player

data class PlayerStatus(val position: Int, val stack: Int) {
    val id: Int = position
}
