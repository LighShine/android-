package com.lizhiheng.myapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class ConvertImage {
    companion object {

        // 将Base64编码的字符串解析为Bitmap对象
        fun convertToBitmap(imageAsString: String): Bitmap {
            val byteArrayAsDecodedString = android.util.Base64.decode(imageAsString, android.util.Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(
                byteArrayAsDecodedString,
                0,
                byteArrayAsDecodedString.size
            )
        }

        // 调整图片大小并压缩以减少Base64编码字符串的长度
        private fun resizeAndCompressImage(bitmap: Bitmap, coefficient: Double, quality: Int): String? {
            val resizedBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * coefficient).toInt(),
                (bitmap.height * coefficient).toInt(),
                true
            )
            val stream = ByteArrayOutputStream()
            val resultCompress = resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            return if (resultCompress) {
                val byteArray = stream.toByteArray()
                android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
            } else null
        }

        // 将Bitmap对象转换为Base64编码的字符串
        fun convertToString(bitmap: Bitmap): String? {
            val stream = ByteArrayOutputStream()
            val resultCompress = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream) // 初始质量设为90
            return if (resultCompress) {
                val byteArray = stream.toByteArray()
                when {
                    byteArray.size > 2000000 -> resizeAndCompressImage(bitmap, 0.1, 50)
                    byteArray.size in 1000000..2000000 -> resizeAndCompressImage(bitmap, 0.5, 70)
                    else -> android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
                }
            } else null
        }
    }
}
