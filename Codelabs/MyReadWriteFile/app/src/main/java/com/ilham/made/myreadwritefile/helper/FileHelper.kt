package com.ilham.made.myreadwritefile.helper

import android.content.Context
import android.util.Log
import com.ilham.made.myreadwritefile.models.FileModel
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStreamWriter

internal object FileHelper {

    private val TAG = FileHelper::class.java.name

    fun writeToFile(fileModel: FileModel,context: Context){
        try {
            /*
                Jika menggunakan method openFileOutput, dibutuhkan context ( itulah kenapa kita butuh parameter context )
             */
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileModel.filename.toString(), Context.MODE_PRIVATE))
            outputStreamWriter.write(fileModel.data.toString())
            outputStreamWriter.close()
        } catch (e: IOException){
            Log.e(TAG, "File write failed :", e)
        }
    }

    fun readFromFile(context: Context, filename: String) : FileModel {

        val fileModel = FileModel()

        try {
            val inputStream = context.openFileInput(filename)

            if (inputStream != null){

                /*
                    there is 2 method, you can choose 1 of them
                 */
                inputStream.bufferedReader().use { bufferedReader ->
                    fileModel.data = bufferedReader.readText()
                    fileModel.filename = filename
                }
                inputStream.close()

//                val receiveString = inputStream.bufferedReader().use(BufferedReader::readText)
//                inputStream.close()
//                fileModel.data = receiveString
//                fileModel.filename = filename

            }
        } catch (e: FileNotFoundException){
            Log.e(TAG, "File not found :", e)
        } catch (e: IOException){
            Log.e(TAG, "Can not read file :", e)
        }

        return fileModel
    }
}