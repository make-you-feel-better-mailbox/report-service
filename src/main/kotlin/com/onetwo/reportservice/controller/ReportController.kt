package com.onetwo.reportservice.controller

import com.onetwo.reportservice.common.GlobalURI
import com.onetwo.reportservice.dto.DeleteReportResponse
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.dto.RegisterReportResponse
import com.onetwo.reportservice.service.ReportService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class ReportController(private val reportService: ReportService) {

    /**
     * Register Report api
     *
     * @param registerReportRequest data about report target category and target id
     * @param userId   user authentication id
     * @return Boolean about report register success
     */
    @PostMapping(GlobalURI.REPORT_ROOT)
    fun registerReport(@RequestBody @Valid registerReportRequest: RegisterReportRequest,
                       @AuthenticationPrincipal userId: String): ResponseEntity<RegisterReportResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.registerReport(registerReportRequest, userId))
    }

    /**
     * Delete Report api
     *
     * @param category request target category
     * @param targetId request target id
     * @param userId   user authentication id
     * @return Boolean about report delete success
     */
    @DeleteMapping("${GlobalURI.REPORT_ROOT}${GlobalURI.PATH_VARIABLE_CATEGORY_WITH_BRACE}${GlobalURI.PATH_VARIABLE_TARGET_ID_WITH_BRACE}")
    fun deleteReport(@PathVariable(GlobalURI.PATH_VARIABLE_CATEGORY) category: Int,
                     @PathVariable(GlobalURI.PATH_VARIABLE_TARGET_ID) targetId: Long,
                     @AuthenticationPrincipal userId: String): ResponseEntity<DeleteReportResponse> {
        return ResponseEntity.ok().body(reportService.deleteReport(category, targetId, userId));
    }
}