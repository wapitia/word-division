package worddivision.model.builder

import worddivision.model.Letter
import worddivision.model.Cell
import worddivision.model.SubtractionStep
import worddivision.model.CellRow
import worddivision.model.Tableau
import worddivision.model.BlankCell
import worddivision.standard.BuildCache
import worddivision.standard.StandardTextUtility.spaces

import worddivision.model.builder.TableauBuildException as TBE
import worddivision.model.builder.TableauBuildProblem.MissingQuotient
import worddivision.model.builder.TableauBuildProblem.MissingDividend
import worddivision.model.builder.TableauBuildProblem.MissingDivisor
import worddivision.model.builder.TableauBuildProblem.RowTooLong
import worddivision.model.builder.TableauBuildProblem.MissingRemainderRow

import kotlin.math.max

class TextTableauBuilder(
    var quotient: String? = null,
    var divisor: String? = null,
    val rows: MutableList<String> = mutableListOf()
) {
    fun quotient(quotient: String) = apply { this.quotient = quotient }
    fun divisor(divisor: String) = apply { this.divisor = divisor }
    fun row(row: String) = apply { rows.add(row) }

    fun build(): Tableau {
        val accums = Accums()

        var quotientStr = quotient ?: throw TBE(MissingQuotient, "Missing quotient")
        var divisorStr = divisor ?: throw TBE(MissingDivisor, "Missing divisor")
        val dividendStr: String = rows.getOrNull(0) ?: throw TBE(MissingDividend, "Missing dividend (row 0)")
        val width = dividendStr.length
        if (width < 1) throw TBE(MissingDividend, "Missing dividend (row 0)")

        val dividendSubrow = buildSubrow(dividendStr, width, accums)
        val divisorSubrow = buildSubrow(divisorStr, divisorStr.length, accums)
        val quotientSubrow = buildSubrow(quotientStr, width, accums)
        val subtractionSteps: Array<SubtractionStep> = buildSubSteps(rows, width, accums)
        return Tableau(accums.letters(), width,
            dividendSubrow, divisorSubrow, quotientSubrow, subtractionSteps)
    }

    private fun buildSubSteps(rows: List<String>, width: Int, accum: Accums): Array<SubtractionStep> {

        // helper functions
        fun areOddRows() = rows.size % 2 == 1

        fun isSubStepAt(mindex: Int) = mindex + 2 < rows.size

        fun normalize(rowStr: String): String = when {
            rowStr.length > width -> throw TBE(RowTooLong, "Row too long. ")
            rowStr.length < width -> rowStr + spaces(width - rowStr.length)
            else                  -> rowStr
        }

        fun subrowOf(rowIx: Int): CellRow {
            val str = rows[rowIx].let { normalize(it) }
            val subrow = buildSubrow(str, width, accum)
            return subrow
        }

        // build algorithm
        if (! areOddRows())
            throw TBE(MissingRemainderRow, "Expected a remainder row")
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
        return stepList.toTypedArray()
    }

    private fun buildSubrow(str: String, width: Int, accum: Accums): CellRow {
        val blankPrefix = max(0, width - str.length)
        val cells: Sequence<Cell> = List(width) { i ->
            if (i < blankPrefix) BlankCell
            else accum.cellOfChar(str[i - blankPrefix])
        }.asSequence()
        return CellRowBuilder().cells(cells).build()
    }
}

internal class Accums {
    val letterCache: BuildCache<Char, Letter> = BuildCache { c -> MutableLetter(c) }
    val cellCache: BuildCache<Letter,Cell> = BuildCache { ltr ->  Cell.letterCell(ltr) }

    fun letters(): Array<Letter> = cellCache.cache.keys
        .filter( Letter::hasChar )
        .sortedBy( Letter::char )
        .toTypedArray()

    fun cellOfChar(c: Char) = cellCache[letterCache[c]]
}

enum class TableauBuildProblem {
    MissingDividend,
    MissingDivisor,
    MissingQuotient,
    MissingRemainderRow,
    RowTooLong
}

class TableauBuildException(val buildProblem: TableauBuildProblem, message: String): Exception(message)
