package core.action.bettinground

sealed class ActionValidation

class ValidAction : ActionValidation()
class InvalidAction(val reason: String) : ActionValidation()
