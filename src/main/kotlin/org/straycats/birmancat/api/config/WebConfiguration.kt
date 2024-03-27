package org.straycats.birmancat.api.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.straycats.birmancat.api.lock.UserLockInterceptor
import org.straycats.birmancat.api.permission.AuthorizationFilter
import org.straycats.birmancat.api.scope.BlockCertainProfileInterceptor
import org.straycats.birmancat.utils.Jackson
import java.time.format.DateTimeFormatter

@Configuration
@ControllerAdvice
class WebConfiguration(
    private val userLockInterceptor: UserLockInterceptor,
    private val blockCertainProfileInterceptor: BlockCertainProfileInterceptor,
    private val appEnvironment: AppEnvironment,
) : DelegatingWebMvcConfiguration() {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userLockInterceptor)
        registry.addInterceptor(blockCertainProfileInterceptor)
        super.addInterceptors(registry)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val objectMapper = Jackson.getMapper()
        converters.add(ByteArrayHttpMessageConverter())
        converters.add(StringHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
        super.configureMessageConverters(converters)
    }

    @Bean
    fun requestResponseLogFilter(): FilterRegistrationBean<RequestResponseLogFilter> {
        return FilterRegistrationBean<RequestResponseLogFilter>().apply {
            filter = RequestResponseLogFilter()
        }
    }

    @Bean
    fun authorizationFilter(): FilterRegistrationBean<AuthorizationFilter> {
        return FilterRegistrationBean<AuthorizationFilter>().apply {
            filter = AuthorizationFilter(appEnvironment.signIn.sessionCryptoKey)
        }
    }

    @Bean
    override fun mvcConversionService(): FormattingConversionService {
        val conversionService = super.mvcConversionService()
        val dateTimeRegistrar = DateTimeFormatterRegistrar()
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ISO_DATE)
        dateTimeRegistrar.setTimeFormatter(DateTimeFormatter.ISO_TIME)
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeRegistrar.registerFormatters(conversionService)
        return conversionService
    }
}
