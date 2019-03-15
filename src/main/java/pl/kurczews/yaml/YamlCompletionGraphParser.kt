package pl.kurczews.yaml

import org.yaml.snakeyaml.Yaml
import pl.kurczews.completion.CompletionGenerator
import pl.kurczews.completion.YamlEntry
import pl.kurczews.completion.meta.Metadata
import java.io.Reader
import java.io.Writer

@Suppress("UNCHECKED_CAST")
class YamlCompletionGraphParser {

    companion object {
        private const val META_KEY = "meta"
    }

    fun parseYaml(reader: Reader, writer: Writer) {
        val (metadata, entries) = extractEntries(reader)
        val yamlEntry = x(entries)

        CompletionGenerator
                .from(metadata)
                .process(yamlEntry, writer)
    }

    private fun x(entries: List<Map<String, Any>>): YamlEntry<List<String>> {
        return YamlEntry("", emptyList())
    }

    private fun extractEntries(reader: Reader): Pair<Metadata, List<Map<String, Any>>> {
        val entries = Yaml().loadAll(reader).toList() as MutableList<Map<String, Any>>
        val metadata = when {
            entries.first().containsKey(META_KEY) -> entries.removeAt(0) as Map<String, String>
            else -> mapOf()
        }
        return Pair(Metadata.from(metadata), entries)
    }
}