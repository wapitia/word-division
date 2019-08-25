package com.wapitia.worddivision.model

interface Cell {
    fun solution(): Digit?

    companion object CellFactory {

        fun letterCell(letter: Letter) = LetterCell(letter)
        fun blankCell() = DigitCell(0)
    }
}

class CellReduction {
    fun reduce(cell: Cell): Cell = when (cell) {
        is LetterCell -> cell.solution()
                ?. let { it -> DigitCell(it) }
                ?: cell
        is DigitCell -> cell
        else -> cell
    }
}

class LetterCell(val letter: Letter) : Cell {
    override fun solution(): Digit? = letter.solution()
}

/* com.wapitia.worddivision.model.Cell resolved to a digit */
class DigitCell(val digit: Digit) : Cell {
    override fun solution() = digit
}

object BlankCell : Cell {
    override fun solution() = 0
}
