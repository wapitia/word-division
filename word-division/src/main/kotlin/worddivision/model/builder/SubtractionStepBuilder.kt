package worddivision.model.builder

import worddivision.model.Subcell
import worddivision.model.Subrow
import worddivision.model.SubtractionStep

class SubtractionStepBuilder(
    var minuend: Subrow? = null,
    var subtrahend: Subrow? = null,
    var difference: Subrow? = null)
{
    fun minuend(minuend: Subrow) = apply { this.minuend = minuend }
    fun subtrahend(subtrahend: Subrow) = apply { this.subtrahend = subtrahend }
    fun difference(difference: Subrow) = apply { this.difference = difference }

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
            .toTypedArray()
        return SubtractionStep(subcells)
    }
}

class SubtractionStepBuildException(message: String): Exception(message)