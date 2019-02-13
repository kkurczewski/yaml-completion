package pl.kurczews.completion.extractor

data class Command(val name: String,
                   val subcommands: List<String>,
                   val options: List<String>)