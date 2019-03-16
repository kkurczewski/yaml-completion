package pl.kurczews.completion

import pl.kurczews.graph.dag.DirectedAcyclicGraph
import pl.kurczews.yaml.Symbols.EXPRESSION_PREFIX
import pl.kurczews.yaml.Symbols.EXPRESSION_TAG_PREFIX
import pl.kurczews.yaml.Symbols.OPTION_PREFIX

class CommandExtractor {

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<CommandCompletion> {
        return directedGraph
                .getNodes()
                .filterNot { it.isExpression() }
                .filterNot { isOption(it) }
                .map { getCompletions(it, directedGraph) }
                .filterNot { it.isLeaf() }
    }

    private fun getCompletions(command: String, directedGraph: DirectedAcyclicGraph<String>): CommandCompletion {
        val (opts, subCmds) = getDescendants(directedGraph, command)

        return when (subCmds.first().isExpression()) {
            true -> DynamicCommandCompletion(command, subCmds.first(), opts)
            false -> StaticCommandCompletion(command, subCmds, opts)
        }
    }

    private fun getDescendants(graph: DirectedAcyclicGraph<String>, cmd: String): Pair<List<String>, List<String>> {
        return graph.getOutgoingNodes(cmd).partition { isOption(it) }
    }

    private fun CommandCompletion.isLeaf(): Boolean {
        return when (this) {
            is StaticCommandCompletion -> this.subcommands.isEmpty().and(this.options.isEmpty())
            is DynamicCommandCompletion -> this.expression.isEmpty().and(this.options.isEmpty())
        }
    }

    private fun String.isExpression(): Boolean {
        return this.startsWith(EXPRESSION_PREFIX).and(this.startsWith(EXPRESSION_TAG_PREFIX))
    }

    private fun isOption(cmd: String) = cmd.startsWith(OPTION_PREFIX)
}