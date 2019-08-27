package worddivision.model

class Subcell(val s: Cell, val m: Cell, val d: Cell, val b: Carry, val c: Carry)

enum class SubractionRole(val cellof: (Subcell) -> Cell) {
    /** Subtrahend */
    S(Subcell::s),
    /* Minuend */
    M(Subcell::m),
    /* Difference */
    D(Subcell::d)
}
