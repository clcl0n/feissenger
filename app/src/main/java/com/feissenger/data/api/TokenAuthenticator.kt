package com.feissenger.data.api

import android.content.Context
import com.feissenger.MainActivity
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
            val sharedPreferences = context.getSharedPreferences(
                MainActivity::class.java.simpleName,
                Context.MODE_PRIVATE
            )
            with(sharedPreferences) {
                refresh = getString("refresh", "")!!
                uid = getString("uid", "")!!
                access = getString("access", "")!!
            }

            if (!response.request().header("Authorization").equals(access))
                return null

            val tokenResponse =
                WebApi.create(context).refresh(RefreshRequest(uid, refresh)).execute()

            if (tokenResponse.isSuccessful) {
                access = "Bearer " + tokenResponse.body()!!.access

                sharedPreferences.edit()
                    .putString("access", access)
                    .putString("refresh", tokenResponse.body()!!.refresh)
                    .apply()


                return response.request().newBuilder().header("Authorization", access).build()
            }
        }

        return null
    }
}