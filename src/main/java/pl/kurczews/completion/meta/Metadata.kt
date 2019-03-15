package pl.kurczews.completion.meta

import pl.kurczews.completion.meta.CompletionType.BASH_COMPLETION

data class Metadata(val completionType: CompletionType) {
    companion object {
        fun from(metadataMap: Map<String, String>): Metadata {
            return Metadata(enumValueOf(metadataMap.getOrDefault("completion-type", BASH_COMPLETION.toString())))
        }
    }
}