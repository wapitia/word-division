package worddivision.model

/**
 * For the Word Division Models, digits are in the range 0..9
 */
typealias Digit = Int

/**
 * Returns true when the digit is in the range 0 .. 9 inclusive
 */
fun Digit.isInRange(): Boolean = this in 0..9

/**
 * If the [Digit] is in the range 0 .. 9, just return it, otherwise throw a [DigitRangeException]
 */
fun Digit.validate(): Digit = when {
    isInRange() -> this
    else -> throw DigitRangeException(this)
}

/**
 * Exception thrown when a digit is out of range
 */
class DigitRangeException(offendingValue: Int) : Exception("Digit `$offendingValue` is out of range:  must be within 0..9")
