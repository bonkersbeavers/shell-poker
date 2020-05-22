package core.handflow.hand

open class HandAction

interface ApplicableHandAction {
    fun apply(handState: HandState): HandState
}

interface ValidatableHandAction {
    fun validate(handState: HandState): ActionValidation
}

sealed class ActionValidation

object ValidAction : ActionValidation()
data class InvalidAction(val reason: String) : ActionValidation()
