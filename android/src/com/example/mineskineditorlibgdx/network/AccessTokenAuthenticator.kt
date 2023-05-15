package com.example.mineskineditorlibgdx.network

//@Singleton
//class AccessTokenAuthenticator(
//    private val dropboxService: dagger.Lazy<DropboxService>,
//    private val clientId: String,
//    private val clientSecret: String,
//    private val dataStoreHandler: DataStoreHandler
//) : Authenticator {
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//        synchronized(this) {
//            return runBlocking {
//                val requestAccessToken = response.request.header("Authorization")
//                val localAccessToken = dataStoreHandler.getAccessToken()!!
//                if (requestAccessToken != localAccessToken) {
//                    return@runBlocking response.request.newBuilder()
//                        .header("Authorization", localAccessToken).build()
//                }
//                val newAccessToken = getNewAccessToken(clientId, clientSecret)
//                return@runBlocking if (newAccessToken == null) {
//                    // TODO probably add this stuff also
////                    unauthorizedEventDispatcher.requestDeauthorization()
//                    null
//                } else {
//                    response.request.newBuilder().header("Authorization", "Bearer $newAccessToken")
//                        .build()
//                }
//            }
//        }
//    }
//
//    private suspend fun getNewAccessToken(
//        clientId: String,
//        clientSecret: String
//    ): String? {
//        val refreshToken = dataStoreHandler.getRefreshToken() ?: return null
//        val response = tryOrNull {
//            dropboxService.get().accessToken(
//                refreshToken = refreshToken,
//                clientId = clientId,
//                clientSecret = clientSecret
//            )
//        }
//        response ?: return null
//        dataStoreHandler.setTokens(response.accessToken, response.refreshToken)
//        return response.accessToken
//    }
//
//}