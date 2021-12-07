package com.mauricio.shoppingcart.utils.file

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object FileUtils {
    @JvmStatic
    fun loadFromAsset(context: Context?, file: String?): String? {
        var mJson: String? = null
        if (file.isNullOrEmpty()) return mJson
        var input: InputStream?
        if (context != null) input = context.assets.open(file) else input = javaClass.classLoader?.getResourceAsStream(file)

         mJson = try {
            val size = input?.available()
            val buffer = ByteArray(size!!)
            input?.read(buffer)
            input?.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
             input?.close()
            return null
        }
        return mJson
    }
}