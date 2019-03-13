package pl.kurczews.completion.fuzzy

import com.mitchellbosecke.pebble.PebbleEngine
import java.io.StringWriter
import java.io.Writer
import java.util.*

fun main() {
    val writer = StringWriter()
    FuzzyCompletionGenerator()
            .generate(listOf(FuzzyCommand("foo", "docker images | awk '{print $1}'")), writer)
    println(writer.toString())
}

class FuzzyCompletionGenerator {

    private val engine = PebbleEngine.Builder().strictVariables(true).build()
    private val firstOrderFuzzyCompletion = engine.getTemplate("fuzzy_completion.pebble")

    fun generate(cmds: List<FuzzyCommand>, writer: Writer) {
        // TODO implement + allow fuzzy on subcommands
        if (cmds.size > 1) {
            highOrderCompletion(cmds, writer)
        } else {
            firstOrderCompletion(cmds.first(), writer)
        }
    }

    private fun firstOrderCompletion(rootCmd: FuzzyCommand, writer: Writer) {
        val context = HashMap<String, Any?>().apply {
            this["root_command"] = rootCmd
            this["options_cmd"] = rootCmd.optionsCmd
            this["fzf_trigger"] = rootCmd.fzfTrigger
            this["fzf_options"] = rootCmd.fzfOptions
        }
        firstOrderFuzzyCompletion.evaluate(writer, context)
    }

    private fun highOrderCompletion(cmds: List<FuzzyCommand>, writer: Writer) {
        // TODO
    }
}