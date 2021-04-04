package ga.kojin.bump.helpers

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import kotlin.collections.ArrayList


class SharedPreferences(val context: Context) {
    private val PREFS_NAME = "BUMP"

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)

    }

    fun clearSharedPreferences() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun saveIntList(name: String?, list: List<Int>) {
        var s = ""
        for (i in list) {
            s += "$i,"
        }
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(name, s)
        editor.apply()
    }

    fun readIntArray(name: String): ArrayList<Int> {
        val s = sharedPref.getString(name, "")
        val st = StringTokenizer(s, ",")
        val result = ArrayList<Int>()
        while (st.hasMoreTokens()) {
            result.add(st.nextToken().toInt())
        }
        return result
    }

    fun saveStrList(name: String, list: List<String>) {
        var s = ""
        for (i in list) {
            s += "$i,"
        }
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(name, s)
        editor.apply()
    }

    fun readStrList(name: String): ArrayList<String> {
        val s = sharedPref.getString(name, "")
        val st = StringTokenizer(s, ",")
        val result = ArrayList<String>()
        while (st.hasMoreTokens()) {
            result.add(st.nextToken())
        }
        return result
    }
}