package core.action

sealed class ShowdownAction

data class ShowCards(val playerId: Int) : ShowdownAction()
data class MuckCards(val playerId: Int) : ShowdownAction()
