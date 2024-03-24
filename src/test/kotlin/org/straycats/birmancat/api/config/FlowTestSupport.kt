package org.straycats.birmancat.api.config

import com.asarkar.spring.test.redis.AutoConfigureEmbeddedRedis
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.straycats.birmancat.BirmanCatApplication

@ActiveProfiles("embedded")
@ExtendWith(SpringExtension::class)
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
@SpringBootTest(classes = [BirmanCatApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [DefaultEmbeddedRedis::class, CustomEmbeddedRedisAutoConfiguration::class])
@AutoConfigureMockMvc
@AutoConfigureEmbeddedRedis(port = 4310)
class FlowTestSupport {

    @Autowired
    lateinit var mockMvc: MockMvc
}
