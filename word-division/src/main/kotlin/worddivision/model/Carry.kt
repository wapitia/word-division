package com.wapitia.worddivision.model

interface Carry {
    fun isRaised(): Boolean
    companion object Factory {
        fun raised() = RaisedCarry
        fun lowered() = LoweredCarry
    }
}

object RaisedCarry : Carry {
    override fun isRaised() = true
}

object LoweredCarry : Carry {
    override fun isRaised() = false
}