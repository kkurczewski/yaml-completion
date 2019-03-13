package pl.kurczews.yaml

import org.yaml.snakeyaml.Yaml
import pl.kurczews.completion.classic.CommandExtractor
import pl.kurczews.completion.classic.CompletionGenerator
import pl.kurczews.completion.meta.CompletionType.DEFAULT
import pl.kurczews.completion.meta.Metadata
import pl.kurczews.completion.meta.Metadata.Companion.COMPLETION_TYPE_FIELD
import pl.kurczews.graph.dag.guava.GuavaDirectedAcyclicGraph
import java.io.Reader
import java.io.Writer

@Suppress("UNCHECKED_CAST")
class YamlCompletionGraphParser {

    private val extractor = CommandExtractor()
    private val generator = CompletionGenerator()
    private val bashSplitter = BashSplitter()

    companion object {
        private const val META_KEY = "meta"
    }

    fun parseYaml(reader: Reader, writer: Writer) {
        // TODO read completion type
        val entries = Yaml().loadAll(reader).toList() as List<Map<String, Any>>

        // FIXME default values/fallback are messed up
        val metadata = extractMetadata(entries) ?: Metadata(DEFAULT)

        for (entry in entries) {
            when {
                isMetaData(entry.keys.first()) -> {}
                else -> process(mapOfListFrom(entry), writer)
            }
        }
    }

    private fun process(yaml: Map<String, List<String>>, writer: Writer) {
        for (command in yaml.keys) {
            val graph = GuavaDirectedAcyclicGraph(command)
            for (arc in yaml[command] ?: emptyList()) {
                graph.addArc(bashSplitter.split(arc).toSet())
            }
            val cmds = extractor.extract(graph)
            generator.generate(cmds, writer)
        }
    }

    private fun extractMetadata(entries: List<Map<String, Any>>): Metadata? {
        return (entries.first()[META_KEY] as? Map<String, String>)?.let { processMetadata(it) }
    }

    private fun processMetadata(metadata: Map<String, String>): Metadata {
        return Metadata(enumValueOf(metadata.getOrDefault(COMPLETION_TYPE_FIELD, DEFAULT.toString())))
    }

    private fun isMetaData(key: String): Boolean = key == META_KEY

    private fun mapOfListFrom(entry: Map<String, Any>): Map<String, List<String>> = entry as Map<String, List<String>>

    private fun mapFrom(entry: Any?): Map<String, String> = entry as Map<String, String>
}