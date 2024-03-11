package org.straycats.birmancat.api.domain

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.permission.RoleHeader
import org.straycats.birmancat.utils.fromJson

internal class SampleControllerFlow(
    private val mockMvc: MockMvc,
) {
    fun helloWorld(): String {
        val uri = linkTo<SampleController> { helloWorld(1234) }.toUri()

        return mockMvc.get(uri) {
            contentType = MediaType.APPLICATION_JSON
            header(RoleHeader.XUser.KEY, 1234)
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<String>>()
            .content!!
    }
}
