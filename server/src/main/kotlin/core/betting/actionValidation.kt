package core.betting

sealed class ActionValidation

object ValidAction : ActionValidation()
data class InvalidAction(val reason: String) : ActionValidation()
