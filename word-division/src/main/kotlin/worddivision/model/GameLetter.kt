package worddivision.model

class GameLetter(val char: Char) : Letter {

    var solvedDigit: Digit? = null

    override fun char() = char

    override fun solution(): Digit? = solvedDigit

    fun solveAs(digit: Digit) {
        this.solvedDigit = digit
    }

    fun unsolve() {
        this.solvedDigit = null
    }

}
