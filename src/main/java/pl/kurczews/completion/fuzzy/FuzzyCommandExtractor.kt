package pl.kurczews.completion.fuzzy

import pl.kurczews.graph.dag.DirectedAcyclicGraph
import pl.kurczews.yaml.BashSplitter.EXPRESSION_PREFIX

class FuzzyCommandExtractor {
    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<FuzzyCommand> {
        return directedGraph
                .getNodes()
                .filterNot { isExpression(it) }
                .filterNot { isOption(it) }
                .map { buildCommand(it, directedGraph) }
                .filterNot { isLeaf(it) }
    }

    private fun buildCommand(command: String, directedGraph: DirectedAcyclicGraph<String>): FuzzyCommand {
        val (opts, subCmds) = directedGraph
                .getOutgoingNodes(command)
                .partition { isOption(it) }

        return FuzzyCommand(command, subCmds.first(), opts.toSet())
    }

    private fun isLeaf(cmd: FuzzyCommand): Boolean = cmd.subcommandsExpression.isEmpty() && cmd.options.isEmpty()

    private fun isExpression(cmd: String) = cmd.startsWith(EXPRESSION_PREFIX)

    private fun isOption(cmd: String) = cmd.startsWith(OPTION_PREFIX)
}
