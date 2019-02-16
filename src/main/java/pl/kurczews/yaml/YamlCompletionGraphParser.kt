package pl.kurczews.yaml

import org.yaml.snakeyaml.Yaml
import pl.kurczews.completion.extractor.CommandExtractor
import pl.kurczews.completion.generator.CompletionGenerator
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph
import java.io.Reader
import java.io.Writer

class YamlCompletionGraphParser {

    private val extractor = CommandExtractor()
    private val generator = CompletionGenerator()
    private val bashExpressionParser = BashExpressionParser()

    @Suppress("UNCHECKED_CAST")
    fun parseYaml(reader: Reader, writer: Writer) {
        val entries = Yaml().loadAll(reader).iterator() as Iterator<Map<String, List<String>>>
        for (entry in entries) {
            process(entry, writer)
        }
    }

    private fun process(yamlEntry: Map<String, List<String>>, writer: Writer) {
        for (key in yamlEntry.keys) {
            val graph = GuavaDirectedAcyclicGraph(key)
            for (arc in yamlEntry[key] ?: emptyList()) {
                graph.addArc(bashExpressionParser.parse(arc).toSet())
            }
            val cmds = extractor.extract(graph)
            generator.generate(cmds, writer)
        }
    }
}