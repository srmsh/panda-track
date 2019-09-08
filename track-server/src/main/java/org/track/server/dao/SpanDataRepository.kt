package org.track.server.dao

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import org.track.server.collect.entity.SpanData

@Repository
interface SpanDataRepository : ReactiveMongoRepository<SpanData, String> {
}