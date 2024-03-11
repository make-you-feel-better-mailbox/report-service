package com.onetwo.reportservice.exception

import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        val logger: Logger? = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }


    @ExceptionHandler(BadRequestException::class)
    fun badRequestException(badRequestException: BadRequestException): ResponseEntity<String> {
        logger?.info("BadRequestException ", badRequestException)
        return ResponseEntity.badRequest().body(badRequestException.message)
    }

    @ExceptionHandler(NotFoundResourceException::class)
    fun notFoundResourceException(notFoundResourceException: NotFoundResourceException): ResponseEntity<String> {
        logger?.info("NotFoundResourceException ", notFoundResourceException)
        return ResponseEntity.badRequest().body(notFoundResourceException.message)
    }
}