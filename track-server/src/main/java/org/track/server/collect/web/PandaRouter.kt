package org.track.server.collect.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import org.track.server.collect.web.handler.CollectHandler

@Configuration
private open class PandaRouter {

    @Autowired
    lateinit var collectHandler : CollectHandler

    @Bean
    open fun responseRouterFunction(): RouterFunction<ServerResponse> {
        return RouterFunctions
                .route(RequestPredicates.POST("/panda/collect").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), HandlerFunction {ServerResponse.ok().body(collectHandler.collect(it)) })
                .andRoute(RequestPredicates.GET("/panda/findAll").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), HandlerFunction { ServerResponse.ok().body(collectHandler.findSpanData(it)) })
    }

    @Bean
    open fun infoRouterFunction(): RouterFunction<ServerResponse> {
        return RouterFunctions
                .route(RequestPredicates.GET("/info").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), HandlerFunction {collectHandler.info(it) })
    }
}