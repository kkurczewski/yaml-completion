package pl.kurczews.completion

import pl.kurczews.completion.bash.BashCompletionGenerator
import pl.kurczews.completion.fuzzy.FuzzyCompletionGenerator
import pl.kurczews.completion.meta.CompletionType.BASH_COMPLETION
import pl.kurczews.completion.meta.CompletionType.FUZZY_COMPLETION
import pl.kurczews.completion.meta.Metadata
import java.io.Writer

interface CompletionGenerator {

    companion object {
        fun from(metadata: Metadata): CompletionGenerator {
            return when (metadata.completionType) {
                BASH_COMPLETION -> BashCompletionGenerator()
                FUZZY_COMPLETION -> FuzzyCompletionGenerator()
            }
        }
    }

    fun process(yamlPayload: YamlEntry<List<String>>, writer: Writer)
}