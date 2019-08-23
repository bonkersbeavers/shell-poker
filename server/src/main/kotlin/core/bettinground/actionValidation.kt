package core.bettinground

sealed class ActionValidation

class ValidAction : ActionValidation()
data class InvalidAction(val reason: String) : ActionValidation()
