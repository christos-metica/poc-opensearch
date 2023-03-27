package com.metica.resource.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "sample-index")
class IndexedEntity : TaggableEntity {
    @Id
    var id: String? = null
        @Id get() = field
        set(value) {
            field = value
        }

    var name: String? = null

    var price: Double? = null

    var currency: String? = null

    var discount: Double? = null

    var description: String? = null

    @Field(type = FieldType.Object)
    var items: List<ItemEntity>? = mutableListOf()

    override var tags: List<String> = mutableListOf()

    override fun toString(): String {
        return "IndexedEntity(id=$id, name=$name, price=$price, currency=$currency, discount=$discount, description=$description, itemEntity=$items, tags=$tags)"
    }


}