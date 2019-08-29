package com.wapitia.games.worddivision.model.builder

import com.wapitia.games.worddivision.model.SubtractionCell
import com.wapitia.games.worddivision.model.CellRow
import com.wapitia.games.worddivision.model.SubtractionStep
import com.wapitia.games.worddivision.solution.MutableCarry

import com.wapitia.games.worddivision.model.builder.SubtractionStepBuildException as SSBE

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