package com.ilham.made.myreadwritefile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ilham.made.myreadwritefile.helper.FileHelper
import com.ilham.made.myreadwritefile.models.FileModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_new.setOnClickListener(this)
        button_open.setOnClickListener(this)
        button_save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.button_new -> newFile()
            R.id.button_open -> showList()
            R.id.button_save -> saveFile()
        }

    }

    private fun newFile(){
        edit_file.setText("")
        edit_title.setText("")
        showToast(this,"Clearing File")
    }

    private fun showList(){
        val arrayList = ArrayList<String>()
        /*
            Obyek filesDir() akan secara otomatis memperoleh path dari internal storage aplikasi Anda.
            Dengan menggunakan .list(), Anda akan memperoleh semua nama berkas yang ada.
            Tiap berkas yang ditemukan ditambahkan ke dalam obyek arrayList.
         */
        val path: File = filesDir

        Collections.addAll(arrayList, *path.list() as Array<String>)
        val items = arrayList.toTypedArray<CharSequence>()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih file yang diinginkan")
        builder.setItems(items) { _, item ->
            loadData(items[item].toString())
        }

        val alert = builder.create()
        alert.show()
    }

    private fun loadData(title: String){
        val fileModel = FileHelper.readFromFile(this, title)
        edit_title.setText(fileModel.filename)
        edit_file.setText(fileModel.data)
        showToast(this,"Loading " + fileModel.filename + " data")
    }

    private fun saveFile(){
        when{
            edit_title.text.toString().isEmpty() -> showToast(this, "Title harus diisi terlebih dahulu")
            edit_file.text.toString().isEmpty() -> showToast(this, "Konten harus diisi terlebih dahulu")
            else -> {
                val title = edit_title.text.toString()
                val text = edit_file.text.toString()
                val fileModel = FileModel()
                fileModel.filename = title
                fileModel.data = text
                FileHelper.writeToFile(fileModel, this)
                showToast(this, "Saving " + fileModel.filename + " file")
            }
        }
    }

    private fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
