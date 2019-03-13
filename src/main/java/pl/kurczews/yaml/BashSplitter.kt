package pl.kurczews.yaml

import pl.kurczews.common.slice2
import pl.kurczews.common.split2
import pl.kurczews.common.update
import java.util.*

class BashSplitter {

    companion object {
        const val EXPRESSION_PREFIX = '$'
        private const val COMPLETION_SEPARATOR = ' '
    }

    /**
     * Splits bash ${variables} and $(substitutions) as single words despite spaces
     */
    fun split(line: String): List<String> {
        return when {
            line.contains(EXPRESSION_PREFIX) -> processLineWithExpression(line)
            else -> line.split(COMPLETION_SEPARATOR)
        }
    }

    private fun processLineWithExpression(initialLine: String): List<String> {
        val words = mutableListOf<String>()
        val stack = Stack<String>()
        stack.push(initialLine)
        while (stack.isNotEmpty()) {
            val line = stack.pop()
            val (word, leftovers) = when {
                line.startsWith(EXPRESSION_PREFIX) -> extractExpression(line)
                else -> line.split2(COMPLETION_SEPARATOR)
            }
            words.add(word)
            if (leftovers.isNotEmpty()) {
                stack.push(leftovers.trimStart())
            }
        }
        return words
    }

    private fun extractExpression(line: String): Pair<String, String> {
        val brackets = mutableMapOf<Char, Int>().withDefault { 0 }
        for ((index, char) in line.withIndex()) {
            when {
                char == '{' -> brackets.update('}') { it.inc() }
                char == '(' -> brackets.update(')') { it.inc() }
                char.isClosingBracket() -> {
                    brackets.update(char) { it.dec() }
                    if (brackets.values.all { it == 0 }) {
                        return line.slice2(index)
                    }
                }
            }
        }
        throw IllegalArgumentException("Couldn't extract expression for: $line")
    }

    private fun Char.isClosingBracket(): Boolean {
        return this == '}' || this == ')'
    }
}
