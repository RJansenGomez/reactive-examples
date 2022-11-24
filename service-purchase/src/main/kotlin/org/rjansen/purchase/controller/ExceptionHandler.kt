package org.rjansen.event.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(MonoError::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun monoGeneric(e: Exception): Mono<String> {
        println("Going through the handler")
        return Mono.just(e.message ?: "No message")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun generic(e: Exception): String {
        return e.message ?: "No message"
    }
}

class MonoError(e: Exception) : RuntimeException(e)