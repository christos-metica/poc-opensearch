package com.metica.resource.repository

import com.metica.resource.entity.IndexedEntity
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.repository.query.Param

/**
 * QuerydslPredicateExecutor<IndexedEntity> doesn't seem to be be supported currently
 * ElasticsearchRepositoryFactory.java:81
 *
 */
interface IndexEntityRepository : ElasticsearchRepository<IndexedEntity, String> {

    @Query("""{
        "more_like_this": {
          "fields": [
            "items.tags"
          ],
          "like": [
            {
              "doc": { "items": [{"quantity": "1", "tags": "?0"}] }
            }
          ],
          "min_term_freq": 1, "min_doc_freq": 1,
          "max_query_terms": 12
        }
      }""")
    fun findSimilar(@Param("tagName") tag: String): List<IndexedEntity>

}