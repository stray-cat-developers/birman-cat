package org.straycats.birmancat.api.domain.sender.email

import org.springframework.core.io.ResourceLoader
import org.straycats.birmancat.api.common.ErrorCode
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.api.config.DevelopMistakeException
import org.straycats.birmancat.api.domain.sender.Sender

class EmailSender(
    private val resourceLoader: ResourceLoader,
    private val appEnvironment: AppEnvironment,
) : Sender {
    private lateinit var from: String
    private lateinit var to: String
    private lateinit var message: String
    override fun init(from: String, to: String) {
        this.from = from
        this.to = to
    }

    override fun init(to: String) {
        from = appEnvironment.corporation.notifyEmail
        this.to = to
    }

    override fun setPayload(payloadMap: Map<String, Any?>) {
        val templatePayload = payloadMap["template"] ?: throw DevelopMistakeException(ErrorCode.PD01)
        val template = Sender.Template.valueOf(templatePayload.toString())
        val parser = TemplateParser(resourceLoader, template).apply {
            initDocumentUsingClassPath()
        }

        message = parser.parse(payloadMap)
    }

    override fun send(): Boolean {
        // TODO("Not yet implemented")
        return true
    }
}
