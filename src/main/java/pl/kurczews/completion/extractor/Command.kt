package pl.kurczews.completion.extractor

data class Command(val name: String,
                   val subcommands: Set<String>,
                   val options: Set<String> = emptySet())