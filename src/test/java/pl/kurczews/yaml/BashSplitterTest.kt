package pl.kurczews.yaml

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class BashSplitterTest {

    private val bashSplitter = BashSplitter()

    @Test
    fun parse_simple_line() {
        assertThat(bashSplitter.split("ab cd ef")).containsExactly("ab", "cd", "ef")
    }

    @Test
    fun parse_line_with_expression() {
        assertThat(bashSplitter.split("$(ls ~) ab cd")).containsExactly("$(ls ~)", "ab", "cd")
        assertThat(bashSplitter.split("ab $(ls ~) cd")).containsExactly("ab", "$(ls ~)", "cd")
        assertThat(bashSplitter.split("ab cd $(ls ~)")).containsExactly("ab", "cd", "$(ls ~)")
    }

    @Test
    fun parse_line_with_variable() {
        assertThat(bashSplitter.split("\${foo} ab cd")).containsExactly("\${foo}", "ab", "cd")
        assertThat(bashSplitter.split("ab \${foo} cd")).containsExactly("ab", "\${foo}", "cd")
        assertThat(bashSplitter.split("ab cd \${foo}")).containsExactly("ab", "cd", "\${foo}")
    }

    @Test
    fun parse_line_with_nested_expression() {
        assertThat(bashSplitter.split("$(ls \${foo}) ab cd")).containsExactly("$(ls \${foo})", "ab", "cd")
    }

    @Test
    fun throw_when_unbalanced_brackets() {
        assertThatThrownBy { bashSplitter.split("$((ls \${foo)) ab cd") }
                .hasMessageContaining("\$((ls \${foo)) ab cd")
    }
}