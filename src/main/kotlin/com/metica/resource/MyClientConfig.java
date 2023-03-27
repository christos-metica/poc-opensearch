package com.metica.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

@Configuration
public class MyClientConfig {//extends ReactiveElasticsearchConfiguration {

//	@Override
//	public ClientConfiguration clientConfiguration() {
//		return ClientConfiguration.builder()
//			.connectedTo("localhost:9200")
//			.build();
//	}
}