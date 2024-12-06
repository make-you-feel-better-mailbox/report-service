package com.onetwo.reportservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.onetwo.reportservice.common.GlobalURI
import com.onetwo.reportservice.common.SecurityConfig
import com.onetwo.reportservice.config.TestConfig
import com.onetwo.reportservice.dto.DeleteReportResponse
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.dto.RegisterReportResponse
import com.onetwo.reportservice.service.ReportService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ReportController::class],
        excludeFilters = [
            ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                    classes = [SecurityConfig::class
                    ])
        ])
@Import(TestConfig::class)
class ReportControllerTest {
    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @MockBean
    private val reportService: ReportService? = null

    @Autowired
    private val testConfig: TestConfig? = null

    private val userId = "testUserId"
    private val category = 1
    private val targetId = 11L
    private val reason = "Because i hate it"

    @Test
    @DisplayName("[단위][Controller] Report 등록 - 성공 테스트")
    @Throws(Exception::class)
    fun registerReportSuccessTest() {
        //given
        val authorities: Set<GrantedAuthority> = testConfig!!.getGrantedAuthoritiesByUserId(userId)

        val auth = UsernamePasswordAuthenticationToken(userId, userId, authorities)
        SecurityContextHolder.getContext().authentication = auth

        val registerReportRequest: RegisterReportRequest = RegisterReportRequest(category, targetId, reason)
        val registerReportResponse: RegisterReportResponse = RegisterReportResponse(true)

        Mockito.`when`(reportService!!.registerReport(registerReportRequest, userId)).thenReturn(registerReportResponse)
        //when
        val resultActions = mockMvc!!.perform(
                post(GlobalURI.REPORT_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper!!.writeValueAsString(registerReportRequest))
                        .accept(MediaType.APPLICATION_JSON))
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
    }

    @Test
    @DisplayName("[단위][Controller] Report 삭제 - 성공 테스트")
    @Throws(Exception::class)
    fun deleteReportSuccessTest() {
        //given
        val authorities: Set<GrantedAuthority> = testConfig!!.getGrantedAuthoritiesByUserId(userId)

        val auth = UsernamePasswordAuthenticationToken(userId, userId, authorities)
        SecurityContextHolder.getContext().authentication = auth

        val deleteReportResponse: DeleteReportResponse = DeleteReportResponse(true)

        Mockito.`when`(reportService!!.deleteReport(category, targetId, userId)).thenReturn(deleteReportResponse)
        //when
        val resultActions = mockMvc!!.perform(
                delete("${GlobalURI.REPORT_ROOT}${GlobalURI.PATH_VARIABLE_CATEGORY_WITH_BRACE}${GlobalURI.PATH_VARIABLE_TARGET_ID_WITH_BRACE}",
                        category, targetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
    }
}

