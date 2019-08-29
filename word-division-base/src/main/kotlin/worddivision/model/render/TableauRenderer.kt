package com.wapitia.games.worddivision.model.render

import com.wapitia.games.worddivision.model.Cell
import com.wapitia.games.worddivision.model.Tableau
import com.wapitia.games.worddivision.model.CellRow
import com.wapitia.games.worddivision.model.Letter
import com.wapitia.games.worddivision.standard.StandardCollectionUtility
import com.wapitia.games.worddivision.standard.StandardTextUtility.spaces
import com.wapitia.games.worddivision.standard.StandardTextUtility.repeats
import com.wapitia.games.worddivision.standard.StandardTextUtility.BLANK
import com.wapitia.games.worddivision.standard.StandardTextUtility.LF

object TableauRenderer {

    data class PrintMetrics (
        val cellGap: Int = 1,
        val showCandiatesTop: Boolean = false,

        //  strings and characters for framing the tableau
        val marginQuotientLine: String      = "   ",
        val marginHorizontalSepLine: String = " ┌─",
        val horizontalSepChar: Char         = '─',
        val marginDividendLine: String      = " │ ",
        val marginMinuendLine: String       = "   ",
        val marginSubtractLine: String      = "   ",
        val marginRemainderLine: String     = "   "
        )

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

        // dividend/divisor/quotient widths are the text width including the gap, and defines the overall matrix size
        val dividendWidth = textWidth(tableau.width, metrics.cellGap)
        val quotientWidth = textWidth(tableau.quotient.size, metrics.cellGap)
        val divisorWidth = textWidth(tableau.divisor.size, metrics.cellGap)

        // space prefixes
        val divisorMargin = spaces(divisorWidth)
        val quotientPrefix = spaces(dividendWidth - quotientWidth)

        // display candidate letterCache at top
        if (metrics.showCandiatesTop) {
            out.append("CANDIDATES:").append(LF)
            tableau.letters
                .filter(Letter::hasChar)
                .sortedBy(Letter::char)
                .forEach { ltr -> out.append(ltr.char()).append(" ")
            }
            out.append(LF)
            out.append(LF)
        }

        // output quotient line
        out.append(divisorMargin)
            .append(metrics.marginQuotientLine)
            .append(quotientPrefix)
            .append(cellArraytoText(tableau.quotient, metrics.cellGap))
            .append(LF)

        // output horizontal separator line
        out.append(divisorMargin)
            .append(metrics.marginHorizontalSepLine)
            .append(repeats(metrics.horizontalSepChar, dividendWidth))
            .append(LF)

        // output divisor / dividend line
        out.append(cellArraytoText(tableau.divisor, metrics.cellGap))
            .append(metrics.marginDividendLine)
            .append(cellArraytoText(tableau.dividend, metrics.cellGap))
            .append(LF)

        // print the m and d rows of each subtraction step. Only the first s row is printed, which is the divident
        // in subsequent steps, the d overlaps the next s row, so no more s rows
        for (subtractionStep in tableau.subtractionSteps) {
            // output minuend line
            val minuendRow = subtractionStep.minuendRow()
            out.append(divisorMargin)
                .append(metrics.marginMinuendLine)
                .append(cellArraytoText(minuendRow, metrics.cellGap))
                .append(LF)

            // output remaining rows only if there is stuff in them
            if (minuendRow.isOccupied()) {
                val differenceRow = subtractionStep.differenceRow()
                val preWid = prefixWidth(minuendRow.preblanks, metrics.cellGap)
                val textSize = differenceRow.size - minuendRow.preblanks - minuendRow.postblanks
                val wid = textWidth(textSize, metrics.cellGap)
                val postWid = 0    // textWidth(countZPostBlanks, metrics.cellGap)
                out.append(divisorMargin).append(metrics.marginSubtractLine)
                    .append(spaces(preWid))
                    .append(repeats(metrics.horizontalSepChar, wid))
                    .append(spaces(postWid))
                    .append(LF)
                // output remainder line
                out.append(divisorMargin).append(metrics.marginRemainderLine)
                    .append(cellArraytoText(differenceRow, metrics.cellGap))
                    .append(LF)
            }
        }
        return out
    }

    fun textWidth(width: Int, gap: Int) = width + gap * (width - 1)

    fun prefixWidth(width: Int, gap: Int) = width * (gap + 1)

    fun cellArraytoText(numb: CellRow, cellGap: Int): String {
        val bldr = StringBuilder()
        StandardCollectionUtility.SuperleaveBuilder<Cell>()
            .onElement { _, cell -> bldr.append(cell.char()) }
            .onGap { _, _, _ -> (0 until cellGap).forEach { _ -> bldr.append(BLANK) } }
            .build(numb.cells.asSequence())
        return bldr.toString()
    }
}
