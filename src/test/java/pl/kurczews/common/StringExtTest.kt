package pl.kurczews.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ConsumeUntilTest {

    @Test
    fun should_split_string_by_given_condition() {
        val (first, second) = "foo bar baz".split2(' ')
        assertThat(first).isEqualTo("foo")
        assertThat(second).isEqualTo("bar baz")
    }

    @Test
    fun should_split_string_by_given_condition_2() {
        val (first, second) = "foo bar baz".slice2(3)
        assertThat(first).isEqualTo("foo ")
        assertThat(second).isEqualTo("bar baz")
    }
}