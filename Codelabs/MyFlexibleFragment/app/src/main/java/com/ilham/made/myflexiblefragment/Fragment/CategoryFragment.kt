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
class CategoryFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnDetailCategory: Button = view.findViewById(R.id.btn_detail_category)
        btnDetailCategory.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_detail_category) {
            val mDetailCategoryFragment = DetailCategoryFragment()

            // kirim data dengan bundle
            val mBundle = Bundle()

            // 1. Data yang dikirim
            mBundle.putString(DetailCategoryFragment.EXTRA_NAME, "LifeStyle")
//            mBundle.putString(DetailCategoryFragment.EXTRA_DESCRIPTION, "Tes")

            // 2. Data deskripsi yang dikirim
            val description = "Kategori ini akan berisi produk - produk lifestyle"
            mDetailCategoryFragment.arguments = mBundle
            mDetailCategoryFragment.descriptions = description

            val mFragmentManager = fragmentManager as FragmentManager

            mFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, mDetailCategoryFragment, DetailCategoryFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }
    }


}
