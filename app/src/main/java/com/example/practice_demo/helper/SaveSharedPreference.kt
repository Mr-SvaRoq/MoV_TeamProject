package com.example.practice_demo.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.google.gson.Gson
import com.google.gson.JsonParseException

/**
 * Drzi status o prihlaseni pouzivatela, prezije to aj
 * zavretie appky
 *
 * TODO: ulozia sa sem vsetky data ktore vracia login
 */
class SaveSharedPreference {
    companion object {
        private const val PREF_USER_NAME = "userdata"
        private val gson = Gson()

        private fun getSharedPreferences(ctx: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        /**
         * Nasetuj username -> login
         */
        fun setUser(ctx: Context, user: UserLoginResponse) {
            getSharedPreferences(ctx)?.edit()?.let {editor ->
                val userJson = gson.toJson(user)
                editor.putString(PREF_USER_NAME, userJson)
                editor.commit()
            }
        }

        /**
         * Ziskaj ulozene data
         */
        fun getUser(ctx: Context): UserLoginResponse? {
            var user: UserLoginResponse? = null

            try {
                getSharedPreferences(ctx)?.getString(PREF_USER_NAME, "").let {
                    user = gson.fromJson(it, UserLoginResponse::class.java)
                }

                return user
            } catch (e: JsonParseException) {
                return null
            }
        }

        /**
         * Over, ci je user prihlaseny
         */
        fun hasUser(ctx: Context): Boolean =
            getUser(ctx) != null

        /**
         * Vycisti data -> logout
         */
        fun clearUsername(ctx: Context) {
            getSharedPreferences(ctx)?.edit()?.let {editor ->
                editor.clear()
                editor.commit()
            }
        }
    }
}