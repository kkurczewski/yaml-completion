package pl.kurczews.completion.extractor

import pl.kurczews.graph.dag.DirectedAcyclicGraph

class CommandExtractor {

    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<Command> {
        return directedGraph
                .getNodes()
                .filter { !it.startsWith(OPTION_PREFIX) }
                .map { buildCommand(it, directedGraph) }
                .filterNot { it.isLeaf() }
    }

    private fun buildCommand(command: String, directedGraph: DirectedAcyclicGraph<String>): Command {
        val (subCmds, opts) = directedGraph
                .getOutgoingNodes(command)
                .partition { !it.startsWith(OPTION_PREFIX) }

        return Command(command, subCmds.toSet(), opts.toSet())
    }

    private fun Command.isLeaf(): Boolean = this.subcommands.isEmpty() && this.options.isEmpty()
}
