package com.onetwo.reportservice.service

import com.onetwo.reportservice.dto.DeleteReportResponse
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.dto.RegisterReportResponse
import com.onetwo.reportservice.persistence.entity.ReportEntity
import com.onetwo.reportservice.persistence.repository.ReportRepository
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@SpringBootTest
@Transactional
class ReportServiceBootTest {

    @Autowired
    private var reportService: ReportService? = null

    @Autowired
    private var reportRepository: ReportRepository? = null

    private val userId = "testUserId"
    private val category = 1
    private val targetId = 11L
    private val reason = "Because i hate it"

    @Test
    @DisplayName("[통합][Service] Report 등록 - 성공 테스트")
    fun registerReportSuccessTest() {
        //given
        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)

        //when
        val result: RegisterReportResponse = reportService!!.registerReport(registerReportRequest, userId)

        //then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.isRegisterSuccess)
    }

    @Test
    @DisplayName("[통합][Service] Report 등록 기등록 - 실패 테스트")
    fun registerReportAlreadyExistFailTest() {
        //given
        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)

        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        reportRepository!!.save(report)

        //when then
        Assertions.assertThrows(BadRequestException::class.java) { reportService!!.registerReport(registerReportRequest, userId) }
    }

    @Test
    @DisplayName("[통합][Service] Report 삭제 - 성공 테스트")
    fun deleteReportSuccessTest() {
        //given
        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        reportRepository!!.save(report)

        //when
        val result: DeleteReportResponse = reportService!!.deleteReport(category, targetId, userId)

        //then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.isDeleteSuccess)
    }

    @Test
    @DisplayName("[통합][Service] Report 삭제 미존재 - 실패 테스트")
    fun deleteReportDoesNotExistFailTest() {
        //given when then
        Assertions.assertThrows(NotFoundResourceException::class.java) { reportService!!.deleteReport(category, targetId, userId) }
    }
}