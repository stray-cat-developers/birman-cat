package org.straycats.birmancat.api.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.straycats.birmancat.api.config.FlowTestSupport

internal class SampleControllerTest : FlowTestSupport() {

    @Test
    fun helloWorld() {
        // Given
        val sampleControllerFlow = SampleControllerFlow(mockMvc)
        // When
        val reply = sampleControllerFlow.helloWorld()
        // Then
        reply shouldBe "Hello World"
    }
}
