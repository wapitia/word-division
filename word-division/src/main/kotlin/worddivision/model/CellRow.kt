package worddivision.model

import worddivision.model.builder.CellRowBuilder

class CellRow(val cells: List<Cell>, val preblanks: Int, val postblanks: Int) {

    val size = cells.size

    operator fun get(ix: Int) = cells[ix]

    fun textSize(): Int  = size - preblanks - postblanks

    companion object Builder {
        fun builder() = CellRowBuilder()
    }
}
