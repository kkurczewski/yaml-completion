package pl.kurczews.completion.extractor

import pl.kurczews.graph.dag.DirectedAcyclicGraph
import pl.kurczews.yaml.BashExpressionParser.Companion.EXPRESSION_PREFIX

class CommandExtractor {

    companion object {
        private const val OPTION_PREFIX = "-"
    }

    fun extract(directedGraph: DirectedAcyclicGraph<String>): List<Command> {
        return directedGraph
                .getNodes()
                .filterNot { isExpression(it) }
                .filterNot { isOption(it) }
                .map { buildCommand(it, directedGraph) }
                .filterNot { isLeaf(it) }
    }

    private fun buildCommand(command: String, directedGraph: DirectedAcyclicGraph<String>): Command {
        val (opts, subCmds) = directedGraph
                .getOutgoingNodes(command)
                .partition { isOption(it) }

        return Command(command, subCmds.toSet(), opts.toSet())
    }

    private fun isLeaf(cmd: Command): Boolean = cmd.subcommands.isEmpty() && cmd.options.isEmpty()

    private fun isExpression(cmd: String) = cmd.startsWith(EXPRESSION_PREFIX)

    private fun isOption(cmd: String) = cmd.startsWith(OPTION_PREFIX)
}
