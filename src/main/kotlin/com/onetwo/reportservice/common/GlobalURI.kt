package com.onetwo.reportservice.common

class GlobalURI {

    companion object {
        const val ROOT_URI: String = "/report-service"
        const val REPORT_ROOT: String = "$ROOT_URI/reports"
        const val PATH_VARIABLE_CATEGORY: String = "category"
        const val PATH_VARIABLE_CATEGORY_WITH_BRACE: String = "/{$PATH_VARIABLE_CATEGORY}"

        const val PATH_VARIABLE_TARGET_ID: String = "target-id"
        const val PATH_VARIABLE_TARGET_ID_WITH_BRACE: String = "/{$PATH_VARIABLE_TARGET_ID}"
    }
}