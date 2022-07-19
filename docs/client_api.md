# FingerprintJS Pro Android Client API


## Full public API

```kotlin

interface FingerprintJS {
    fun getVisitorId(listener: (FingerprintJSProResponse) -> Unit)
    fun getVisitorId(listener: (FingerprintJSProResponse) -> Unit, errorListener: (Error) -> (Unit))
    fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> (Unit)
    )
    fun getVisitorId(
        linkedId: String,
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> (Unit)
    )
    fun getVisitorId(
        tags: Map<String, Any>,
        linkedId: String,
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> (Unit)
    )
}

```


### Configuration

```kotlin

class Configuration @JvmOverloads constructor(
    val apiKey: String,
    val region: Region = Region.US,
    val endpointUrl: String = region.endpointUrl,
    val extendedResponseFormat: Boolean = false
)

```

By default extended result is not returned. Change the flag in configuration to get the extended one.


You can find your [Public API key](https://dev.fingerprint.com/docs) in your [dashboard](https://dashboard.fingerprint.com/subscriptions/).

### Response format

```kotlin

package com.fingerprintjs.android.fpjs_pro


data class FingerprintJSProResponse(
    val requestId: String,
    val visitorId: String,
    val confidenceScore: ConfidenceScore,
    val visitorFound: Boolean, // Available with extendedResponseFormat == true
    val ipAddress: String, // Available with extendedResponseFormat == true
    val ipLocation: IpLocation?, // Available with extendedResponseFormat == true
    val osName: String, // Available with extendedResponseFormat == true
    val osVersion: String, // Available with extendedResponseFormat == true
    val firstSeenAt: Timestamp,// Available with extendedResponseFormat == true
    val lastSeenAt: Timestamp, // Available with extendedResponseFormat == true
    val errorMessage: String? = null
)

data class IpLocation(
    val accuracyRadius: Int,
    val latitude: Double,
    val longitude: Double,
    val postalCode: String,
    val timezone: String,
    val city: City,
    val country: Country,
    val continent: Continent,
    val subdivisions: List<Subdivisions>
) {

    data class City(
        val name: String
    )

    data class Country(
        val code: String,
        val name: String
    )

    data class Continent(
        val code: String,
        val name: String
    )

    data class Subdivisions(
        val isoCode: String,
        val name: String
    )
}

data class ConfidenceScore(
    val score: Double
)

data class Timestamp(
    val global: String,
    val subscription: String
)

```

### Error handling

```kotlin
fpjsClient.getVisitorId(
    listener = { visitorId ->
        // Handle ID
    },
    errorListener = { error ->
        when(error){
            is ApiKeyRequired -> {
                val requestId = error.requestId
                // Handle error
            }
                ...
        }
    })

```

Error is a sealed class

```kotlin
sealed class Error(
    val requestId: String = UNKNOWN,
    val description: String? = UNKNOWN
)
```

and it might me one of:

- ApiKeyRequired
- ApiKeyNotFound
- ApiKeyExpired
- RequestCannotBeParsed
- Failed
- RequestTimeout
- TooManyRequest
- OriginNotAvailable
- HeaderRestricted
- NotAvailableForCrawlBots
- NotAvailableWithoutUA
- WrongRegion
- SubscriptionNotActive
- UnsupportedVersion
- InstallationMethodRestricted
- ResponseCannotBeParsed
- NetworkError
- UnknownError

### [Linked ID](https://dev.fingerprint.com/docs/js-agent#linkedid) support

```kotlin
 fpjsClient.getVisitorId(
      linkedId = "your_linked_id",
      listener = { visitorId ->
          // Handle ID
      },
      errorListener = { error ->
          // Handle error
      })
```


### [Tags](https://dev.fingerprint.com/v2/docs/js-agent#tag) support

```kotlin
 fpjsClient.getVisitorId(
      tags = mapOf("sessionId" to sessionId),
      listener = { visitorId ->
          // Handle ID
      },
      errorListener = { error ->
          // Handle error
      })
```

Tags are returned in the webhook response so make sure the map you are passing to the library represents a valid JSON.

You can use both tags and linked id:

```kotlin

 fpjsClient.getVisitorId(
     tags = mapOf("sessionId" to sessionId),
     linkedId = "your_linked_id",
     listener = { visitorId ->
          // Handle ID
      },
      errorListener = { error ->
          // Handle error
      })

```
