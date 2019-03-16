package pl.kurczews.completion.fuzzy

import com.mitchellbosecke.pebble.PebbleEngine
import pl.kurczews.completion.CompletionGenerator
import pl.kurczews.completion.YamlEntry
import pl.kurczews.completion.CommandExtractor
import pl.kurczews.completion.CommandCompletion
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph
import pl.kurczews.yaml.BashSplitter
import java.io.Writer
import java.util.*

class FuzzyCompletionGenerator : CompletionGenerator {

    private val engine = PebbleEngine.Builder().strictVariables(true).build()
    private val firstOrderFuzzyCompletion = engine.getTemplate("fuzzy_completion.pebble")
    private val cmdExtractor = CommandExtractor()

    override fun process(yamlPayload: YamlEntry<List<String>>, writer: Writer) {
        // TODO allow fuzzy on subcommands (first, second word, etc)
        val cmds = parseCmds(yamlPayload)
//        when {
//            cmds.size > 1 -> highOrderCompletion(cmds, writer)
//            else -> firstOrderCompletion(cmds.first(), writer)
//        } // FIXME
    }

    private fun parseCmds(yamlPayload: YamlEntry<List<String>>): List<CommandCompletion> {
        val graph = buildGraph(yamlPayload)
        return cmdExtractor.extract(graph)
    }

    private fun buildGraph(yamlPayload: YamlEntry<List<String>>): GuavaDirectedAcyclicGraph<String> {
        val graph = GuavaDirectedAcyclicGraph(yamlPayload.name)
        yamlPayload
                .entries
                .map { BashSplitter.split(it) }
                .forEach { graph.addArc(it) }
        return graph
    }

    private fun firstOrderCompletion(rootCmd: FuzzyCommand, writer: Writer) {
        val context = HashMap<String, Any?>().apply {
            this["root_command"] = rootCmd
            this["options_cmd"] = rootCmd.subcommandsExpression
            this["fzf_trigger"] = rootCmd.fzfTrigger
            this["fzf_options"] = rootCmd.fzfOptions
        }
        firstOrderFuzzyCompletion.evaluate(writer, context)
    }

    private fun highOrderCompletion(cmds: List<FuzzyCommand>, writer: Writer) {
        // TODO
    }
}