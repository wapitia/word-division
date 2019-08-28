package worddivision.app

import worddivision.model.Tableau
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
        val tableau = Tableau.textBuilder()
            .quotient("   GO")
            .divisor("YEW")
            .row("LIGHT")
            .row("LYNO")
            .row(" ENIT")
            .row(" HIIH")
            .row("  LGG")
            .build()
        val str = tableau.toString()
        System.out.println(str)
    }
}