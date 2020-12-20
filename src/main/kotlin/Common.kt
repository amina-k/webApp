import org.json.JSONObject

fun getSuccessResponse(data: Map<String, Any>? = null): JSONObject {
    return JSONObject(
        mapOf(
            "status" to "200",
            "message" to "success",
            "data" to data
        )
    )
}

fun getErrorResponse(): JSONObject {
    return JSONObject(
        mapOf(
            "status" to "200",
            "message" to "success"
        )
    )
}