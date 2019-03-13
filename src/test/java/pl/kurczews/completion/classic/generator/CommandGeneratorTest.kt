package pl.kurczews.completion.classic.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.completion.classic.Command
import pl.kurczews.completion.classic.CompletionGenerator
import java.io.File
import java.io.StringWriter

class CommandGeneratorTest {

    private val generator = CompletionGenerator()

    @Test
    fun should_generate_first_order_completion() {
        val writer = StringWriter()
        val commands = listOf(Command("foo", setOf("foo1", "foo2", "foo3")))

        generator.generate(commands, writer)
        assertThat(writer.toString()).isEqualTo(contentOf("generator/first_order_cmd_completion.txt"))
    }

    @Test
    fun should_generate_first_order_completion_with_options() {
        val writer = StringWriter()
        val commands = listOf(Command("foo", setOf("foo1", "foo2", "foo3"), setOf("-a", "--all")))

        generator.generate(commands, writer)
        assertThat(writer.toString()).isEqualTo(contentOf("generator/first_order_cmd_completion_opts.txt"))
    }

    @Test
    fun should_generate_high_order_completion() {
        val writer = StringWriter()

        val commands = listOf(
                Command("my-cmd", setOf("a", "d")),
                Command("a", setOf("a2", "b2", "c2")),
                Command("a2", setOf("a3")),
                Command("b2", setOf("b3")),
                Command("c2", setOf("c3")),
                Command("d", setOf("a3"))
        )
        generator.generate(commands, writer)

        assertThat(writer.toString()).isEqualTo(contentOf("generator/high_order_cmd_completion.txt"))
    }

    @Test
    fun should_generate_high_order_completion_with_options() {
        val writer = StringWriter()

        val commands = listOf(
                Command("my-cmd", setOf("a", "d"), setOf("-a", "--all")),
                Command("a", setOf("a2", "b2", "c2")),
                Command("a2", setOf("a3")),
                Command("b2", setOf("b3"), setOf("-a", "--all")),
                Command("c2", setOf("c3")),
                Command("c3", emptySet(), setOf("-a", "--all")),
                Command("d", setOf("a3"))
        )
        generator.generate(commands, writer)

        assertThat(writer.toString()).isEqualTo(contentOf("generator/high_order_cmd_completion_opts.txt"))
    }

    private fun contentOf(file: String): String {
        return File(javaClass.classLoader.getResource(file).toURI()).readText()
    }
}
