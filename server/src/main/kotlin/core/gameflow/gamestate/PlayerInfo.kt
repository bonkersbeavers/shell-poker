package core.gameflow.gamestate

data class PlayerInfo(val position: Int, val stack: Int) {
    val id: Int = position
}