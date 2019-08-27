package worddivision.model.builder

import worddivision.model.Subcell
import worddivision.model.CellRow
import worddivision.model.SubtractionStep

class SubtractionStepBuilder(
    var minuend: CellRow? = null,
    var subtrahend: CellRow? = null,
    var difference: CellRow? = null)
{
    fun minuend(minuend: CellRow) = apply { this.minuend = minuend }
    fun subtrahend(subtrahend: CellRow) = apply { this.subtrahend = subtrahend }
    fun difference(difference: CellRow) = apply { this.difference = difference }

    fun build(): SubtractionStep {
        val m = minuend ?: throw SubtractionStepBuildException("Missing minuend")
        val s = subtrahend ?: throw SubtractionStepBuildException("Missing subtrahend")
        val d = difference ?: throw SubtractionStepBuildException("Missing difference")
        var curCarry = MutableCarry(raised = false)
        val subcells = (0 until m.size)
            .map { i ->
                val nextCarry = MutableCarry(raised = false)
                val cell = Subcell(m[i], s[i], d[i], curCarry, nextCarry)
                curCarry = nextCarry
                cell
            }
            .asSequence()
            .toList()
        return SubtractionStep(subcells)
    }
}

class SubtractionStepBuildException(message: String): Exception(message)