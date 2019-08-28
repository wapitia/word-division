package worddivision.model.builder

import worddivision.model.Digit
import worddivision.model.Carry
import worddivision.model.Letter
import worddivision.standard.StandardTextUtility.BLANK

class MutableLetter(val char: Char): Letter {
    var solution: Digit? = null
    override fun char() = char
    override fun hasChar() = char != BLANK
    override fun solution() = solution
}

object Blank : Letter {
    override fun char() = BLANK
    override fun hasChar() = false
    override fun solution() = 0
}

class MutableCarry(var raised: Boolean): Carry {
    override fun isRaised() = raised
    override fun raise(raiseIt: Boolean) {
        this.raised = raiseIt
    }
}
