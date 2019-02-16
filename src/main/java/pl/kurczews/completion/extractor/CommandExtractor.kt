package pl.kurczews.completion.extractor

import pl.kurczews.graph.dag.DirectedAcyclicGraph

class CommandExtractor {

    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<Command> {
        return directedGraph
                .getNodes()
                .filter { isCommand(it) }
                .map { buildCommand(it, directedGraph) }
                .filterNot { isLeaf(it) }
    }

    private fun buildCommand(command: String, directedGraph: DirectedAcyclicGraph<String>): Command {
        val (subCmds, opts) = directedGraph
                .getOutgoingNodes(command)
                .partition { isCommand(it) }

        return Command(command, subCmds.toSet(), opts.toSet())
    }

    private fun isLeaf(cmd: Command): Boolean = cmd.subcommands.isEmpty() && cmd.options.isEmpty()

    private fun isCommand(cmd: String) = !cmd.startsWith(OPTION_PREFIX)
}
