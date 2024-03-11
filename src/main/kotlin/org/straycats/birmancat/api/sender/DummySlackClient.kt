package org.straycats.birmancat.api.sender

import org.slf4j.LoggerFactory
import org.straycats.birmancat.utils.toJson

class DummySlackClient : SlackClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun incomingWebhook(path: String, payload: SlackResources.Payload) {
        log.info("send webhook payload is ${payload.toJson()}")
    }

    override fun chatBot(token: String, channel: String, payload: SlackResources.Payload) {
        log.info("send chat bot is ${payload.blocks}")
    }
}
