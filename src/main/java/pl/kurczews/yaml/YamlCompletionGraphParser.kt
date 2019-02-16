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

    fun parseYaml(reader: Reader, writer: Writer) {
        val entries = Yaml().loadAll(reader).iterator()
        for (entry in entries) {
            process(entry as HashMap<String, List<String>>, writer)
        }
    }

    private fun process(yamlEntry: HashMap<String, List<String>>, writer: Writer) {
        for (key in yamlEntry.keys) {
            val graph = GuavaDirectedAcyclicGraph(key)
            for (arc in yamlEntry[key] ?: emptyList()) {
                graph.addArc(arc.split(" "))
            }
            val (rootCmd, cmds) = extractor.extract(graph)
            generator.generate(rootCmd, cmds, writer)
        }
    }
}