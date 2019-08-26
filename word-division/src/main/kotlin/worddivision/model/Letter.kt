package worddivision.model

/**
 * A common letter of the puzzle whose instance
 * is shared by all Cells, so that for example if
 * there is a 'W' in the puzzle there should be exactly
 * one 'W' Letter instance
 *
 */
interface Letter {

    /**
     * The letter of the puzzle, or BLANK if it is a  blank (in which the
     * solution is automatically 0)
     */
    fun char(): Char

    /**
     * The hope of a solution.
     * If solved, returns the solution digit, or null if not solved.
     */
    fun solution(): Digit?
}