package pl.kurczews.yaml.validator

sealed class ValidationResult

object ValidationSuccess : ValidationResult()

data class ValidationFailure(val msg: String, val failingLines: List<String>) : ValidationResult()