package org.straycats.birmancat.api.sender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.straycats.birmancat.utils.CustomDateSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SlackResources {

    data class Payload(
        val blocks: List<org.straycats.birmancat.api.sender.BlockKit.Block>,
    )

    companion object {
        private val mapper = Jackson2ObjectMapperBuilder.json()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .propertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy())
            .modules(JavaTimeModule())
            .serializerByType(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
            .serializers(CustomDateSerializer())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build<ObjectMapper>()
            .registerKotlinModule()

        /**
         * Slack's block kit should use a predefined mapper.
         */
        fun getMapper(): ObjectMapper = mapper
    }
}
