package org.straycats.birmancat.api.sender

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.slf4j.LoggerFactory
import org.straycats.birmancat.api.config.AppEnvironment
import org.straycats.birmancat.utils.RestClientSupport
import org.straycats.birmancat.utils.toJson

class StableSlackClient(
    private val env: AppEnvironment.Slack,
    private val httpClient: CloseableHttpClient,
) : SlackClient, RestClientSupport(
    SlackResources.getMapper(),
    env.logging,
    LoggerFactory.getLogger(StableSlackClient::class.java),
) {

    override fun incomingWebhook(path: String, payload: SlackResources.Payload) {
        val header = listOf("Content-Type" to "application/json", "charset" to "utf-8")

        val url = "${env.host}$path"
        log.info("send to slack via incoming webhook [path=$path/payload=${payload.toJson()}]")
        httpClient.post(url, header, payload).orElseThrow()
    }

    override fun chatBot(token: String, channel: String, payload: SlackResources.Payload) {
        val url = "${env.host}/api/chat.postMessage"

        val header = listOf(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $token",
        )

        log.info("send to slack via token [url=$url/payload=${payload.toJson()}]")

        httpClient.post(
            url,
            header,
            PostMessage(
                payload.blocks,
                channel,
            ),
        ).orElseThrow()
    }

    private data class PostMessage(
        val blocks: List<org.straycats.birmancat.api.sender.BlockKit.Block>,
        val channel: String,
    )
}
