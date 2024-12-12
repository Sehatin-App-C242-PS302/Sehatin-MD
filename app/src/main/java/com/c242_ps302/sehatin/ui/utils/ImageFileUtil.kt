package com.c242_ps302.sehatin.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val MAXIMAL_SIZE = 5000000
private val timeStamp: String =
    SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun formatDate(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        if (dateString.contains("T")) {
            val instant = Instant.parse(dateString)
            formatter.format(instant)
        } else {
            val localDate =
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
    } catch (e: Exception) {
        Log.d("Utils", "Error parsing date", e)
        dateString
    }
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpeg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
        buffer,
        0,
        length
    )
    outputStream.close()
    inputStream.close()
    return myFile
}

fun bitmapToFile(bitmap: Bitmap, context: Context): File {
    val myFile = createCustomTempFile(context)
    val outputStream = FileOutputStream(myFile)

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

    outputStream.flush()
    outputStream.close()

    return myFile
}

fun File.compressImageSize(): File {
    val file = this
    val bitmap = getBitmapWithCorrectRotation(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 10
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

private fun getBitmapWithCorrectRotation(imagePath: String): Bitmap? {
    val options = BitmapFactory.Options()
    var bitmap = BitmapFactory.decodeFile(imagePath, options)

    try {
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        bitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return bitmap
}

private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}