package pl.kurczews.completion

sealed class CommandCompletion(val name: String)

class StaticCommandCompletion(name: String,
                              val subcommands: List<String>,
                              val options: List<String>) : CommandCompletion(name)

class DynamicCommandCompletion(name: String,
                               val expression: String,
                               val options: List<String>) : CommandCompletion(name)