package org.straycats.birmancat.api.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Common.Privacy")
data class Privacy(
    val location: Location? = null,
)
