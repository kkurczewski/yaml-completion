package pl.kurczews.yaml

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.io.InputStreamReader
import java.io.StringWriter
import java.io.Writer

class YamlCompletionGraphParserTest {

    @Test
    fun process_simple_yaml() {
        val writer: Writer = StringWriter()
        YamlCompletionGraphParser().parseYaml(readerOf("yaml_test/simple/simple.yml"), writer)

        assertThat(writer.toString()).isEqualTo(contentOf("yaml_test/simple/simple.txt"))
    }

    @Test
    fun process_yaml_with_expression() {
        val writer: Writer = StringWriter()
        YamlCompletionGraphParser().parseYaml(readerOf("yaml_test/expression/expression.yml"), writer)

        assertThat(writer.toString()).isEqualTo(contentOf("yaml_test/expression/expression.txt"))
    }

    private fun readerOf(file: String): InputStreamReader {
        return File(javaClass.classLoader.getResource(file).toURI()).reader()
    }

    private fun contentOf(file: String): String {
        return File(javaClass.classLoader.getResource(file).toURI()).readText()
    }
}