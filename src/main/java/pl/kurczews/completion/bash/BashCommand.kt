package pl.kurczews.completion.bash

data class BashCommand(val name: String,
                       val subcommands: Set<String>,
                       val options: Set<String> = emptySet())