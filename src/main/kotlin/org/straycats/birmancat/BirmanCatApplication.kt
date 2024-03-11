package org.straycats.birmancat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator
import org.straycats.birmancat.api.config.AppEnvironment

@SpringBootApplication
@EnableConfigurationProperties(AppEnvironment::class)
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
class BirmanCatApplication

fun main(args: Array<String>) {
    runApplication<BirmanCatApplication>(*args)
}
