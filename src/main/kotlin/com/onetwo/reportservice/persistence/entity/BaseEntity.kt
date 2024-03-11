package com.onetwo.reportservice.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseEntity(@CreatedDate
                      @Column(updatable = false)
                      @Convert(converter = Jsr310JpaConverters.InstantConverter::class)
                      private var createdAt: Instant,
                      @Column(nullable = false)
                      private var createUser: String,
                      @LastModifiedDate
                      @Convert(converter = Jsr310JpaConverters.InstantConverter::class)
                      protected var updatedAt: Instant? = null,
                      @Column
                      protected var updateUser: String? = null) {


}