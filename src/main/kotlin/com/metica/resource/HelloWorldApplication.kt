package com.metica.resource

import com.metica.resource.repository.IndexEntityRepository
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.opensearch.client.RestClient
import org.opensearch.client.RestClientBuilder
import org.opensearch.client.json.jackson.JacksonJsonpMapper
import org.opensearch.client.opensearch.OpenSearchAsyncClient
import org.opensearch.client.transport.rest_client.RestClientTransport
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import java.time.Duration


@SpringBootApplication(exclude = [ ElasticsearchDataAutoConfiguration::class ] )
@Configuration
//@EnableSwagger2
@EnableElasticsearchRepositories(basePackageClasses = [IndexEntityRepository::class])
class HelloWorldApplication {
    companion object AwsPocApplication {
        private val log: Logger = LoggerFactory.getLogger(HelloWorldApplication::class.java)
    }

    @Bean
    fun openSearchAsyncClient(@Value("\${opensearch.password}") password: String,
                              @Value("\${opensearch.username}") username: String,
                              @Value("\${opensearch.uri.scheme}") scheme: String,
                              @Value("\${opensearch.uri.host}") host: String,
                              @Value("\${opensearch.uri.port}") port: Int): OpenSearchAsyncClient? {
        val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            UsernamePasswordCredentials(username, password)
        )

        val restClient = RestClient.builder(HttpHost(host, port, scheme))
            .setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(
                    credentialsProvider
                )
            }.build()
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())
        return OpenSearchAsyncClient(/* transport = */ transport)

    }

    /**
     * Used by IndexEntityRepository
     */
//    @Bean
//    fun opensearchClientConfiguration(@Value("\${opensearch.password}") password: String,
//                                      @Value("\${opensearch.username}") username: String,
//                                      @Value("\${opensearch.uri.scheme}") scheme: String,
//                                      @Value("\${opensearch.uri.host}") host: String,
//                                      @Value("\${opensearch.uri.port}") port: Int): RestClientBuilder {
//        return RestClient.builder(HttpHost(host, 9200, scheme))
//            .setHttpClientConfigCallback { httpClientBuilder: HttpAsyncClientBuilder ->
//                httpClientBuilder.setDefaultCredentialsProvider(
//                    BasicCredentialsProvider().apply {
//                        setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username, password))
//                    }
//                )
//            }
//    }

    @Bean
    fun reactivePageableHandlerMethodArgumentResolver(): HandlerMethodArgumentResolver? {
        return ReactivePageableHandlerMethodArgumentResolver()
    }

}

fun main(args: Array<String>) {
    runApplication<HelloWorldApplication>(*args)
}
