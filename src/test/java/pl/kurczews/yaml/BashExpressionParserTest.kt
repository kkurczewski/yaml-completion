package pl.kurczews.yaml

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class BashExpressionParserTest {

    private val expressionParser = BashExpressionParser()

    @Test
    fun parse_simple_line() {
        assertThat(expressionParser.parse("ab cd ef")).containsExactly("ab", "cd", "ef")
    }

    @Test
    fun parse_line_with_expression() {
        assertThat(expressionParser.parse("$(ls ~) ab cd")).containsExactly("$(ls ~)", "ab", "cd")
        assertThat(expressionParser.parse("ab $(ls ~) cd")).containsExactly("ab", "$(ls ~)", "cd")
        assertThat(expressionParser.parse("ab cd $(ls ~)")).containsExactly("ab", "cd", "$(ls ~)")
    }

    @Test
    fun parse_line_with_variable() {
        assertThat(expressionParser.parse("\${foo} ab cd")).containsExactly("\${foo}", "ab", "cd")
        assertThat(expressionParser.parse("ab \${foo} cd")).containsExactly("ab", "\${foo}", "cd")
        assertThat(expressionParser.parse("ab cd \${foo}")).containsExactly("ab", "cd", "\${foo}")
    }

    @Test
    fun parse_line_with_nested_expression() {
        assertThat(expressionParser.parse("$(ls \${foo}) ab cd")).containsExactly("$(ls \${foo})", "ab", "cd")
    }

    @Test
    fun throw_when_unbalanced_brackets() {
        assertThatThrownBy { expressionParser.parse("$((ls \${foo)) ab cd") }
                .hasMessageContaining("\$((ls \${foo)) ab cd")
    }
}