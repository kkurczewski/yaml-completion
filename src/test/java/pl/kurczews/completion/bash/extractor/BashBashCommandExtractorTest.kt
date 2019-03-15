package pl.kurczews.completion.bash.extractor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.completion.bash.BashCommand
import pl.kurczews.completion.bash.BashCommandExtractor
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph

class BashBashCommandExtractorTest {

    @Test
    fun should_preserver_head_order() {
        val unorderedGraph = GuavaDirectedAcyclicGraph("my-cmd")
                .addArc(setOf("a", "a2", "a3"))
                .addArc(setOf("a", "b2", "b3"))
                .addArc(setOf("a", "c2", "c3"))
                .addArc(setOf("d", "a3"))
                .build()

        val completions = BashCommandExtractor().extract(unorderedGraph)
        assertThat(completions.first()).isEqualTo(BashCommand("my-cmd", setOf("a", "d"), emptySet()))
    }

    @Test
    fun should_extract_first_order_completion() {

        val simpleGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .build()

        val completions = BashCommandExtractor().extract(simpleGraph)

        assertThat(completions).containsExactlyInAnyOrder(BashCommand("kat", setOf("kat1", "kat2", "kat3")))
        assertThat(completions.first()).isEqualTo(BashCommand("kat", setOf("kat1", "kat2", "kat3")))
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

        val completions = BashCommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                BashCommand("kat1", emptySet(), setOf("-kk1"))
        )
        assertThat(completions.first()).isEqualTo(BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
    }

    @Test
    fun should_extract_first_order_completion_with_expression() {

        val simpleGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "$(ls /tmp)")
                .build()

        val completions = BashCommandExtractor().extract(simpleGraph)

        assertThat(completions).containsExactlyInAnyOrder(BashCommand("kat", setOf("kat1", "kat2", "$(ls /tmp)")))
        assertThat(completions.first()).isEqualTo(BashCommand("kat", setOf("kat1", "kat2", "$(ls /tmp)")))
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
                .addArc(setOf("kat1", "kat11", "kat111"))
                .addArc(setOf("kat2", "kat22", "kat1"))
                .addEdge("kat22", "-kk2")
                .build()

        val completions = BashCommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                BashCommand("kat1", setOf("kat11"), setOf("-kk1")),
                BashCommand("kat2", setOf("kat22")),
                BashCommand("kat11", setOf("kat111")),
                BashCommand("kat22", setOf("kat1"), setOf("-kk2"))
        )
        assertThat(completions.first()).isEqualTo(BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
    }

    @Test
    fun should_extract_higher_order_completion_with_options_and_expression() {

        val simpleOptionsGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .addEdge("kat", "-k1")
                .addEdge("kat", "-k2")
                .addEdge("kat1", "-kk1")
                .addEdge("kat1", "$(ls /tmp)")
                .addArc(setOf("kat1", "kat11", "kat111"))
                .addArc(setOf("kat2", "kat22", "kat1"))
                .addEdge("kat22", "-kk2")
                .build()

        val completions = BashCommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                BashCommand("kat1", setOf("kat11", "$(ls /tmp)"), setOf("-kk1")),
                BashCommand("kat2", setOf("kat22")),
                BashCommand("kat11", setOf("kat111")),
                BashCommand("kat22", setOf("kat1"), setOf("-kk2"))
        )
        assertThat(completions.first()).isEqualTo(BashCommand("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
    }

}
