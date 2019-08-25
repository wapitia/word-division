package com.wapitia.worddivision.model.render

import com.wapitia.worddivision.model.Tableau

object TableauRenderer {

    fun toString(tableau: Tableau): String = writeTableau(tableau, StringBuilder(), DefaultMetrics).toString()

    data class WriteMetrics(val cellGap: Int)

    val DefaultMetrics = WriteMetrics(cellGap = 1)

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

     */
    inline fun textWidth(width: Int, gap: Int): Int = width + gap * (width - 1)
    fun writeTableau(tableau: Tableau, out: StringBuilder, metx: WriteMetrics): StringBuilder {
        // dividend/divisor/quotient widths are the text width including the gap, and defines the overall matrix size
        val dividendWidth = textWidth(tableau.width, metx.cellGap)
        val quotientWidth = textWidth(tableau.quotient.size, metx.cellGap)
        val divisorWidth = textWidth(tableau.width, metx.cellGap)

        // easiest to paint a tableau into a 2D character grid and then reduce it down into a string
        // than to keep count of widths and negative offsets
//        val matrix: Array<Array<Char>> = Array<Array<Char>>(2) {}
        return out
    }


}
