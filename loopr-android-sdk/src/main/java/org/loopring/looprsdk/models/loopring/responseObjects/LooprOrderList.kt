package org.loopring.looprsdk.models.loopring.responseObjects

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName


import java.lang.reflect.Type

class LooprOrderList(
        override var id: Int? = null, override var jsonrpc: String? = null
) : LooprResponse {

    /**
     * List of [LooprOrder] objects with information about the order
     */
    var orders: ArrayList<LooprOrderItem>? = null

    /**
     * Total amount of orders
     * Example output - 12
     */
    @SerializedName("total")
    var total: Int? = null

    /**
     * Index of page
     * Example output - 3
     */
    @SerializedName("pageIndex")
    var pageIndex: Int? = null

    /**
     * Number of results per page
     * Example output - 10
     */
    @SerializedName("pageSize")
    var pageSize: Int? = null

    /**
     * Custom class deserializer
     */
    class LooprOrderListDeserializer : JsonDeserializer<LooprOrderList> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LooprOrderList? {
            if (json.isJsonNull || json.isJsonPrimitive) {
                return null
            } else {
                val jsonObj = json.asJsonObject
                val orderList = LooprOrderList()

                LooprResponse.checkForError(jsonObj)
                orderList.setIdJsonRPC(jsonObj)

                //TODO - check if this code is enough to handle normally encountered errors
                jsonObj.get("result")?.let {
                    orderList.total = it.asJsonObject.get("total").asString.toIntOrNull()
                    orderList.pageIndex = it.asJsonObject.get("pageIndex").asString.toIntOrNull()
                    orderList.pageSize = it.asJsonObject.get("pageSize").asString.toIntOrNull()

                    val dataArray = it.asJsonObject.get("data").asJsonArray
                    orderList.orders = ArrayList()
                    dataArray.forEach {
                        orderList.orders?.add(context.deserialize(it, LooprOrderItem::class.java))
                    }
                }

                return orderList
            }
        }

    }

    companion object {
        val ORDER_OPENED = "ORDER_OPENED" //Includes new and partial
        val ORDER_NEW = "ORDER_NEW"
        val ORDER_PARTIAL = "ORDER_PARTIAL"
        val ORDER_FINISHED = "ORDER_FINISHED"
        val ORDER_CANCEL = "ORDER_CANCEL"
        val ORDER_CUTOFF = "ORDER_CUTOFF"
    }
}