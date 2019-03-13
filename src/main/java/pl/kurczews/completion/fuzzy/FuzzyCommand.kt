package pl.kurczews.completion.fuzzy

data class FuzzyCommand(val name: String,
                        val optionsCmd: String,
                        val fzfTrigger: String? = null,
                        val fzfOptions: List<String> = emptyList())