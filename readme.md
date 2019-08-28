# Word Division Application

Application to create and solve Word Division problems, such as the
Word Arithmetic pages in Dell Crossword puzzles:
<pre>
                  G O
          ┌───────────
    M E W │ L I G H T
            L M N O
            ───────
              E N I T
              H I I H
              ────────
                L G G
</pre>

This is a long division puzzle, having ten digits represented
by ten unique letters, whose solution 0 through 9 spell out a word
or phrase. The solution involves finding the unique decimal digit
solution of each letter by using long division techniques.

## Terminology

<B>Dividend</B> The number that will be divided, "L I G H T" in the example above.
<P>
<B>Divisor</B> Divides into the Divident, " M E W" in the example above.

<P>
<B>Quotient</B> The result of the division, without the remainder. "G O" in the example.

<P>
<B>Remainder</B> "L G G" in the example above.

<P>
<B>Subtraction Step</B> One step in the long-division process, a row of subtraction.
                        There are two subtraction steps in the example above:
<PRE>
       L I G H          E N I T
       L M N O          H I I H
       ───────          ───────
         E N I            L G G
</PRE>
There is one subtraction step for each digit of the Quotient.
Note that each step vertically overlaps other steps, but on the other hand,
is only a horizontal subslice of the width of the overall dividend .

<P>
<B>com.wapitia.worddivision.model.Subcell</B> A Subtraction cell focuses on a single vertical column from
a Subtraction step, such as:
<PRE>
        I
        I
        ──
        G
</PRE>
These digits, along with any carry or borrow, can be an isolated part of the solution.
