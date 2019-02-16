package pl.kurczews.completion.generator

import com.mitchellbosecke.pebble.PebbleEngine
import pl.kurczews.completion.extractor.Command
import java.io.StringWriter
import java.io.Writer
import java.util.*

class CompletionGenerator {

    private val engine = PebbleEngine.Builder().strictVariables(true).build()
    private val completionTemplate = engine.getTemplate("high_order_cmd_completion.txt")

    fun generate(rootCmd: Command, cmds: List<Command>, writer: Writer) {
        val context = HashMap<String, Any>().apply {
            this["root_command"] = rootCmd
            this["commands"] = cmds
        }
        completionTemplate.evaluate(writer, context)
    }
}