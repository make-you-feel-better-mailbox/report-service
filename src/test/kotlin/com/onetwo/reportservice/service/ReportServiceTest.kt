package com.onetwo.reportservice.service

import com.onetwo.reportservice.common.GlobalStatus
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
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class ReportServiceTest {

    @InjectMocks
    private var reportService: ReportServiceImpl? = null

    @Mock
    private var reportRepository: ReportRepository? = null

    private val reportId = 1L
    private val userId = "testUserId"
    private val category = 1
    private val targetId = 11L
    private val reason = "Because i hate it"

    @Test
    @DisplayName("[단위][Service] Report 등록 - 성공 테스트")
    fun registerReportSuccessTest() {
        //given
        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)

        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        report.id = 1L

        given(reportRepository!!.findByUserIdAndCategoryAndTargetIdAndState(userId, category, targetId, GlobalStatus.PERSISTENCE_NOT_DELETED))
                .willReturn(Optional.empty())
        given(reportRepository!!.save(any(ReportEntity::class.java)))
                .willReturn(report)

        //when
        val result: RegisterReportResponse = reportService!!.registerReport(registerReportRequest, userId)

        //then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.isRegisterSuccess)
    }

    @Test
    @DisplayName("[단위][Service] Report 등록 기등록 - 실패 테스트")
    fun registerReportAlreadyExistFailTest() {
        //given
        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)

        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        report.id = 1L

        given(reportRepository!!.findByUserIdAndCategoryAndTargetIdAndState(userId, category, targetId, GlobalStatus.PERSISTENCE_NOT_DELETED))
                .willReturn(Optional.of(report))

        //when then
        Assertions.assertThrows(BadRequestException::class.java) { reportService!!.registerReport(registerReportRequest, userId) }
    }

    @Test
    @DisplayName("[단위][Service] Report 삭제 - 성공 테스트")
    fun deleteReportSuccessTest() {
        //given
        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        report.id = 1L

        given(reportRepository!!.findByUserIdAndCategoryAndTargetIdAndState(userId, category, targetId, GlobalStatus.PERSISTENCE_NOT_DELETED))
                .willReturn(Optional.of(report))

        //when
        val result: DeleteReportResponse = reportService!!.deleteReport(category, targetId, userId)

        //then
        Assertions.assertNotNull(result)
        Assertions.assertTrue(result.isDeleteSuccess)
    }

    @Test
    @DisplayName("[단위][Service] Report 삭제 미존재 - 실패 테스트")
    fun deleteReportDoesNotExistFailTest() {
        //given
        given(reportRepository!!.findByUserIdAndCategoryAndTargetIdAndState(userId, category, targetId, GlobalStatus.PERSISTENCE_NOT_DELETED))
                .willReturn(Optional.empty())

        //when then
        Assertions.assertThrows(NotFoundResourceException::class.java) { reportService!!.deleteReport(category, targetId, userId) }
    }
}