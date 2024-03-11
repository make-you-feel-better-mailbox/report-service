package com.onetwo.reportservice.service

import com.onetwo.reportservice.dto.DeleteReportResponse
import com.onetwo.reportservice.dto.RegisterReportRequest
import com.onetwo.reportservice.dto.RegisterReportResponse

interface ReportService {

    /**
     * Register Report service
     *
     * @param registerReportRequest data about report target category and target id
     * @param userId   user authentication id
     * @return Boolean about report register success
     */
    fun registerReport(registerReportRequest: RegisterReportRequest, userId: String): RegisterReportResponse

    /**
     * Delete Report service
     *
     * @param category request target category
     * @param targetId request target id
     * @param userId   user authentication id
     * @return Boolean about report delete success
     */
    fun deleteReport(category: Int, targetId: Long, userId: String): DeleteReportResponse
}