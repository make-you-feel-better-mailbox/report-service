package com.onetwo.reportservice.service

import com.onetwo.reportservice.common.GlobalStatus
import com.onetwo.reportservice.dto.DeleteReportResponse
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.dto.RegisterReportResponse
import com.onetwo.reportservice.persistence.entity.ReportEntity
import com.onetwo.reportservice.persistence.repository.ReportRepository
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ReportServiceImpl(val reportRepository: ReportRepository) : ReportService {

    /**
     * Register Report service
     *
     * @param registerReportRequest data about report target category and target id
     * @param userId   user authentication id
     * @return Boolean about report register success
     */
    @Transactional
    override fun registerReport(registerReportRequest: RegisterReportRequest, userId: String): RegisterReportResponse {
        reportRepository.findByUserIdAndCategoryAndTargetIdAndState(
                userId,
                registerReportRequest.category,
                registerReportRequest.targetId,
                GlobalStatus.PERSISTENCE_NOT_DELETED
        ).ifPresent { throw BadRequestException("report already exist") }

        val newReport = ReportEntity(userId,
                registerReportRequest.category,
                registerReportRequest.targetId,
                registerReportRequest.reason,
                GlobalStatus.PERSISTENCE_NOT_DELETED,
                Instant.now(),
                userId
        )

        val savedReport = reportRepository.save(newReport)

        return RegisterReportResponse(savedReport.let { savedReport.id?.let { true } } ?: false)
    }

    /**
     * Delete Report service
     *
     * @param category request target category
     * @param targetId request target id
     * @param userId   user authentication id
     * @return Boolean about report delete success
     */
    @Transactional
    override fun deleteReport(category: Int, targetId: Long, userId: String): DeleteReportResponse {
        val report = reportRepository.findByUserIdAndCategoryAndTargetIdAndState(
                userId,
                category,
                targetId,
                GlobalStatus.PERSISTENCE_NOT_DELETED
        ).orElseThrow { throw NotFoundResourceException("report does not exist") }

        report.delete()

        return DeleteReportResponse(report.state)
    }
}