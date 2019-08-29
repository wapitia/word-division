package com.wapitia.games.worddivision.model

import com.wapitia.games.worddivision.model.builder.CellRowBuilder

class CellRow(val cells: List<Cell>, val preblanks: Int, val postblanks: Int) {

    val size = cells.size

    operator fun get(ix: Int) = cells[ix]

    fun textSize() = size - preblanks - postblanks

    fun isOccupied() = preblanks < size

    companion object Builder {
        fun builder() = CellRowBuilder()
        fun build(cells: Sequence<Cell>) = builder().cells(cells).build()
    }
}
