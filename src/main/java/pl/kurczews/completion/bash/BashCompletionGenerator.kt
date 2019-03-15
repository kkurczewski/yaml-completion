package pl.kurczews.completion.bash

import com.mitchellbosecke.pebble.PebbleEngine
import pl.kurczews.completion.CompletionGenerator
import pl.kurczews.completion.YamlEntry
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph
import pl.kurczews.yaml.BashSplitter
import java.io.Writer
import java.util.*

class BashCompletionGenerator : CompletionGenerator {

    private val engine = PebbleEngine.Builder().strictVariables(true).build()
    private val firstOrderCompletion = engine.getTemplate("first_order_completion.pebble")
    private val highOrderCompletion = engine.getTemplate("high_order_completion.pebble")
    private val cmdExtractor = BashCommandExtractor()

    override fun process(yamlPayload: YamlEntry<List<String>>, writer: Writer) {
        val cmds = parseCmds(yamlPayload)
        when {
            cmds.size > 1 -> highOrderCompletion(cmds, writer)
            else -> firstOrderCompletion(cmds.first(), writer)
        }
    }

    private fun parseCmds(yamlPayload: YamlEntry<List<String>>): List<BashCommand> {
        val graph = buildGraph(yamlPayload)
        return cmdExtractor.extract(graph)
    }

    private fun buildGraph(yamlPayload: YamlEntry<List<String>>): GuavaDirectedAcyclicGraph<String> {
        val graph = GuavaDirectedAcyclicGraph(yamlPayload.name)
        yamlPayload
                .properties
                .map { BashSplitter.split(it) }
                .forEach { graph.addArc(it) }
        return graph
    }

    private fun firstOrderCompletion(rootCmd: BashCommand, writer: Writer) {
        val context = HashMap<String, Any>().apply {
            this["root_command"] = rootCmd
        }
        firstOrderCompletion.evaluate(writer, context)
    }

    private fun highOrderCompletion(cmds: List<BashCommand>, writer: Writer) {
        val context = HashMap<String, Any>().apply {
            this["root_command"] = cmds.first()
            this["commands"] = cmds
        }
        highOrderCompletion.evaluate(writer, context)
    }
}