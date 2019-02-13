package pl.kurczews.graph.dag

abstract class MutableDirectedAcyclicGraph<T>(head: T) : DirectedAcyclicGraph<T>(head) {
    abstract fun addNode(node: T): MutableDirectedAcyclicGraph<T>
    abstract fun addEdge(first: T, second: T): MutableDirectedAcyclicGraph<T>
    abstract fun addArc(nodes: List<T>): MutableDirectedAcyclicGraph<T>
    fun build(): DirectedAcyclicGraph<T> = this
}