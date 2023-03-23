package com.leandrolcd.dogedexmvvm.auth.model

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson


data class User(
    val id: Long, val email: String, val authenticationToken: String
){



 companion object {
    private const val AUTH_PREFS = "users"
    private const val USER_PREFS = "data"
    fun setLoggedInUser(activity: Activity, user: User) {
        val userString = Gson().toJson(user)
        activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).also {
            it.edit().putString(USER_PREFS, userString).apply()
            Log.i("setUser", userString.orEmpty())
        }

    }

    fun getLoggedInUser(activity: Activity): User? {
        val prefs = activity.getSharedPreferences(AUTH_PREFS,
            Context.MODE_PRIVATE) ?: return null
       val user = Gson().fromJson(prefs.getString(USER_PREFS, ""), User::class.java)
        if (user != null && user.id == 0L) return null
        Log.i("getUser", prefs.getString(USER_PREFS, "")!!)
        return user
    }
    fun logout(activity: Activity){
        activity.getSharedPreferences(AUTH_PREFS,
            Context.MODE_PRIVATE).also {
                it.edit().clear().apply()
        }
    }

}
}
