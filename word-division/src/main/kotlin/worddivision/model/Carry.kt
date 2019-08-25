package worddivision.model

interface Carry {
    fun isRaised(): Boolean
    companion object Factory {
        fun raised(): Carry = RaisedCarry
        fun lowered(): Carry = LoweredCarry
        protected object RaisedCarry : Carry {
            override fun isRaised() = true
        }
        protected object LoweredCarry : Carry {
            override fun isRaised() = false
        }
    }
}

