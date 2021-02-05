package com.swirepay.android_sdk.model

class SuccessResponse<T>(val entity: T) : BaseResponse()

class SuccessPageResponse<T>(val entity: ContentResponse<T>) : BaseResponse()

open class BaseResponse{
     var message: String = ""
     var responseCode: Int = -1
     var status: String = ""
}

open class PageResponse {
     var pageable: Any? = null
     var first: String? = null
     var number: String? = null
     var last: Boolean? = null
     var totalElements: String? = null
     var totalPages: String? = null
     var size: String? = null
     var numberOfElements: String? = null
     var sort: Any? = null
     var empty: Boolean? = null
}

class ContentResponse<T> : PageResponse() {
    var content: List<T>? = null
}

class SuccessListResponse<T>(val entity: List<T>) : BaseResponse()


