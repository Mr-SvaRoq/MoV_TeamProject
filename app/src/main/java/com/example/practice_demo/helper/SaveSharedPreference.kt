package com.example.practice_demo.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Drzi status o prihlaseni pouzivatela, prezije to aj
 * zavretie appky
 *
 * TODO: ulozia sa sem vsetky data ktore vracia login
 */
class SaveSharedPreference {
    companion object {
        private const val PREF_USER_NAME = "username"

        private fun getSharedPreferences(ctx: Context): SharedPreferences? {
            return PreferenceManager.getDefaultSharedPreferences(ctx)
        }

        /**
         * Nasetuj username -> login
         */
        fun setUsername(ctx: Context, username: String) {
            getSharedPreferences(ctx)?.edit()?.let {editor ->
                editor.putString(PREF_USER_NAME, username)
                editor.commit()
            }
        }

        /**
         * Ziskaj ulozene data
         */
        fun getUsername(ctx: Context): String? =
            getSharedPreferences(ctx)?.getString(PREF_USER_NAME, "")

        /**
         * Over, ci je user prihlaseny
         */
        fun hasUsername(ctx: Context): Boolean =
            getUsername(ctx)?.isEmpty() != true

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