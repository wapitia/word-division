package worddivision.model

import java.lang.RuntimeException

/**
 * Indexes one of the three lines of subtraction
 */
enum class SubtractionRole { SUBTRAHEND, MINUEND, DIFFERENCE }

class SubtractionCell(val subtrahend: Cell, val minuend: Cell, val difference: Cell, val borrow: Carry, val carry: Carry) {
    companion object Ref {
        val refMap: Map<SubtractionRole,(SubtractionCell) -> Cell>  = mapOf(
                SubtractionRole.SUBTRAHEND to SubtractionCell::subtrahend,
                SubtractionRole.MINUEND to SubtractionCell::minuend,
                SubtractionRole.DIFFERENCE to SubtractionCell::difference
        )

        fun cellSequenceOf(role:SubtractionRole, subcells: Array<SubtractionCell>): Sequence<Cell> =
            subcells.map(refMap[role] ?: throw  RuntimeException()).asSequence()
    }
}
