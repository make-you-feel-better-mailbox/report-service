package com.onetwo.reportservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.onetwo.reportservice.common.GlobalURI
import com.onetwo.reportservice.config.TestHeader
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.persistence.entity.ReportEntity
import com.onetwo.reportservice.persistence.repository.ReportRepository
import onetwo.mailboxcommonconfig.common.GlobalStatus
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader::class)
class ReportControllerBootTest {
    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Autowired
    private val reportRepository: ReportRepository? = null

    @Autowired
    private val testHeader: TestHeader? = null

    private val userId = "testUserId"
    private val category = 1
    private val targetId = 11L
    private val reason = "Because i hate it"

    @Test
    @Transactional
    @DisplayName("[통합][Controller] Report 등록 - 성공 테스트")
    @Throws(Exception::class)
    fun registerReportSuccessTest() {
        //given
        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)

        //when
        val resultActions = mockMvc!!.perform(
                post(GlobalURI.REPORT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper!!.writeValueAsString(registerReportRequest))
                        .headers(testHeader!!.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON))
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("register-report",
                        requestHeaders(
                                headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                        ),
                        requestFields(
                                fieldWithPath("category").type(JsonFieldType.NUMBER).description("report를 등록할 target의 category ( 1: posting, 2: comment, 3: user"),
                                fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("report를 등록할 target id"),
                                fieldWithPath("reason").type(JsonFieldType.STRING).description("report를 등록할 이유")
                        ),
                        responseFields(
                                fieldWithPath("registerSuccess").type(JsonFieldType.BOOLEAN).description("등록 완료 여부")
                        )
                )
                )
    }

    @Test
    @Transactional
    @DisplayName("[통합][Controller] Report 삭제 - 성공 테스트")
    @Throws(Exception::class)
    fun deleteReportSuccessTest() {
        //given
        val report: ReportEntity = ReportEntity(userId, category, targetId, reason, false, Instant.now(), userId)

        reportRepository!!.save(report)

        //when
        val resultActions = mockMvc!!.perform(
                RestDocumentationRequestBuilders.delete("${GlobalURI.REPORT_ROOT}${GlobalURI.PATH_VARIABLE_CATEGORY_WITH_BRACE}${GlobalURI.PATH_VARIABLE_TARGET_ID_WITH_BRACE}",
                        category, targetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader!!.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON))
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-report",
                        requestHeaders(
                                headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                        ),
                        pathParameters(
                                parameterWithName(GlobalURI.PATH_VARIABLE_CATEGORY).description("삭제할 Report의 category"),
                                parameterWithName(GlobalURI.PATH_VARIABLE_TARGET_ID).description("삭제할 Report의 target id")
                        ),
                        responseFields(
                                fieldWithPath("deleteSuccess").type(JsonFieldType.BOOLEAN).description("삭제 성공 여부")
                        )
                )
                )
    }
}

