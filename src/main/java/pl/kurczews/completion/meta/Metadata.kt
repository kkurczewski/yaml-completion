package pl.kurczews.completion.meta

import pl.kurczews.completion.meta.CompletionType.DEFAULT

data class Metadata(val completionType: CompletionType = DEFAULT) {
    companion object {
        const val COMPLETION_TYPE_FIELD = "completion-type"
    }
}