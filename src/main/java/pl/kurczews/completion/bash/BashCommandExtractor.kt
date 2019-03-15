package pl.kurczews.completion.bash

import pl.kurczews.graph.dag.DirectedAcyclicGraph
import pl.kurczews.yaml.BashSplitter.EXPRESSION_PREFIX

class BashCommandExtractor {

    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<BashCommand> {
        return directedGraph
                .getNodes()
                .filterNot { isExpression(it) }
                .filterNot { isOption(it) }
                .map { buildCommand(it, directedGraph) }
                .filterNot { isLeaf(it) }
    }

    private fun buildCommand(command: String, directedGraph: DirectedAcyclicGraph<String>): BashCommand {
        val (opts, subCmds) = directedGraph
                .getOutgoingNodes(command)
                .partition { isOption(it) }

        return BashCommand(command, subCmds.toSet(), opts.toSet())
    }

    private fun isLeaf(cmd: BashCommand): Boolean = cmd.subcommands.isEmpty() && cmd.options.isEmpty()

    private fun isExpression(cmd: String) = cmd.startsWith(EXPRESSION_PREFIX)

    private fun isOption(cmd: String) = cmd.startsWith(OPTION_PREFIX)
}
