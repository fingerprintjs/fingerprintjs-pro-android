package com.fingerprintjs.android.fpjs_pro.transport.http_client


import com.fingerprintjs.android.fpjs_pro.logger.Logger
import com.fingerprintjs.android.fpjs_pro.tools.executeSafe
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


internal interface HttpClient {
    fun performRequest(
        request: Request
    ): RawRequestResult
}

internal class HttpClientImpl(
    private val logger: Logger
) : HttpClient {
    override fun performRequest(request: Request): RawRequestResult {
        return executeSafe({
            sendPostRequest(request)
        }, RawRequestResult(null))
    }

    private fun sendPostRequest(request: Request): RawRequestResult {

        val reqParamJson = JSONObject(request.bodyAsMap())
        logger.debug(this, reqParamJson)

        val mURL = URL(request.url)

        with(mURL.openConnection() as HttpsURLConnection) {
            logger.debug(this, "Headers:$")
            request.headers.keys.forEach {
                logger.debug(this, "$it ${request.headers[it]}")
                setRequestProperty(it, request.headers[it])
            }

            doOutput = true
            val wr = OutputStreamWriter(outputStream)
            wr.write(reqParamJson.toString())
            wr.flush()

            logger.debug(this, "URL : $url")
            logger.debug(this, "Response Code : $responseCode")

            val status: Int = responseCode

            val resultInputStream = if (status != HttpURLConnection.HTTP_OK) {
                errorStream
            } else {
                inputStream
            }

            BufferedReader(InputStreamReader(resultInputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }

                logger.debug(this, "Response : $response")

                return RawRequestResult(
                    response.toString().toByteArray()
                )
            }
        }
    }
}
