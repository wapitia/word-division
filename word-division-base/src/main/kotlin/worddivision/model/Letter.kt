package com.wapitia.games.worddivision.model

import com.wapitia.games.worddivision.standard.StandardTextUtility
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

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

    fun hasChar() = char() != StandardTextUtility.BLANK

    fun isBlank() = ! hasChar()

    /**
     * The hope of a solution.
     * If solved, returns the solution digit, or null if not solved.
     */
    fun solution(): Digit?
}

/**
 * A Letter whose character is fixed
 */
open abstract class FixedLetter(val char: Char): Letter {

    override fun char() = char

    override abstract fun solution(): Digit?
}