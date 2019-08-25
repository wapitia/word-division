package com.wapitia.worddivision.model.builder

import com.wapitia.worddivision.model.*
import com.wapitia.worddivision.model.SubtractionStep
import com.wapitia.worddivision.model.Tableau

open class MutableLetter(val char: Char): Letter {
    var solution: Digit? = null
    override fun char() = char
    override fun solution(): Digit? = solution
}

object Blank : MutableLetter(BLANK) {
    override fun solution() = 0
}

/**
 * Build a valid immutable Tableau.
 * data class Tableau(
        val letters: Array<Letter>,
        val width: Int,
        val dividend:  Array<Cell>,
        val divisor:  Array<Cell>,
        val quotient:  Array<Cell>,
        val subtractionSteps: Array<SubtractionStep>)
 */
class TextTableauBuilder(
    var width: Int? = null,
    var quotient: String? = null,
    var divisor: String? = null,
    val rows: MutableList<String> = ArrayList()
) {
    fun width(width: Int) = apply { this.width = width }
    fun quotient(quotient: String) = apply { this.quotient = quotient }
    fun divisor(divisor: String) = apply { this.divisor = divisor }
    fun row(row: String) = apply {
        rows.add(row)
    }

    fun build(): Tableau = validateAndBuild()

    class LetterCellAccum(val letterMap: MutableMap<Char,Letter> = HashMap<Char,Letter>(),
                          val cellMap: MutableMap<Letter,Cell> = HashMap<Letter,Cell>() )
    {
        fun letters(): Array<Letter> = cellMap.keys.toTypedArray().apply { sortBy { ltr -> ltr.char() } }

        fun fetchLetter(c: Char): Letter = letterMap.get(c).run {
            if (this == null) {
                val res = MutableLetter(c)
                letterMap.put(c, res)
                res
            }
            else this
        }

        fun fetchCell(letter: Letter): Cell = cellMap.get(letter).run {
            if (this == null) {
                val res = Cell.letterCell(letter)
                cellMap.put(letter, res)
                res
            }
            else this
        }

        fun fetchCellFromChar(c: Char): Cell = fetchCell(fetchLetter(c))
    }

    protected fun validateAndBuild(): Tableau {

        val accum: LetterCellAccum = LetterCellAccum()

        // width of dividend string sets the overall width of tableau
        val dividendStr: String = rows.getOrNull(0) ?: ""
        val width: Int = dividendStr.length;
        if (width < 1)
            throw TableauBuildException(TableauBuildProblem.MissingDividend, "Missing dividend (row 0)")

        val dividend: Array<Cell> = cellArrayOf(dividendStr, width, accum)
        val divsr: Array<Cell> = cellArrayOf(
            divisor
                ?: throw TableauBuildException(TableauBuildProblem.MissingDivisor, "Missing divisor"),
            width, accum)
        val quotnt: Array<Cell> = cellArrayOf(
            quotient
                ?: throw TableauBuildException(TableauBuildProblem.MissingQuotient, "Missing quotient"),
            width, accum)
        val subtractionSteps: Array<SubtractionStep> = buildSubSteps(rows, width, accum)
        val letters: Array<Letter> = accum.letters()
        return Tableau(letters, width, dividend, divsr, quotnt, subtractionSteps)
    }

    fun hasOddSize(list: List<Any>) = list.size % 2 == 1

    fun rowHasSubStepAt(mindex: Int, list: List<Any>) = mindex + 2 < list.size

    protected fun buildSubSteps(rows: List<String>, width: Int, accum: LetterCellAccum): Array<SubtractionStep> {

        val MinuendIX = 0
        val SubtrahendIX = 1
        val DifferenceIX = 2

        fun normalize(minuend: String): String {
            if (minuend.length > width)
                throw TableauBuildException(TableauBuildProblem.RowTooLong, "Row too long. ")
            if (minuend.length < width)
                return minuend + spaces(width - minuend.length)
            return minuend
        }

        if (! hasOddSize(rows))
            throw TableauBuildException(TableauBuildProblem.MissingRemainderRow, "Expected a remainder row")

        val stepList: MutableList<SubtractionStep> = mutableListOf<SubtractionStep>()
        var ix = 0
        while ( rowHasSubStepAt(ix, rows)) {

            val minuendStr = rows.get(ix + MinuendIX).run { normalize(this) }
            val minuend = cellArrayOf(minuendStr, width, accum)

            val subtrahendStr = rows.get(ix + SubtrahendIX).run { normalize(this) }
            val subtrahend = cellArrayOf(subtrahendStr, width, accum)

            val differenceStr = rows.get(ix + DifferenceIX).run { normalize(this) }
            val difference = cellArrayOf(differenceStr, width, accum)

            val step: SubtractionStep = buildStep(minuend, subtrahend, difference)
            stepList.add(step)

            ix += 2  // difference of this row overlaps minuend of previous row
        }
        val result: Array<SubtractionStep> = stepList.toTypedArray()
        return result
    }

    fun buildStep(minuend: Array<Cell>, subtrahend: Array<Cell>, difference: Array<Cell>): SubtractionStep {
        val width = minuend.size
        val accum: MutableList<Subcell> = mutableListOf<Subcell>()
        var leftCarry = Carry.lowered()
        IntRange(0, width).forEach { i ->
            val mCell = minuend[i]
            val sCell = subtrahend[i]
            val dCell = difference[i]
            val rightCarry = Carry.lowered()
            val cell = Subcell(mCell, sCell, dCell, leftCarry, rightCarry)
            accum.add(cell)
        }
        val subcells: Array<Subcell> = accum.toTypedArray()
        return SubtractionStep(subcells)
    }

    companion object CommonTextUtils {

        private var SPACES = "                "
        private fun ensureSpaces(size: Int): String {
            while (SPACES.length < size) SPACES += SPACES
            return SPACES
        }

        fun spaces(size: Int) = ensureSpaces(size).substring(0, size)
    }

    protected fun cellArrayOf(str: String, width: Int, accum: LetterCellAccum)
    : Array<Cell>
    {
        val blankPrefix = kotlin.math.max(0, width - str.length)
        val result: Array<Cell> = Array<Cell>(width) { i ->
            if (i < blankPrefix)
                BlankCell
            else {
                val strIx = i - blankPrefix
                val c: Char = str.get(strIx)
                val cell: Cell = accum.fetchCellFromChar(c)
                cell
            }
        }
        return result
    }
}

enum class TableauBuildProblem {
    MissingDividend,
    MissingDivisor,
    MissingQuotient,
    MissingRemainderRow,
    RowTooLong
}

class TableauBuildException(
        val buildProblem: TableauBuildProblem,
        message: String
): Exception(message)
