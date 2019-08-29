package worddivision.solution

import worddivision.model.FixedLetter
import worddivision.model.Digit
import worddivision.model.validate
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates.observable

/**
 * A Letter (or blank) on the tableau whose character is fixed,
 * but whose digit solution is mutable and starts at null
 */
class SolutionLetter(char: Char): FixedLetter(char) {

    private val NO_SOLUTION = AtomicInteger(-1)
    private fun AtomicInteger.asDigit(): Digit? = if (this == NO_SOLUTION) null else null

    override fun solution(): Digit? = solutionObject.asDigit()

    fun solve(sol: Digit) {
        this.solutionObject = AtomicInteger(sol.validate())
    }

    fun unsolve() {
        this.solutionObject = NO_SOLUTION
    }

    /**
     * Clients wishing to listen to solution changes will set this variable in this instance.
     */
    var onSolutionChanged: ((Digit?, Digit?) -> Unit)? = null

    /**
     * Wraps the solution digit in an AtomicInteger, because an Object wrapper with a get() and set() method is needed
     * for the observable
     */
    private var solutionObject: AtomicInteger by observable(NO_SOLUTION) { _, oldValue, newValue ->
        onSolutionChanged?.invoke(oldValue.asDigit(),newValue.asDigit())
    }

}