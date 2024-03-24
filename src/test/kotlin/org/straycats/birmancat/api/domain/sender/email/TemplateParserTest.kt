package org.straycats.birmancat.api.domain.sender.email

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.straycats.birmancat.api.config.FlowTestSupport
import org.straycats.birmancat.api.domain.sender.Sender

class TemplateParserTest : FlowTestSupport() {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Test
    fun parse() {
        val template = TemplateParser(resourceLoader, Sender.Template.DEFAULT_PASSWORD).apply {
            initDocumentUsingClassPath()
        }
        val payloadMap = mapOf(
            "name" to "TEST",
            "password" to "123fer34",
        )
        val result = template.parse(payloadMap)

        result.trimIndent() shouldBe """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Account Registration</title>
            </head>
            <body>
            Hello. TEST
            The default password is 123fer34.
            </body>
            </html>
        """.trimIndent()
    }
}
