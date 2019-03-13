package pl.kurczews.completion.classic

data class Command(val name: String,
                   val subcommands: Set<String>,
                   val options: Set<String> = emptySet())