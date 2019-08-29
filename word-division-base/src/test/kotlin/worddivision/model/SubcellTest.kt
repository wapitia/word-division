package worddivision.model

import kotlin.test.Test

/*
 *                   G O
 *           ┌───────────
 *     M E W │ L I G H T
 *             L M N O
 *             ───────
 *               E N I T
 *               H I I H
 *               ────────
 *                 L G G
 */
internal class SubcellTest {

    @Test
    fun buildEx1() {
        val tableau = Tableau.textBuilder2()
            .quotient("   GO")
            .divisor("YEW")
            .subtraction("LIGHT", "LYNO")
            .subtraction(" ENIT", " HIIH")
            .remainder("  LGG")
            .build()
        val str = tableau.toString()
        println(str)
    }
}