package pl.kurczews.yaml.validator

import org.assertj.core.api.Assertions.assertThat
import pl.kurczews.completion.YamlEntry

class ChainValidatorTest {

    fun should_return_success_for_correct_keys() {
        val correctEntry = YamlEntry("foo", mapOf(
                "arg1" to listOf("foo1", "bar1", "baz1"),
                "arg2" to listOf("foo2", "bar2", "baz2")
        ))

        assertThat(ChainValidator().validate(correctEntry)).isEqualTo(ValidationSuccess)
    }

    fun should_return_failure_for_invalid_keys() {
        val correctEntry = YamlEntry("foo", mapOf(
                "arg1" to listOf("foo1", "bar1", "baz1"),
                "arg2" to listOf("foo2", "bar2", "baz2"),
                "arg" to listOf("foo2", "bar2", "baz2"),
                "xarg1" to listOf("foo2", "bar2", "baz2")
        ))

        val validationResult = ChainValidator().validate(correctEntry)
        assertThat(validationResult).isInstanceOf(ValidationFailure::class.java)
        assertThat((validationResult as ValidationFailure).failingLines).isEqualTo(listOf("arg", "xarg1"))
    }
}