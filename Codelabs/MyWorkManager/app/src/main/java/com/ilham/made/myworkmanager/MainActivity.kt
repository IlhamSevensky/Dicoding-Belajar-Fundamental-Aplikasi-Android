package com.ilham.made.myworkmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOneTimeTask.setOnClickListener(this)
        btnPeriodicTask.setOnClickListener(this)
        btnCancelTask.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnOneTimeTask -> startOneTimeTask()
            R.id.btnPeriodicTask -> startPeriodicTask()
            R.id.btnCancelTask -> cancelPeriodicTask()
        }
    }

    private fun startOneTimeTask() {
        textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, editCity.text.toString())
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .build()

        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity, object : Observer<WorkInfo> {
                override fun onChanged(workInfo: WorkInfo?) {
                    val status = workInfo?.state?.name
                    textStatus.append("\n" + status)
                }
            })
    }

    private fun startPeriodicTask() {
        textStatus.text = getString(R.string.status)

        // mengirim data ke work manager dengan format key value
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, editCity.text.toString())
            .build()

        // syarat agar workmanager dijalankan
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // harus terkoneksi internet
            .build()

        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setInputData(data) // mengambil data
                .setConstraints(constraints)
                .addTag("PERIOD") // contoh dengan tag
                .build()

        WorkManager.getInstance().enqueue(periodicWorkRequest)
        // untuk mengecek state workmanager secara live berdasarkan id nya
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity, object : Observer<WorkInfo> {
                override fun onChanged(workInfo: WorkInfo?) {
                    val status = workInfo?.state?.name
                    textStatus.append("\n" + status)
                    btnCancelTask.isEnabled = false
                    if (workInfo?.state == WorkInfo.State.ENQUEUED) {
                        btnCancelTask.isEnabled = true
                    }
                }
            })
    }

    private fun cancelPeriodicTask() {
        // untuk membatalkan task berdasarkan id
        WorkManager.getInstance().cancelWorkById(periodicWorkRequest.id)

        // bisa membatalkan banyak task dengan menggunakan tag, caranya adalah pada saat membuat task, tambahkan .addTag dan pada saat cancel, bisa dengan method cancelAllWorkByTags
        // contoh
        // WorkManager.getInstance().cancelAllWorkByTag("PERIOD")
    }
}
