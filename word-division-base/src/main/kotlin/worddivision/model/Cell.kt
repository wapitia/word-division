package com.wapitia.games.worddivision.model

interface Cell {
    /** Character of the cell, or space if it isn't letter based */
    fun char(): Char
    fun isBlank(): Boolean
    fun solution(): Digit?

    fun reduce() = reduce(this)

    companion object CellFactory {
        private val reducer = CellReduction()

        fun letterCell(letter: Letter): Cell = if (letter.isBlank()) BlankCell else LetterCell(letter)
        fun blankCell(): Cell = BlankCell
        fun digitCell(digit: Digit): Cell = DigitCell(digit.validate())
        fun reduce(cell: Cell): Cell = reducer.reduce(cell)
    }
}

private class CellReduction {
    fun reduce(cell: Cell): Cell = when (cell) {
        is LetterCell -> cell.solution()
                ?. let { DigitCell(it) }
                ?: cell
        is DigitCell -> cell
        else -> cell
    }
}

private class LetterCell(val letter: Letter) : Cell {
    override fun char(): Char = letter.char()
    override fun isBlank(): Boolean = false
    override fun solution(): Digit? = letter.solution()
}

private class DigitCell(val digit: Digit) : Cell {
    override fun char(): Char = (digit + '0'.toInt()).toChar()
    override fun isBlank(): Boolean = false
    override fun solution() = digit
}

private object BlankCell : Cell {
    override fun char(): Char = ' '
    override fun isBlank(): Boolean = true
    override fun solution() = 0
}
