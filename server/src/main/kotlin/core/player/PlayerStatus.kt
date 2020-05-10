package core.player

data class PlayerStatus(val position: Int, val stack: Int) {
    val id: Int = position
}
