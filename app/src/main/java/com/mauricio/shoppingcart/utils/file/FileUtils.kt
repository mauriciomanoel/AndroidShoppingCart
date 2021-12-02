package com.mauricio.shoppingcart.utils.file

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

object FileUtils {
    @JvmStatic
    fun loadFromAsset(context: Context?, file: String?): String? {
        var mJson: String? = null
        if (context != null && file != null) {
            mJson = try {
                val `is` = context.assets.open(file)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, Charset.forName("UTF-8"))
            } catch (ex: IOException) {
                ex.printStackTrace()
                context.assets.close()
                return null
            }
        }
        return mJson
    }
}