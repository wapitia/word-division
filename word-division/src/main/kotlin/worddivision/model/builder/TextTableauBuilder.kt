package worddivision.model.builder

import worddivision.model.Letter
import worddivision.model.Cell
import worddivision.model.SubtractionStep
import worddivision.model.CellRow
import worddivision.model.Tableau
import worddivision.model.BlankCell
import worddivision.standard.StandardTextUtility.isBlank
import worddivision.standard.StandardTextUtility.spaces
import worddivision.standard.StandardCollectionUtility.mapFetch

class TextTableauBuilder(
    var quotient: String? = null,
    var divisor: String? = null,
    val rows: MutableList<String> = mutableListOf<String>()
) {
    fun quotient(quotient: String) = apply { this.quotient = quotient }
    fun divisor(divisor: String) = apply { this.divisor = divisor }
    fun row(row: String) = apply { rows.add(row) }

    fun build(): Tableau {
        val accum = LetterCellAccum()

        var quotientStr = quotient ?: throw TableauBuildException(TableauBuildProblem.MissingQuotient, "Missing quotient")
        var divisorStr = divisor ?: throw TableauBuildException(TableauBuildProblem.MissingDivisor, "Missing divisor")
        val dividendStr: String = rows.getOrNull(0) ?: throw TableauBuildException(TableauBuildProblem.MissingDividend, "Missing dividend (row 0)")
        val width = dividendStr.length;
        if (width < 1) throw TableauBuildException(TableauBuildProblem.MissingDividend, "Missing dividend (row 0)")

        val dividendSubrow = buildSubrow(dividendStr, width, accum)
        val divisorSubrow = buildSubrow(divisorStr, divisorStr.length, accum)
        val quotientSubrow = buildSubrow(quotientStr, width, accum)
        val subtractionSteps: List<SubtractionStep> = buildSubSteps(rows, width, accum)
        return Tableau(accum.letters(), width, dividendSubrow, divisorSubrow, quotientSubrow, subtractionSteps)
    }

    private fun buildSubSteps(rows: List<String>, width: Int, accum: LetterCellAccum): List<SubtractionStep> {

        // helper functions
        fun areOddRows() = rows.size % 2 == 1

        fun isSubStepAt(mindex: Int) = mindex + 2 < rows.size

        fun normalize(rowStr: String): String = when {
            rowStr.length > width -> throw TableauBuildException(TableauBuildProblem.RowTooLong, "Row too long. ")
            rowStr.length < width -> rowStr + spaces(width - rowStr.length)
            else                  -> rowStr
        }

        fun subrowOf(rowIx: Int): CellRow {
            val str = rows.get(rowIx).run { normalize(this) }
            val subrow = buildSubrow(str, width, accum)
            return subrow
        }

        // build algorithm
        if (! areOddRows())
            throw TableauBuildException(TableauBuildProblem.MissingRemainderRow, "Expected a remainder row")
        val stepList = mutableListOf<SubtractionStep>()
        var ix = 0
        while (isSubStepAt(ix)) {
            val step: SubtractionStep = SubtractionStep.builder()
                .minuend(subrowOf(ix))
                .subtrahend(subrowOf(ix + 1))
                .difference(subrowOf(ix + 2))
                .build()
            stepList.add(step)

            ix += 2  // difference of this row overlaps minuend of previous row
        }
        return stepList.toList()
    }

    private fun buildSubrow(str: String, width: Int, accum: LetterCellAccum): CellRow {
        val blankPrefix = kotlin.math.max(0, width - str.length)
        val result: List<Cell> = List<Cell>(width) { i ->
            if (i < blankPrefix)
                BlankCell
            else {
                val c: Char = str.get(i - blankPrefix)
                val cell = accum.fetchCellFromChar(c)
                cell
            }
        }
        return CellRowBuilder(result.asSequence()).build()
    }
}

internal class LetterCellAccum(val letterMap: MutableMap<Char,Letter> = HashMap<Char,Letter>(),
                               val cellMap: MutableMap<Letter,Cell> = HashMap<Letter,Cell>() )
{
    fun letters(): Array<Letter> = cellMap.keys.filter{ ltr -> ! isBlank(ltr.char())}.toTypedArray().apply { sortBy { ltr -> ltr.char() } }
    fun fetchLetter(c: Char): Letter = mapFetch<Char,Letter>(letterMap, c) { _ -> MutableLetter(c) }
    fun fetchCell(letter: Letter): Cell = mapFetch<Letter,Cell>(cellMap, letter) { _ -> Cell.letterCell(letter) }
    fun fetchCellFromChar(c: Char): Cell = fetchCell(fetchLetter(c))
}

enum class TableauBuildProblem {
    MissingDividend,
    MissingDivisor,
    MissingQuotient,
    MissingRemainderRow,
    RowTooLong
}

class TableauBuildException(val buildProblem: TableauBuildProblem, message: String): Exception(message)
