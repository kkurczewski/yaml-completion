package pl.kurczews.yaml.validator

import pl.kurczews.completion.YamlEntry

class ChainValidator {
    private val keyPattern = Regex("arg[0-9]+")

    companion object {
        private const val INVALID_KEY = "Invalid key, expected format: `arg[0-9]+`"
        private const val AMBIGUOUS_COMPLETION: String = "Every position should have exactly one completion chain"
    }

    fun validate(payload: YamlEntry<Map<String, List<String>>>): ValidationResult {
        val invalidPatternKeys = payload.entries.keys.filter { it.notMatches(keyPattern) }
        val ambiguousCompletions = payload.entries.filter { it.value.size == 1 }.map { it.key }

        return when {
            invalidPatternKeys.isNotEmpty() -> ValidationFailure(INVALID_KEY, invalidPatternKeys)
            ambiguousCompletions.isNotEmpty() -> ValidationFailure(AMBIGUOUS_COMPLETION, ambiguousCompletions)
            else -> ValidationSuccess
        }
    }

    private fun String.notMatches(regex: Regex): Boolean = !this.matches(regex)
}