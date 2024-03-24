package org.straycats.birmancat.api.domain.sender.email

import com.github.mustachejava.DefaultMustacheFactory
import org.springframework.core.io.ResourceLoader
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.config.DevelopMistakeException
import org.straycats.birmancat.api.domain.sender.Sender
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.Charset

class TemplateParser(
    private val resourceLoader: ResourceLoader,
    private val template: Sender.Template,
) {

    private var document: String = ""

    fun initDocumentUsingClassPath() {
        val resource = resourceLoader.getResource("classpath:template/email/${template.name.lowercase()}.html")
        if (resource.exists().not()) {
            throw DevelopMistakeException(ErrorCode.PD01)
        }

        document = resource.getContentAsString(Charset.defaultCharset())
    }

    fun parse(payloadMap: Map<String, Any?>): String {
        val mf = DefaultMustacheFactory()
        val writer = StringWriter()
        val mustache = mf.compile(StringReader(document), template.name)
        mustache.execute(writer, payloadMap).flush()
        val parsed = writer.toString()
        writer.flush()
        return parsed
    }
}
