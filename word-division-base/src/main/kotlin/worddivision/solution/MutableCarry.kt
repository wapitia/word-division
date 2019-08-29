package com.wapitia.games.worddivision.solution

import com.wapitia.games.worddivision.model.Carry

class MutableCarry
    // primary constructor
    (private var raised: Boolean): Carry
{
    constructor(): this(raised = false)

    override fun isRaised() = raised
    override fun raise(raiseIt: Boolean) {
        this.raised = raiseIt
    }
}