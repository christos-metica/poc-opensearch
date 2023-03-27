package com.metica.resource.entity

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import org.opensearch.client.opensearch._types.mapping.Property.Kind
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

class DynamicBean {
    var id: String? = null

    val attributes : MutableMap<String, Any>
        @JsonAnyGetter get

    val mapping: MutableMap<String, Kind>
        @JsonIgnore get

    constructor() {
        attributes = HashMap()
        mapping = HashMap()
    }

    constructor(attributes: Map<String, Any>) {
        this.attributes = mutableMapOf<String, Any>()
        this.attributes.putAll(attributes)
        this.mapping = HashMap()
    }

    @JsonAnySetter
    fun setAttribute(name: String, value: Any) {
        attributes[name] = value
    }

    override fun toString(): String {
        return "DynamicBean(attributes=$attributes, mapping=$mapping)"
    }

}