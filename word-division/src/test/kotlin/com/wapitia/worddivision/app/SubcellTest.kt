package worddivision.app

import worddivision.model.Tableau
import kotlin.test.Test

/*
 *                   G O
 *           ┌───────────
 *     Y E W │ L I G H T
 *             L Y N O
 *             ───────
 *               E N I T
 *               H I I H
 *               ────────
 *                 L G G
 */

//import kotlin.test.Test
//import kotlin.test.assertTrue

internal class SubcellTest {

    @Test
    fun buildEx1() {
        val tableau = Tableau.builder()
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