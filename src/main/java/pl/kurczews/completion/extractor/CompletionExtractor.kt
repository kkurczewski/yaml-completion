package pl.kurczews.completion.extractor

import pl.kurczews.graph.dag.DirectedAcyclicGraph

// TODO two-space split to avoid space errors (arc)??
// TODO strict-mode (as separate class), check whole arc to perform completion

class CompletionExtractor {

    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): Pair<Command, List<Command>> {
        val cmds = directedGraph
                .getNodes()
                .filter { !it.startsWith(OPTION_PREFIX) }
                .map { extractCompletion(it, directedGraph) }

        val rootCmd = cmds.first { it.name == directedGraph.head }

        return Pair(rootCmd, cmds)
    }

    private fun extractCompletion(command: String, directedGraph: DirectedAcyclicGraph<String>): Command {
        val (subCmds, opts) = directedGraph
                .getOutgoingNodes(command)
                .partition { !it.startsWith(OPTION_PREFIX) }

        return Command(command, subCmds, opts)
    }
}
