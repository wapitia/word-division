package worddivision.model.builder

import worddivision.model.Cell
import worddivision.model.CellRow
import worddivision.standard.StandardTextUtility.BLANK

class CellRowBuilder(seq: Sequence<Cell> = sequenceOf<Cell>()) {

    val cells: MutableList<Cell> = mutableListOf<Cell>()

    init {
        for (cell in seq) cells.add(cell)
    }

    fun cell(cell: Cell) = apply { this.cells.add(cell)}
    fun cells(cells: Sequence<Cell>) = apply { this.cells.addAll(cells) }
    fun cells(cells: List<Cell>) = apply { this.cells.addAll(cells) }

    fun build(): CellRow {
        var preBlanks = 0
        var postBlanks = 0
        var blankSoFar = true
        for (cell in cells) {
            var cellIsBlank = cell.char() == BLANK
            if (cellIsBlank) {
                if (blankSoFar) ++preBlanks
                ++postBlanks
            } else {
                blankSoFar = false
                postBlanks = 0
            }
        }
        if (blankSoFar) postBlanks = 0
        return CellRow(cells.toList(), preBlanks, postBlanks)
    }
}
