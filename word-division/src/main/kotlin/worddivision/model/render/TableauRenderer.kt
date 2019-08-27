package worddivision.model.render

import worddivision.model.Cell
import worddivision.model.Tableau
import worddivision.model.CellRow
import worddivision.standard.StandardCollectionUtility
import worddivision.standard.StandardTextUtility.spaces
import worddivision.standard.StandardTextUtility.repeats
import worddivision.standard.StandardTextUtility.BLANK
import worddivision.standard.StandardTextUtility.LF

object TableauRenderer {

    data class PrintMetrics (
        val cellGap: Int =1,
        val showCandiatesTop: Boolean = false)

    fun toString(tableau: Tableau, metrics: PrintMetrics = PrintMetrics(showCandiatesTop = true)): String =
        writeTableau(tableau, StringBuilder(), metrics).toString()


    /**
     * Write the tableau close to the layout
     * <pre>
     *                   G O
     *           ┌───────────
     *     M E W │ L I G H T
     *             L M N O
     *             ───────
     *               E N I T
     *               H I I H
     *               ───────
     *                 L G G
     * </pre>
     * <i>Figure 1</i>
     *
     */
    fun writeTableau(tableau: Tableau, out: StringBuilder, metrics: PrintMetrics): StringBuilder {

        val MarginWidth = 3   // margin between divisor and the rest of the tableau.
        // dividend/divisor/quotient widths are the text width including the gap, and defines the overall matrix size
        val dividendWidth = textWidth(tableau.width, metrics.cellGap)
        val quotientWidth = textWidth(tableau.quotient.size, metrics.cellGap)
        val divisorWidth = textWidth(tableau.divisor.size, metrics.cellGap)

        // space prefixes
        val divisorMargin = spaces(divisorWidth)
        val marginPrefix = spaces(MarginWidth)
        val quotientPrefix = spaces(dividendWidth - quotientWidth)

        // display candidate letters at top
        if (metrics.showCandiatesTop) {
            out.append("CANDIDATES:").append(LF)
            tableau.letters.forEach { ltr ->
                out.append(ltr.char())
                out.append(" ")
            }
            out.append(LF)
            out.append(LF)
        }

        // output quotient line
        out.append(divisorMargin).append(marginPrefix).append(quotientPrefix)
            .append(cellArraytoText(tableau.quotient, metrics.cellGap))
            .append(LF)

        // output horizontal separator line
        out.append(divisorMargin).append(" ┌").append(repeats('─', dividendWidth + 1))
            .append(LF)

        // get the divident from the s-row of the first subtraction step
        val dividend = tableau.subtractionSteps.get(0).subtrahendRow()
        // output divisor / dividend line
        out.append(cellArraytoText(tableau.divisor, metrics.cellGap)).append(" │ ")
            .append(cellArraytoText(dividend, metrics.cellGap))
            .append(LF)

        // print the m and d rows of each subtraction step. Only the first s row is printed, which is the divident
        // in subsequent steps, the d overlaps the next s row, so no more s rows
        for (subtractionStep in tableau.subtractionSteps) {
            // output minuend line
            val minuendRow = subtractionStep.minuendRow()
            out.append(divisorMargin).append("   ")
                .append(cellArraytoText(minuendRow, metrics.cellGap))
                .append(LF)
            // output remaining rows only if there is stuff in them
            if (minuendRow.preblanks < minuendRow.size) {
                val differenceRow = subtractionStep.differenceRow()
                val preWid = prefixWidth(minuendRow.preblanks, metrics.cellGap)
                val textSize = differenceRow.size - minuendRow.preblanks - minuendRow.postblanks
                val wid = textWidth(textSize, metrics.cellGap)
                val postWid = 0    // textWidth(countZPostBlanks, metrics.cellGap)
                out.append(divisorMargin).append("   ")
                    .append(spaces(preWid))
                    .append(repeats('─', wid))
                    .append(spaces(postWid))
                    .append(LF)
                // output d line
                out.append(divisorMargin).append("   ")
                    .append(cellArraytoText(differenceRow, metrics.cellGap))
                    .append(LF)
            }
        }
        return out
    }

    fun textWidth(width: Int, gap: Int): Int = width + gap * (width - 1)

    fun prefixWidth(width: Int, gap: Int): Int = width * (gap + 1)

    fun cellArraytoText(numb: CellRow, cellGap: Int): String {
        val bldr = StringBuilder()
        StandardCollectionUtility.SuperleaveBuilder<Cell>()
            .onElement { _, cell -> bldr.append(cell.char()) }
            .onGap { _, _, _ -> (0 until cellGap).forEach { _ -> bldr.append(BLANK) } }
            .build(numb.cells.asSequence())
        return bldr.toString()
    }
}

class TableauRenderException(message: String) : Exception(message)