package pl.kurczews

import pl.kurczews.yaml.YamlCompletionGraphParser
import java.io.FileReader
import java.io.FileWriter

fun main(vararg args: String) {
    val writer = FileWriter(args[1])
    YamlCompletionGraphParser().parseYaml(FileReader(args[0]), writer)
}