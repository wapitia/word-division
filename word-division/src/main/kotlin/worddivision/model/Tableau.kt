package worddivision.model

import worddivision.model.builder.TextTableauBuilder
import worddivision.model.render.TableauRenderer


// tableau width is the number of

/**In a Word Division problem, the com.wapitia.worddivision.model.Subcell or Subtraction cell is a
 * Single subtraction column of a subtraction step. For example,
 * given the Word Division problem:
 * <pre>
 *                   G O
 *           ┌───────────
 *     Y E W │ L I G H T
 *             L Y N O
 *             ───────
 *               E N I T
 *               H I I H
 *               ────────
 *                 L G G
 * </pre>
 * <i>Figure 1</i>
 * The following are Subtraction cells:
 * <pre>
 *      L  I  G  H  E  N  I  T
 *      L  Y  N  O  H  I  I  H
 *      ── ── ── ── ── ── ── ──
 *      0  E  N  I  0  L  G  G
 * </pre>
 *
 * <p>
 * In general the entire subcell is modeled with the following components:
 * <pre>
 *    b X c
 *      Y
 *     ───
 *      Z
 * </pre>
 * Where:
 *  X, Y, Z are placeholders that represent a single letter from the puzzle, which may be a digit
 *  in the range 0 through 9 inclusive.
 *  c represents an optional carry digit, which is either 0 or 1.
 *  b represents an optional borrow digit, which is either 0 or 1.
 *  The puzzle may ascribe the same puzzle letter to the placeholders, which help reduce the puzzle
 *  by reducing the number of possibilities.
 *
 * Captures the subcell equation:
 * <pre>
 *     10b + X - c - Y - Z = 0           Equation 1
 * </pre>
 * where:
 * X,Y,Z are decimal digits in the range 0 .. 9 inclusive.
 * c and b are 0 or 1, indicating whether the carry bit is lowered or isRaised, respectively.
 * <P>
 * In the puzzle, Equation 1 is always true, which reduces the possibilities and helps in the solution.
 * We can reduce, or partially reduce, any variable in terms of the others with respect to Equation 1.
 * <PRE>
 *     X = Y + Z + c - 10b
 *     Y = X - Z - c + 10b
 *     Z = X - Y - c + 10b
 *     c = X - Y - Z + 10b
 *     b = - X + Y + Z + c  == 0 ? 0
 *                          == 10 ? 1
 * </PRE>
 */

class Subcell(val x: Cell, val y: Cell, val z: Cell, val c: Carry, val b: Carry)

/**
 * For the Tableau structure, all SubtractionSteps are padded out to $width of the dividend for output alignment
 * purposes,but otherwise has no function.
 * In <i>Figure 1</i>, the two SubtractionSteps would resemble this, where @ stands for spaces (which solve to 0)
 * <PRE>
 *     LIGHT            @ENIT
 *     LYNO@            @HIIH
 *     ────@            @────
 *     @ENIT            @@LGG
 * </PRE>
 */
data class SubtractionStep(val subcells: Array<Subcell>) {
    fun xCells(): Array<Cell> = xyzCells { c -> c.x }
    fun yCells(): Array<Cell> = xyzCells { c -> c.y }
    fun zCells(): Array<Cell> = xyzCells { c -> c.z }

    private fun xyzCells( cf: (Subcell) -> Cell ): Array<Cell> = subcells.map(cf).toTypedArray()
}

data class Tableau(
        val letters: Array<Letter>,
        val width: Int,
        val dividend: Array<Cell>,
        val divisor: Array<Cell>,
        val quotient: Array<Cell>,
        val subtractionSteps: Array<SubtractionStep>)
{
    override fun toString(): String = TableauRenderer.toString(this)

    companion object Builder {
        fun builder() = TextTableauBuilder()
    }
}


