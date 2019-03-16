package pl.kurczews.yaml

import pl.kurczews.common.slice2
import pl.kurczews.common.split2
import pl.kurczews.common.update
import pl.kurczews.yaml.Symbols.EXPRESSION_PREFIX
import java.util.*

object BashSplitter {

    private const val COMPLETION_SEPARATOR = ' '
    private const val OPEN_VARIABLE_BRACKET = '{'
    private const val CLOSE_VARIABLE_BRACKET = '}'
    private const val OPEN_EXPRESSION_BRACKET = '('
    private const val CLOSE_EXPRESSION_BRACKET = ')'

    /**
     * Split string by space and preserve bash ${variables} and $(substitutions) as single words despite spaces
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
                char == OPEN_VARIABLE_BRACKET -> brackets.update(CLOSE_VARIABLE_BRACKET) { it.inc() }
                char == OPEN_EXPRESSION_BRACKET -> brackets.update(CLOSE_EXPRESSION_BRACKET) { it.inc() }
                char.isClosingBracket() -> {
                    brackets.update(char) { it.dec() }
                    if (brackets.values.all { it == 0 }) {
                        return line.slice2(index)
                    }
                }
            }
        }
        throw IllegalArgumentException("Invalid expression: $line")
    }

    private fun Char.isClosingBracket(): Boolean {
        return this == CLOSE_VARIABLE_BRACKET || this == CLOSE_EXPRESSION_BRACKET
    }
}