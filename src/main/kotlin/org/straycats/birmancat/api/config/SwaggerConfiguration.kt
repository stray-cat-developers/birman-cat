package org.straycats.birmancat.api.config

import org.springdoc.core.models.GroupedOpenApi
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class SwaggerConfiguration {

    init {
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalDateTime::class.java,
            io.swagger.v3.oas.models.media.Schema<LocalDateTime>().apply {
                example(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            },
        )
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalTime::class.java,
            io.swagger.v3.oas.models.media.Schema<LocalTime>().apply {
                example(LocalTime.now().format(DateTimeFormatter.ISO_TIME))
            },
        )
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalDate::class.java,
            io.swagger.v3.oas.models.media.Schema<LocalDate>().apply {
                example(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
            },
        )
    }

    @Bean
    fun default(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("API")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToExclude(
            "/v1/maintenance/**",
            "/v1/migration/**",
            "/v1/bridge/**",
            "/v1/batch/**",
            "/v1/gateway/**",
            "/v1/open-api/**",
            "/v1/internal-development/**",
        )
        .build()

    @Bean
    fun maintenance(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Maintenance")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/maintenance/**")
        .build()

    @Bean
    fun migration(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Migration")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/migration/**")
        .build()

    @Bean
    fun bridge(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Bridge")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/bridge/**")
        .build()

    @Bean
    fun batch(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Batch")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/batch/**")
        .build()

    @Bean
    fun gateway(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Gateway")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/gateway/**")
        .build()

    @Bean
    fun openApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Open-API")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/open-api/**")
        .build()

    @Bean
    fun internalDevelopment(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("Internal-Development")
        .addOpenApiCustomizer {
            it.info.version("v1")
        }
        .packagesToScan("org.straycats.birmancat.api.domain")
        .pathsToMatch("/v1/internal-development/**")
        .build()
}
