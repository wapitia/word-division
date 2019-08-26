package worddivision.model

interface Cell {
    /** Character of the cell, or space if it isn't letter based */
    fun char(): Char
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
    override fun char(): Char = letter.char()
    override fun solution(): Digit? = letter.solution()
}

/* com.wapitia.worddivision.model.Cell resolved to a digit */
class DigitCell(val digit: Digit) : Cell {
    override fun char(): Char = (digit + '0'.toInt()).toChar()
    override fun solution() = digit
}

object BlankCell : Cell {
    override fun char(): Char = ' '
    override fun solution() = 0
}
