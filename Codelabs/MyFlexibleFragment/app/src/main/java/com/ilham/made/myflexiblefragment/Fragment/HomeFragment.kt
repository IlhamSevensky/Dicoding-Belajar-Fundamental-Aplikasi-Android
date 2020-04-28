package com.ilham.made.myflexiblefragment.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager

import com.ilham.made.myflexiblefragment.R

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    // Definisikan layout xml ke objek view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // Override method untuk inisialisasi komponen view xml dan aksinya ( Dijalankan/bekerja setelah method onCreateView() )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCategory: Button = view.findViewById(R.id.btn_category)
        btnCategory.setOnClickListener(this)
    }

    // Implement OnClickListener
    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_category) {
            val mCategoryFragment = CategoryFragment() // inisialisasi fragment
            val mFragmentManager = fragmentManager as FragmentManager // inisialisasi fragment manager

            mFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, mCategoryFragment, CategoryFragment::class.java.simpleName)
                .addToBackStack(null) // untuk membuat fragment menumpuk di atas fragment sebelumnya ( sehingga jika ditekan tombol back akan kembali ke fragment sebelumnya ) dan jika tidak diberi akan keluar aplikasi saat ditekan back
                .commit()

            /*
               Method addToBackStack akan menambahkan fragment ke backstack
               Behaviour dari back button akan cek fragment dari backstack,
               jika ada fragment di dalam backstack maka fragment yang akan di close / remove
               jika sudah tidak ada fragment di dalam backstack maka activity yang akan di close / finish
            */
        }
    }


}
