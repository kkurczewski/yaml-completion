package pl.kurczews.completion.extractor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph

class CommandExtractorTest {

    @Test
    fun should_extract_first_order_completion() {

        val simpleGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .build()

        val (rootCmd, completions) = CommandExtractor().extract(simpleGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", listOf("kat1", "kat2", "kat3"), emptyList()),
                Command("kat1", emptyList(), emptyList()),
                Command("kat2", emptyList(), emptyList()),
                Command("kat3", emptyList(), emptyList())
        )
        assertThat(rootCmd).isEqualTo(Command("kat", listOf("kat1", "kat2", "kat3"), emptyList()))
    }

    @Test
    fun should_extract_first_order_completion_with_options() {

        val simpleOptionsGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .addEdge("kat", "-k1")
                .addEdge("kat", "-k2")
                .addEdge("kat1", "-kk1")
                .build()

        val (rootCmd, completions) = CommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", listOf("kat1", "kat2", "kat3"), listOf("-k1", "-k2")),
                Command("kat1", emptyList(), listOf("-kk1")),
                Command("kat2", emptyList(), emptyList()),
                Command("kat3", emptyList(), emptyList())
        )
        assertThat(rootCmd).isEqualTo(Command("kat", listOf("kat1", "kat2", "kat3"), listOf("-k1", "-k2")))
    }

    @Test
    fun should_extract_higher_order_completion_with_options() {

        val simpleOptionsGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .addEdge("kat", "-k1")
                .addEdge("kat", "-k2")
                .addEdge("kat1", "-kk1")
                .addArc(listOf("kat1", "kat11", "kat111"))
                .addArc(listOf("kat2", "kat22", "kat1"))
                .addEdge("kat22", "-kk2")
                .build()

        val (rootCmd, completions) = CommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", listOf("kat1", "kat2", "kat3"), listOf("-k1", "-k2")),
                Command("kat1", listOf("kat11"), listOf("-kk1")),
                Command("kat2", listOf("kat22"), emptyList()),
                Command("kat3", emptyList(), emptyList()),
                Command("kat11", listOf("kat111"), emptyList()),
                Command("kat111", emptyList(), emptyList()),
                Command("kat22", listOf("kat1"), listOf("-kk2"))
        )
        assertThat(rootCmd).isEqualTo(Command("kat", listOf("kat1", "kat2", "kat3"), listOf("-k1", "-k2")))
    }
}
