package worddivision.model.builder

import worddivision.model.SubtractionCell
import worddivision.model.CellRow
import worddivision.model.SubtractionStep
import worddivision.solution.MutableCarry

import worddivision.model.builder.SubtractionStepBuildException as SSBE

class SubtractionStepBuilder(
    var minuend: CellRow? = null,
    var subtrahend: CellRow? = null,
    var difference: CellRow? = null)
{
    fun minuend(minuend: CellRow) = apply { this.minuend = minuend }
    fun subtrahend(subtrahend: CellRow) = apply { this.subtrahend = subtrahend }
    fun difference(difference: CellRow) = apply { this.difference = difference }

    fun build(): SubtractionStep {
        val m = minuend ?: throw SSBE("Missing minuend")
        val s = subtrahend ?: throw SSBE("Missing subtrahend")
        val d = difference ?: throw SSBE("Missing difference")
        var curCarry = MutableCarry(raised = false)
        val subcells = (0 until m.size)
            .map { i ->
                val nextCarry = MutableCarry(raised = false)
                val cell = SubtractionCell(m[i], s[i], d[i], curCarry, nextCarry)
                curCarry = nextCarry
                cell
            }
            .toTypedArray()
        return SubtractionStep(subcells)
    }
}

class SubtractionStepBuildException(message: String): Exception(message)