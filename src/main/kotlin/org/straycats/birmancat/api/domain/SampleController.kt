package org.straycats.birmancat.api.domain

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.birmancat.api.common.Reply
import org.straycats.birmancat.api.common.toReply
import org.straycats.birmancat.api.lock.EnableUserLock
import org.straycats.birmancat.api.permission.RoleHeader

@RestController
@RequestMapping("/v1/sample", "v1/gateway/sample")
class SampleController {

    @Operation(operationId = "Boilerplate.Sample.helloWorld", description = "Hello World")
    @EnableUserLock
    @GetMapping
    fun helloWorld(
        @RequestHeader(RoleHeader.XUser.KEY) userId: Long,
    ): Reply<String> {
        return "Hello World"
            .toReply()
    }
}
