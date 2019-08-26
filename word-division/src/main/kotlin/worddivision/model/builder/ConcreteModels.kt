package worddivision.model.builder

import worddivision.model.Digit
import worddivision.model.Carry
import worddivision.model.Letter
import worddivision.standard.StandardTextUtility

class MutableLetter(val char: Char): Letter {
    var solution: Digit? = null
    override fun char() = char
    override fun solution(): Digit? = solution
}

object Blank : Letter {
    override fun char() = StandardTextUtility.BLANK
    override fun solution() = 0
}

class MutableCarry(var raised: Boolean): Carry {
    override fun isRaised(): Boolean = raised
    override fun raise(raiseIt: Boolean) {
        this.raised = raiseIt
    }
}
