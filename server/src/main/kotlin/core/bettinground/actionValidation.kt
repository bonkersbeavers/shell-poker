package core.bettinground

sealed class ActionValidation

object ValidAction : ActionValidation()
data class InvalidAction(val reason: String) : ActionValidation()
