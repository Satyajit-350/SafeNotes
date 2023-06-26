package com.example.safenotes.utils

import android.content.Context
import com.example.safenotes.utils.Constants.PREFS_TOKEN
import com.example.safenotes.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN,Context.MODE_PRIVATE)
    //save token
    fun saveToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }
    //get saved token
    fun getToken():String?{
        return prefs.getString(USER_TOKEN,null)
    }

    fun logout(){
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}