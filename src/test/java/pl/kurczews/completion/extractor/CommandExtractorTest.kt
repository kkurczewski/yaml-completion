package pl.kurczews.completion.extractor

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph

class CommandExtractorTest {

    @Test
    fun should_preserver_head_order() {
        val unorderedGraph = GuavaDirectedAcyclicGraph("my-cmd")
                .addArc(setOf("a", "a2", "a3"))
                .addArc(setOf("a", "b2", "b3"))
                .addArc(setOf("a", "c2", "c3"))
                .addArc(setOf("d", "a3"))
                .build()

        val completions = CommandExtractor().extract(unorderedGraph)
        assertThat(completions.first()).isEqualTo(Command("my-cmd", setOf("a", "d"), emptySet()))
    }

    @Test
    fun should_extract_first_order_completion() {

        val simpleGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "kat3")
                .build()

        val completions = CommandExtractor().extract(simpleGraph)

        assertThat(completions).containsExactlyInAnyOrder(Command("kat", setOf("kat1", "kat2", "kat3")))
        assertThat(completions.first()).isEqualTo(Command("kat", setOf("kat1", "kat2", "kat3")))
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

        val completions = CommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                Command("kat1", emptySet(), setOf("-kk1"))
        )
        assertThat(completions.first()).isEqualTo(Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
    }

    @Test
    fun should_extract_first_order_completion_with_expression() {

        val simpleGraph = GuavaDirectedAcyclicGraph("kat")
                .addEdge("kat", "kat1")
                .addEdge("kat", "kat2")
                .addEdge("kat", "$(ls /tmp)")
                .build()

        val completions = CommandExtractor().extract(simpleGraph)

        assertThat(completions).containsExactlyInAnyOrder(Command("kat", setOf("kat1", "kat2", "$(ls /tmp)")))
        assertThat(completions.first()).isEqualTo(Command("kat", setOf("kat1", "kat2", "$(ls /tmp)")))
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

        val completions = CommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                Command("kat1", setOf("kat11"), setOf("-kk1")),
                Command("kat2", setOf("kat22")),
                Command("kat11", setOf("kat111")),
                Command("kat22", setOf("kat1"), setOf("-kk2"))
        )
        assertThat(completions.first()).isEqualTo(Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
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

        val completions = CommandExtractor().extract(simpleOptionsGraph)

        assertThat(completions).containsExactlyInAnyOrder(
                Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")),
                Command("kat1", setOf("kat11", "$(ls /tmp)"), setOf("-kk1")),
                Command("kat2", setOf("kat22")),
                Command("kat11", setOf("kat111")),
                Command("kat22", setOf("kat1"), setOf("-kk2"))
        )
        assertThat(completions.first()).isEqualTo(Command("kat", setOf("kat1", "kat2", "kat3"), setOf("-k1", "-k2")))
    }

}
