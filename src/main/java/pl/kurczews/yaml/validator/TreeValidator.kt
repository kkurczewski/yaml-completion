package pl.kurczews.yaml.validator

import pl.kurczews.completion.YamlEntry
import pl.kurczews.yaml.BashSplitter
import pl.kurczews.yaml.Symbols.EXPRESSION_PREFIX
import pl.kurczews.yaml.Symbols.EXPRESSION_TAG_PREFIX

class TreeValidator {

    companion object {
        private const val MISPLACED_EXPRESSION = "Expression is allowed only as last argument of completion chain"
    }

    fun validate(payload: YamlEntry<List<String>>): ValidationResult {
        val misplacedExpressions = getLinesWithMisplacedExpression(payload)

        return when {
            misplacedExpressions.isNotEmpty() -> ValidationFailure(MISPLACED_EXPRESSION, misplacedExpressions)
            else -> ValidationSuccess
        }
    }

    private fun getLinesWithMisplacedExpression(payload: YamlEntry<List<String>>): List<String> {
        return payload.entries
                .withIndex()
                .filter { hasMisplacedExpression(BashSplitter.split(it.value)) }
                .map { "${it.index}: ${it.value}" }
    }

    private fun hasMisplacedExpression(words: List<String>): Boolean {
        return words.indexOfFirst { it.isExpression() } != words.lastIndex
    }

    private fun String.isExpression(): Boolean = this.startsWith(EXPRESSION_PREFIX).or(this.startsWith(EXPRESSION_TAG_PREFIX))
}