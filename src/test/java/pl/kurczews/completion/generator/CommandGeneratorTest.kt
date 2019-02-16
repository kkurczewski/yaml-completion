package pl.kurczews.completion.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.completion.extractor.CommandExtractor
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph
import java.io.File
import java.io.StringWriter

class CommandGeneratorTest {

    private val generator = CompletionGenerator()
    private val completions = CommandExtractor().extract(GuavaDirectedAcyclicGraph("my-cmd")
            .addArc(listOf("a", "a2", "a3"))
            .addArc(listOf("a", "b2", "b3"))
            .addArc(listOf("a", "c2", "c3"))
            .addArc(listOf("d", "a3"))
            .build())

    @Test
    fun should_generate_completion_for_given_command_and_completions() {
        val writer = StringWriter()
        generator.generate(completions.first, completions.second, writer)
        assertThat(writer.toString()).isEqualTo(contentOf("generator/high_order_cmd_completion.txt"))
    }

    private fun contentOf(file: String): String {
        return File(javaClass.classLoader.getResource(file).toURI()).readText()
    }
}
