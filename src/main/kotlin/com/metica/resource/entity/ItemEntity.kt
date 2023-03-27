package com.metica.resource.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "sample-index")
class ItemEntity : TaggableEntity {
    var id: String? = null
        @Id get() = field
        set(value) {
            field = value
        }

    var name: String? = null

    var quantity: Long? = null

    override var tags: List<String> = mutableListOf()

}