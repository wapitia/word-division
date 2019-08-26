package worddivision.standard

object StandardTextUtility {

    const val LF: Char = '\u000a'

    const val BLANK: Char = ' '

    fun isBlank(ch: Char) = ch == BLANK

    fun repeats(ch: Char, size: Int): String = String( CharArray(size) { _ -> ch } )

    fun spaces(size: Int) = repeats(BLANK, size)
}