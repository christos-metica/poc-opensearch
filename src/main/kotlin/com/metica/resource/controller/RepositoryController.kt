package com.metica.resource.controller

import com.metica.resource.entity.IndexedEntity
import com.metica.resource.entity.ItemEntity
import com.metica.resource.repository.IndexEntityRepository
import com.querydsl.core.types.Predicate
import org.opensearch.index.query.QueryBuilders
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/repository/sample-index")
class RepositoryController {

    @Autowired
    val indexEntityRepository: IndexEntityRepository? = null

    @GetMapping("/init")
    fun init(): Mono<String> {
        IndexedEntity().apply {
            name = "Mini Christmas Bundle"
            price = 3.99
            currency = "USD"
            discount = 10.0
            description = "A mini bundle for Christmas that contains a sword and 20 gems"
            items = listOf(
                ItemEntity().apply {
                    name = "Sword"
                    quantity = 1
                    tags = listOf("weapon", "sword")
                }
            )
            tags = listOf("christmas", "bundle")
        }.let {
            indexEntityRepository!!.save(it)
        }

        return Mono.just("Initialized sample-index")
    }


    @GetMapping("/findAll")
    fun findAll(pageable: Pageable): Page<IndexedEntity> {
        return indexEntityRepository!!.findAll(pageable)
    }

    @GetMapping("/findSimilar")
    fun findSimilar(@RequestParam("tag") tag: String): List<IndexedEntity> {
        return indexEntityRepository!!.findSimilar(tag)
    }

    @PostMapping(value = ["/search"])
    fun index(
        @RequestBody indexedEntity: IndexedEntity,
        pageable: Pageable?
    ): Page<IndexedEntity>? {
        val queryStringQuery = QueryBuilders.queryStringQuery("tags:christmas")
        queryStringQuery
        //attributes like minDocFreq: 5 has fixed values
        return indexEntityRepository!!.searchSimilar(indexedEntity, arrayOf("tags"), pageable)
    }

}