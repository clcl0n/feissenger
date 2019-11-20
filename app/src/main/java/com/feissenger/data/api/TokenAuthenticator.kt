package com.feissenger.data.api

import android.content.Context
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.data.api.model.RefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class TokenAuthenticator(val context: Context) : Authenticator {
    var access: String = ""
    var refresh: String = ""
    var uid: String = ""

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header("Needs-Auth")?.compareTo("true") == 0 && response.code() == 401) {
            val sharedPreferences = MySharedPreferences(context)
            with(sharedPreferences) {
                refresh = get("refresh") as String
                uid = get("uid") as String
                access = get("access") as String
            }

            if (!response.request().header("Authorization").equals(access))
                return null

            val tokenResponse =
                WebApi.create(context).refresh(RefreshRequest(uid, refresh)).execute()

            if (tokenResponse.isSuccessful) {
                access = "Bearer " + tokenResponse.body()!!.access

                with(sharedPreferences){
                    put("access", access)
                    put("refresh", tokenResponse.body()!!.refresh)
                }

                return response.request().newBuilder().header("Authorization", access).build()
            }
        }

        return null
    }
}