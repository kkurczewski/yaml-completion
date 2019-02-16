package pl.kurczews.graph.dag.guava

import org.junit.Test
import pl.kurczews.graph.dag.DirectedAcyclicGraph
import kotlin.test.assertTrue

class GuavaDirectedAcyclicGraphTest {

    @Test
    fun should_build_graph_from_completion_lines() {
        val graph = GuavaDirectedAcyclicGraph("cmd")
                .addArc(listOf("a", "a2", "a3"))
                .addArc(listOf("a", "b2", "b3"))
                .addArc(listOf("a", "c2", "c3"))
                .addArc(listOf("d", "a3"))
                .build()

        graph.assertContainsEdge("cmd", "a")
        graph.assertContainsEdge("cmd", "d")
        graph.assertContainsEdge("a", "a2")
        graph.assertContainsEdge("a2", "a3")
        graph.assertContainsEdge("a", "b2")
        graph.assertContainsEdge("b2", "b3")
        graph.assertContainsEdge("a", "c2")
        graph.assertContainsEdge("c2", "c3")
        graph.assertContainsEdge("d", "a3")
    }

    private fun DirectedAcyclicGraph<String>.assertContainsEdge(first: String, second: String) {
        assertTrue("Expected graph to contain edge $first -> $second") { this.containsEdge(first, second) }
    }
}