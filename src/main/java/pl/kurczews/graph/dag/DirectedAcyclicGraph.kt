package pl.kurczews.graph.dag

abstract class DirectedAcyclicGraph<T>(val head: T) {
    abstract fun getOutgoingNodes(node: T): Set<T>
    abstract fun containsNode(node: T): Boolean
    abstract fun containsEdge(source: T, target: T): Boolean
    abstract fun getNodes(): Set<T>
}