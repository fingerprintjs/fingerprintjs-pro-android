package com.fingerprintjs.android.fpjs_pro_demo


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


@Throws(JSONException::class)
fun JSONObject.toMap(): Map<String, Any> {
    val map: MutableMap<String, Any> = HashMap()
    val keys = this.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        var value = this[key]
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = value.toMap()
        }
        map[key] = value
    }
    return map
}

@Throws(JSONException::class)
fun toList(array: JSONArray): List<Any> {
    val list: MutableList<Any> = ArrayList()
    for (i in 0 until array.length()) {
        var value = array[i]
        if (value is JSONArray) {
            value = toList(value)
        } else if (value is JSONObject) {
            value = value.toMap()
        }
        list.add(value)
    }
    return list
}