package com.onetwo.reportservice.persistence.entity

import com.onetwo.reportservice.utils.BooleanNumberConverter
import jakarta.persistence.*
import java.time.Instant

@Entity
class ReportEntity(@Column(nullable = false)
                   var userId: String,
                   @Column(nullable = false)
                   var category: Int,
                   @Column(nullable = false)
                   var targetId: Long,
                   @Column(nullable = false)
                   var reason: String,
                   @Column(nullable = false, length = 1)
                   @Convert(converter = BooleanNumberConverter::class)
                   var state: Boolean,
                   createdAt: Instant,
                   createUser: String) : BaseEntity(createdAt, createUser) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun delete() {
        state = true
        updatedAt = Instant.now()
        updateUser = userId
    }
}