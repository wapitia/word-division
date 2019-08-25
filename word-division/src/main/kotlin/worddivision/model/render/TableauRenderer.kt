package worddivision.model.render

import worddivision.model.Cell
import worddivision.model.Tableau
import worddivision.standard.StandardTextUtility.spaces
import worddivision.standard.StandardTextUtility.repeats
import worddivision.standard.StandardTextUtility.BLANK
import worddivision.standard.StandardTextUtility.LF

object TableauRenderer {

    fun toString(tableau: Tableau): String = writeTableau(tableau, StringBuilder(), DefaultMetrics).toString()

    data class WriteMetrics(val cellGap: Int, val showCandiatesTop: Boolean)

    val DefaultMetrics = WriteMetrics(cellGap = 1, showCandiatesTop = true)

    /**
     * Write the tableau close to the layout
     * <pre>
     *                   G O
     *           ┌───────────
     *     Y E W │ L I G H T
     *             L Y N O
     *             ───────
     *               E N I T
     *               H I I H
     *               ───────
     *                 L G G
     * </pre>
     * <i>Caption: Figure 1</i>
      *
     */
    fun writeTableau(tableau: Tableau, out: StringBuilder, metx: WriteMetrics): StringBuilder {

        val MarginWidth = 3   // margin between divisor and the rest of the tableau.
        // dividend/divisor/quotient widths are the text width including the gap, and defines the overall matrix size
        val dividendWidth = textWidth(tableau.width, metx.cellGap)
        val quotientWidth = textWidth(tableau.quotient.size, metx.cellGap)
        val divisorWidth = textWidth(tableau.divisor.size, metx.cellGap)

        // space prefixes
        val divisorMargin = spaces(divisorWidth)
        val marginPrefix = spaces(MarginWidth)
        val quotientPrefix = spaces(dividendWidth - quotientWidth )

        // display candidate letters at top
        if (metx.showCandiatesTop) {
            out.append("CANDIDATES:").append(LF)
            tableau.letters.forEach { ltr ->
                out.append(ltr.char())
                out.append("  ")
            }
            out.append(LF)
            out.append(LF)
        }

        // output quotient line
        out.append(divisorMargin).append(marginPrefix).append(quotientPrefix).append(cellArraytoText(tableau.quotient, metx.cellGap))
            .append(LF)

        // output horizontal separator line
        out.append(divisorMargin).append(" ┌").append(repeats('─',dividendWidth + 1))
            .append(LF)

        // get the divident from the x-row of the first subtraction step
        val dividend = tableau.subtractionSteps.get(0).xCells()
        // output divisor / dividend line
        out.append(cellArraytoText(tableau.divisor, metx.cellGap)).append(" │ ")
            .append(cellArraytoText(dividend, metx.cellGap))
            .append(LF)

        // print the y and z rows of each subtraction step. Only the first x row is printed, which is the divident
        // in subsequent steps, the z overlaps the next x row, so no more x rows
        for (subtractionStep in tableau.subtractionSteps) {
            // output y line
            val yCells = subtractionStep.yCells()
            out.append(divisorMargin).append("   ")
                .append(cellArraytoText(yCells, metx.cellGap))
                .append(LF)

            // compute the pre and post blanks of the z Cells, and whether they're all blank
            var countYPreBlanks = 0
            var countYPostBlanks = 0
            var isYAllBlank = true
            for (cell in yCells) {
                var cellIsBlank = cell.char() == BLANK
                if (cellIsBlank) {
                    if (isYAllBlank) ++countYPreBlanks
                    ++countYPostBlanks
                }
                else {
                    isYAllBlank = false
                    countYPostBlanks = 0
                }
            }
            if ( ! isYAllBlank) {
                val zCells = subtractionStep.zCells()
                val preWid = prefixWidth(countYPreBlanks, metx.cellGap)
                val textSize = zCells.size - countYPreBlanks - countYPostBlanks
                val wid = textWidth(textSize, metx.cellGap)
                val postWid = 0    // textWidth(countZPostBlanks, metx.cellGap)
                out.append(divisorMargin).append("   ")
                    .append(spaces(preWid))
                    .append(repeats('─',wid))
                    .append(spaces(postWid))
                    .append(LF)
                // output z line
                out.append(divisorMargin).append("   ")
                    .append(cellArraytoText(zCells, metx.cellGap))
                    .append(LF)
            }
        }
        return out
    }

    fun textWidth(width: Int, gap: Int): Int = width + gap * (width - 1)

    fun prefixWidth(width: Int, gap: Int): Int = width * (gap + 1)

    fun cellArraytoText(numb: Array<Cell>, cellGap: Int): String {
        val bldr = StringBuilder()
        val iterator: Iterator<Cell> = numb.iterator()
        if (iterator.hasNext()) {
            val cell = iterator.next()
            bldr.append(cell.char())
            toTextR(iterator, bldr, cellGap)
        }
        return bldr.toString()
    }

    private fun toTextR(iterator: Iterator<Cell>, bldr: StringBuilder, cellGap: Int) {
        if (iterator.hasNext()) {
            (0 until cellGap).forEach { _ -> bldr.append(BLANK) }
            val cell = iterator.next()
            bldr.append(cell.char())
            toTextR(iterator, bldr, cellGap)
        }
    }

}

class TableauRenderException(message: String) : Exception(message)