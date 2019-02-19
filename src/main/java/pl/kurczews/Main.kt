package pl.kurczews

import pl.kurczews.yaml.YamlCompletionGraphParser
import java.io.FileReader
import java.io.FileWriter

fun main(vararg args: String) {
    if (args.isEmpty()) {
        System.err.println("Please provide yaml completion as 1st argument!")
        System.exit(1)
    }
    try {
        val inputFile = args[0]
        val outputFile = args.getOrElse(1) { removeYamlExtension(inputFile) }

        val writer = FileWriter(outputFile)
        YamlCompletionGraphParser().parseYaml(FileReader(inputFile), writer)
    } catch (ex: Exception) {
        System.err.print("Unexpected failure: $ex")
    }
}

private fun removeYamlExtension(file: String): String {
    return file.replace(Regex("\\.ya?ml"), "")
}