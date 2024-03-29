package worddivision.model

import worddivision.model.builder.TextTableauBuilder
import worddivision.model.builder.SubtractionStepBuilder
import worddivision.model.builder.TextTableauBuilder2

import worddivision.model.render.TableauRenderer

// tableau width is the number of

/**In a Word Division problem, the com.wapitia.worddivision.model.Subcell or Subtraction cell is a
 * Single subtraction column of a subtraction step. For example,
 * given the Word Division problem:
 * <pre>
 *                   G O
 *           ┌───────────
 *     M E W │ L I G H T
 *             L M N O
 *             ───────
 *               E N I T
 *               H I I H
 *               ────────
 *                 L G G
 * </pre>
 * <i>Figure 1</i>
 * The following are Subtraction cellCache:
 * <pre>
 *      L  I  G  H  E  N  I  T
 *      L  M  N  O  H  I  I  H
 *      ── ── ── ── ── ── ── ──
 *      0  E  N  I  0  L  G  G
 * </pre>
 *
 * <p>
 * In general the entire subcell is modeled with the following components:
 * <pre>
 *    b S c
 *      M
 *     ───
 *      D
 * </pre>
 * Where:
 *  S, M, D are placeholders that represent a single letter from the puzzle, which may be a digit
 *  in the range 0 through 9 inclusive.
 *  c represents an optional carry digit, which is either 0 or 1.
 *  b represents an optional borrow digit, which is either 0 or 1.
 *  The puzzle may ascribe the same puzzle letter to the placeholders, which help reduce the puzzle
 *  by reducing the number of possibilities.
 *
 * Captures the subcell equation:
 * <pre>
 *     10b + S - c - M - D = 0           Equation 1
 * </pre>
 * where:
 * S,M,D are decimal digits in the range 0 .. 9 inclusive.
 * c and b are 0 or 1, indicating whether the carry bit is lowered or isRaised, respectively.
 * <P>
 * In the puzzle, Equation 1 is always true, which reduces the possibilities and helps in the solution.
 * We can reduce, or partially reduce, any variable in terms of the others with respect to Equation 1.
 * <PRE>
 *     S = M + D + c - 10b
 *     M = S - D - c + 10b
 *     D = S - M - c + 10b
 *     c = S - M - D + 10b
 *     b = - S + M + D + c  == 0 ? 0
 *                          == 10 ? 1
 * </PRE>
 */

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
class SubtractionStep(val subcells: Array<SubtractionCell>) {
    fun subtrahendRow() = cellRowOf(SubtractionRole.SUBTRAHEND)
    fun minuendRow() = cellRowOf(SubtractionRole.MINUEND)
    fun differenceRow() = cellRowOf(SubtractionRole.DIFFERENCE)

    fun cellRowOf(sst: SubtractionRole):CellRow =
        CellRow.builder()
                .cells(SubtractionCell.cellSequenceOf(sst, subcells))
                .build()

    companion object Builder {
        fun builder() = SubtractionStepBuilder()
    }
}

class Tableau(
    val letters: Array<Letter>,
    val width: Int,
    val dividend: CellRow,
    val divisor: CellRow,
    val quotient: CellRow,
    val subtractionSteps: Array<SubtractionStep>)
{
    override fun toString(): String = TableauRenderer.toString(this)

    companion object Builder {
        fun textBuilder() = TextTableauBuilder()
        fun textBuilder2() = TextTableauBuilder2()
    }
}


