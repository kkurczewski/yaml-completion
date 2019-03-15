package pl.kurczews.completion.bash.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.completion.bash.BashCommand
import pl.kurczews.completion.CompletionGenerator
import java.io.File
import java.io.StringWriter

class BashCommandGeneratorTest {

    private val generator = CompletionGenerator()

    @Test
    fun should_generate_first_order_completion() {
        val writer = StringWriter()
        val commands = listOf(BashCommand("foo", setOf("foo1", "foo2", "foo3")))

        generator.generate(commands, writer)
        assertThat(writer.toString()).isEqualTo(contentOf("generator/first_order_cmd_completion.txt"))
    }

    @Test
    fun should_generate_first_order_completion_with_options() {
        val writer = StringWriter()
        val commands = listOf(BashCommand("foo", setOf("foo1", "foo2", "foo3"), setOf("-a", "--all")))

        generator.generate(commands, writer)
        assertThat(writer.toString()).isEqualTo(contentOf("generator/first_order_cmd_completion_opts.txt"))
    }

    @Test
    fun should_generate_high_order_completion() {
        val writer = StringWriter()

        val commands = listOf(
                BashCommand("my-cmd", setOf("a", "d")),
                BashCommand("a", setOf("a2", "b2", "c2")),
                BashCommand("a2", setOf("a3")),
                BashCommand("b2", setOf("b3")),
                BashCommand("c2", setOf("c3")),
                BashCommand("d", setOf("a3"))
        )
        generator.generate(commands, writer)

        assertThat(writer.toString()).isEqualTo(contentOf("generator/high_order_cmd_completion.txt"))
    }

    @Test
    fun should_generate_high_order_completion_with_options() {
        val writer = StringWriter()

        val commands = listOf(
                BashCommand("my-cmd", setOf("a", "d"), setOf("-a", "--all")),
                BashCommand("a", setOf("a2", "b2", "c2")),
                BashCommand("a2", setOf("a3")),
                BashCommand("b2", setOf("b3"), setOf("-a", "--all")),
                BashCommand("c2", setOf("c3")),
                BashCommand("c3", emptySet(), setOf("-a", "--all")),
                BashCommand("d", setOf("a3"))
        )
        generator.generate(commands, writer)

        assertThat(writer.toString()).isEqualTo(contentOf("generator/high_order_cmd_completion_opts.txt"))
    }

    private fun contentOf(file: String): String {
        return File(javaClass.classLoader.getResource(file).toURI()).readText()
    }
}
