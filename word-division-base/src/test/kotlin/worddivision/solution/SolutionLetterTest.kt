package worddivision.solution

import org.junit.Assert
import kotlin.test.Test

internal class SolutionLetterTest {

    @Test
    fun testSimpleSolutionLetter() {
        val sLetter = SolutionLetter('Q')
        System.out.println("Hey!")
        println(sLetter)
        sLetter.solve(3)
        println(sLetter)
        Assert.assertEquals(1, 2)
    }
}