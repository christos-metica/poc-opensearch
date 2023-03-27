package com.metica.resource.controller

import com.metica.resource.entity.DynamicBean
import com.metica.resource.repository.IndexEntityRepository
import org.opensearch.client.json.JsonData
import org.opensearch.client.opensearch.OpenSearchAsyncClient
import org.opensearch.client.opensearch._types.mapping.*
import org.opensearch.client.opensearch._types.query_dsl.Like
import org.opensearch.client.opensearch._types.query_dsl.LikeDocument
import org.opensearch.client.opensearch._types.query_dsl.MoreLikeThisQuery
import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch.core.CreateRequest
import org.opensearch.client.opensearch.core.CreateResponse
import org.opensearch.client.opensearch.indices.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/indexed/{index}")
class EntityController() {

    @Autowired
    val client: OpenSearchAsyncClient? = null

    @Autowired
    val indexEntityRepository: IndexEntityRepository? = null

    @GetMapping("/init")
    fun init(@PathVariable index: String): Mono<String> {

        val createIndexRequest: CreateIndexRequest = CreateIndexRequest.Builder().index(index).build()
        return Mono.fromFuture(client!!.indices().create(createIndexRequest).thenApply { response ->
            val indexSettings: IndexSettings = IndexSettings.Builder().autoExpandReplicas("0-all").build()
            val settingsBody: IndexSettings = IndexSettings.Builder().settings(indexSettings).build()
            val putSettingsRequest: PutIndicesSettingsRequest = PutIndicesSettingsRequest.Builder()
                .index(index).settings(settingsBody).build()

            client!!.indices().putSettings(putSettingsRequest)

            val putMappingRequest: PutMappingRequest = PutMappingRequest.Builder()
                .index(index)
                .properties(
                    mapOf(
                        "id" to Property(TextProperty.Builder().build()),
                        "name" to Property(
                            TextProperty.Builder().analyzer("english").termVector(TermVectorOption.Yes).build()
                        ),
                        "price" to Property(DateProperty.Builder().build()),
                        "currency" to Property(KeywordProperty.Builder().build()),
                        "discount" to Property(DateProperty.Builder().build()),
                        "description" to Property(TextProperty.Builder().termVector(TermVectorOption.Yes).build()),
                        "items" to Property(
                            ObjectProperty.Builder().properties(
                                mapOf(
//                                    "id" to Property(TextProperty.Builder().build()),
                                    "item_name" to Property(TextProperty.Builder().build()),
                                    "quantity" to Property(LongNumberProperty.Builder().build()),
                                    "tags" to Property(
                                        TextProperty.Builder().analyzer("keyword").termVector(
                                            TermVectorOption.Yes
                                        ).store(true).build()
                                    ),
                                )
                            ).build()
                        ),
                        "tags" to Property(
                            TextProperty.Builder().analyzer("keyword").termVector(TermVectorOption.Yes).build()
                        ),
                    )
                ).build()
            return@thenApply client!!.indices().putMapping(putMappingRequest)
        }).then(Mono.just("done"))
    }

    fun createObject(index: String, document: DynamicBean, l: Long): CompletableFuture<CreateResponse> {

        val createRequest: CreateRequest<DynamicBean> = CreateRequest.Builder<DynamicBean>().index(index)
            .id(UUID.randomUUID().toString())
            .document(document)
            .build()
        return client!!.create(createRequest).thenApply { response ->
            sleep(l)
            return@thenApply response
        }.toCompletableFuture()
    }

    @GetMapping("/add")
    fun add(@PathVariable index: String): Flux<Map<String, String>> {

        return Flux.create( { emitter ->
            listOf<CompletableFuture<CreateResponse>>(
                createObject(
                    index, DynamicBean(
                        mapOf<String, Any>(
                            "id" to UUID.randomUUID().toString(),
                            "name" to "Mini Christmas Bundle",
                            "price" to 3.99,
                            "currency" to "USD",
                            "discount" to 10.0,
                            "description" to "A mini bundle for Christmas that contains a sword and 20 gems",
                            "items" to listOf(
                                mapOf(
//                        "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Sword",
                                    "quantity" to 1,
                                    "tags" to listOf("sword", "weapon")
                                ), mapOf(
//                        "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Gems",
                                    "quantity" to 20,
                                    "tags" to listOf("game currency")
                                )
                            ),
                            "tags" to listOf("christmas")
                        )
                    ), 2000
                ).thenApply {
//                    emitter.next("done1")
                    println("done1")
                    emitter.next(mapOf<String, String>("result" to "done1"))
                    return@thenApply it
                },

                createObject(
                    index, DynamicBean(
                        mapOf<String, Any>(
                            "id" to UUID.randomUUID().toString(),
                            "name" to "Medium Christmas Bundle",
                            "price" to 5.99,
                            "currency" to "USD",
                            "discount" to 10.0,
                            "description" to "A medium bundle for Christmas that contains a armor and 50 gems",
                            "items" to listOf(
                                mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Armor",
                                    "quantity" to 1,
                                    "tags" to listOf("armor")
                                ), mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Gems",
                                    "quantity" to 50,
                                    "tags" to listOf("game currency")
                                )
                            ),
                            "tags" to listOf("christmas")
                        )
                    ), 3000
                ).thenApply { it ->
//                    emitter.next("done2")
                    println("done2")
                    emitter.next(mapOf<String, String>("result" to "done2"))
                    return@thenApply it
                },

                createObject(
                    index, DynamicBean(
                        mapOf<String, Any>(
                            "id" to UUID.randomUUID().toString(),
                            "name" to "Medium Bundle",
                            "price" to 5.99,
                            "currency" to "USD",
                            "discount" to 10.0,
                            "description" to "A medium bundle that contains a armor and a sword",
                            "items" to listOf(
                                mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Armor",
                                    "quantity" to 1,
                                    "tags" to listOf("armor")
                                ), mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Sword",
                                    "quantity" to 1,
                                    "tags" to listOf("sword", "weapon")
                                )
                            )
                        )
                    ), 1000
                ).thenApply { it ->
//                    emitter.next("done3")
                    println("done3")
                    emitter.next(mapOf<String, String>("result" to "done3"))
                    return@thenApply it
                },

                createObject(
                    index, DynamicBean(
                        mapOf<String, Any>(
                            "id" to UUID.randomUUID().toString(),
                            "name" to "Medium Bundle",
                            "price" to 5.99,
                            "currency" to "USD",
                            "discount" to 10.0,
                            "description" to "A medium bundle that contains a spear and a sword",
                            "items" to listOf(
                                mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "spear",
                                    "quantity" to 1,
                                    "tags" to listOf("spear", "weapon")
                                ), mapOf(
//                                "id" to UUID.randomUUID().toString(),
                                    "item_name" to "Sword",
                                    "quantity" to 1,
                                    "tags" to listOf("sword", "weapon")
                                )
                            )
                        )
                    ), 1000
                ).thenApply { it ->
//                    emitter.next("done4")
                    println("done4")
                    emitter.next(mapOf<String, String>("result" to "done4"))
                    return@thenApply it
                },
            ).forEach { it.join() }

            emitter.complete()

        }, FluxSink.OverflowStrategy.BUFFER)

    }


    @GetMapping("/search")
    fun search(@PathVariable index: String): Mono<List<DynamicBean?>> {
        val moreLikeThisQuery = MoreLikeThisQuery.Builder().fields("description")
            .like(
                Like.Builder().document(
                    LikeDocument.Builder().doc(JsonData.of(mapOf("description" to "medium bundle"))).build()
                ).build()
            ).minDocFreq(1).minTermFreq(1).maxQueryTerms(12)
            .build()

        return Mono.fromFuture(client!!.search(
            { s: org.opensearch.client.opensearch.core.SearchRequest.Builder ->
                s.index(index).query(Query.Builder().moreLikeThis(moreLikeThisQuery).build())
            },
            DynamicBean::class.java
        ).thenApply { response -> response.hits().hits().map { it.source() } }
        )
    }

    @DeleteMapping()
    fun delete(@PathVariable index: String): Mono<String> {
        return Mono.fromFuture(
            client!!.indices().delete(DeleteIndexRequest.Builder().index(index).build()).thenApply { response ->
                return@thenApply "done"
            })

    }

}