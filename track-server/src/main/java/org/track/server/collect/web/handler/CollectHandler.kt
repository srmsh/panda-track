package org.track.server.collect.web.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.track.server.collect.entity.SpanData
import org.track.server.dao.SpanDataRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
open class CollectHandler {

    @Autowired
    lateinit var spanDataRepository: SpanDataRepository

    fun collect(request: ServerRequest): Flux<SpanData> {
        return spanDataRepository.saveAll(request.bodyToFlux(SpanData::class.java))
    }

    fun findSpanData(request: ServerRequest): Flux<SpanData> {
        return spanDataRepository.findAll();
    }

    fun info(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(BodyInserters.fromObject(request.queryParams()))
    }

}