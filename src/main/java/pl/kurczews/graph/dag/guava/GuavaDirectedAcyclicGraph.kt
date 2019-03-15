package pl.kurczews.graph.dag.guava

import com.google.common.graph.ElementOrder
import com.google.common.graph.GraphBuilder
import com.google.common.graph.MutableGraph
import pl.kurczews.common.forEach2
import pl.kurczews.graph.dag.MutableDirectedAcyclicGraph

class GuavaDirectedAcyclicGraph<T>(head: T) : MutableDirectedAcyclicGraph<T>(head) {

    private val graph: MutableGraph<T> = GraphBuilder
            .directed()
            .nodeOrder<T>(ElementOrder.insertion())
            .allowsSelfLoops(false)
            .build()

    init {
        graph.addNode(head)
    }

    override fun addNode(node: T): MutableDirectedAcyclicGraph<T> {
        graph.addNode(node)
        return this
    }

    override fun addEdge(first: T, second: T): MutableDirectedAcyclicGraph<T> {
        graph.putEdge(first, second)
        return this
    }

    override fun addArc(nodes: List<T>): MutableDirectedAcyclicGraph<T> {
        nodes.forEach2 { first, second ->
            graph.addNode(first)
            graph.addNode(second)
            graph.putEdge(first, second)
        }
        graph.putEdge(head, nodes.first())
        return this
    }

    override fun getNodes(): Set<T> {
        return graph.nodes()
    }

    override fun getOutgoingNodes(node: T): Set<T> {
        return if (containsNode(node)) {
            graph.successors(node)
        } else emptySet()
    }

    override fun containsNode(node: T): Boolean {
        return getNodes().contains(node)
    }

    override fun containsEdge(source: T, target: T): Boolean {
        return graph.hasEdgeConnecting(source, target)
    }
}
