package pl.kurczews.completion.fuzzy

data class FuzzyCommand(val name: String,
                        val subcommandsExpression: String,
                        val options: Set<String> = emptySet(),
                        val fzfTrigger: String = "",
                        val fzfOptions: List<String> = emptyList())