package com.onetwo.reportservice.persistence.repository

import com.onetwo.reportservice.persistence.entity.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReportRepository : JpaRepository<ReportEntity, Long> {
    fun findByUserIdAndCategoryAndTargetIdAndState(userId: String, category: Int, targetId: Long, persistenceNotDeleted: Boolean): Optional<ReportEntity>
}