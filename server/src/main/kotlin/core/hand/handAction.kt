package core.hand

import core.betting.ActionValidation

open class HandAction

interface ApplicableHandAction {
    fun apply(handState: HandState): HandState
}

interface ValidatableHandAction {
    fun validate(handState: HandState): Boolean
}
