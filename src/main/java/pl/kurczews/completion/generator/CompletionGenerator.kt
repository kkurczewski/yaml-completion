package pl.kurczews.completion.generator

import com.mitchellbosecke.pebble.PebbleEngine
import pl.kurczews.completion.extractor.Command
import java.io.Writer
import java.util.*

class CompletionGenerator {

    private val engine = PebbleEngine.Builder().strictVariables(true).build()
    private val firstOrderCompletion = engine.getTemplate("first_order_completion.txt")
    private val highOrderCompletion = engine.getTemplate("high_order_completion.txt")

    fun generate(cmds: List<Command>, writer: Writer) {
        if (cmds.size > 1) {
            highOrderCompletion(cmds, writer)
        } else {
            firstOrderCompletion(cmds.first(), writer)
        }
    }

    private fun firstOrderCompletion(rootCmd: Command, writer: Writer) {
        val context = HashMap<String, Any>().apply {
            this["root_command"] = rootCmd
        }
        firstOrderCompletion.evaluate(writer, context)
    }

    private fun highOrderCompletion(cmds: List<Command>, writer: Writer) {
        val context = HashMap<String, Any>().apply {
            this["root_command"] = cmds.first()
            this["commands"] = cmds
        }
        highOrderCompletion.evaluate(writer, context)
    }
}