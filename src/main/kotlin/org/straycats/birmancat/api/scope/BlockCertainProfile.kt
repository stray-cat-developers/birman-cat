package org.straycats.birmancat.api.scope

/**
 * 특정 프로파일에서 API가 동작하지 못하도록 막는다.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BlockCertainProfile(
    val profiles: Array<String> = ["prod"],
)
