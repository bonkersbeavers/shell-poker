package core.player

data class PlayerStatus(val seat: Int, val stack: Int) {
    val id: Int = seat
}
