package org.straycats.birmancat.api.sender

interface SlackClient {
    fun incomingWebhook(path: String, payload: SlackResources.Payload)

    fun chatBot(token: String, channel: String, payload: SlackResources.Payload)
}
