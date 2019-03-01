package pl.kurczews

import org.yaml.snakeyaml.error.YAMLException
import pl.kurczews.yaml.YamlCompletionGraphParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private val DEFAULT_TARGET_DIR = Paths.get("out")

fun main(vararg args: String) {
    if (args.isEmpty()) {
        exit("Please provide yaml completion as 1st argument!")
    }
    try {
        val (inputFile, outputDirectory) = readArgs(args)
        when {
            inputFile.isDirectory -> {
                Files.newDirectoryStream(inputFile.toPath())
                        .map(Path::toFile)
                        .forEach { generateCompletion(it, outputDirectory) }
            }
            else -> generateCompletion(inputFile, outputDirectory)
        }
    } catch (ex: IOException) {
        exit("Can't access file: $ex")
    }
}

private fun generateCompletion(inputFile: File, outputDirectory: Path) {
    try {
        val outputFile = outputDirectory.resolve(inputFile.nameWithoutExtension).toFile()
        YamlCompletionGraphParser().parseYaml(FileReader(inputFile), FileWriter(outputFile))
    } catch (ex: YAMLException) {
        exit("Yaml parsing error: $ex")
    }
}

private fun readArgs(args: Array<out String>): Pair<File, Path> {
    val inputFile = File(args[0])
    val outputDirectory = args.getOrNull(1)?.let { Paths.get(it) } ?: DEFAULT_TARGET_DIR
    if (!outputDirectory.toFile().exists()) {
        Files.createDirectory(outputDirectory)
    }
    return Pair(inputFile, outputDirectory)
}

private fun exit(errorMsg: String) {
    System.err.println(errorMsg)
    System.exit(1)
}