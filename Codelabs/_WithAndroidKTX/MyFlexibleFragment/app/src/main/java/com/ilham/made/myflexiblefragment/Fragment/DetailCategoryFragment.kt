package com.ilham.made.myflexiblefragment.Fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ilham.made.myflexiblefragment.ProfileActivity

import com.ilham.made.myflexiblefragment.R
import kotlinx.android.synthetic.main.fragment_detail_category.*

/**
 * A simple [Fragment] subclass.
 */
class DetailCategoryFragment : Fragment(), View.OnClickListener {

    var descriptions: String? = null

    lateinit var tvCategoryName: TextView
    lateinit var tvCategoryDescription: TextView
    lateinit var btnProfile: Button
    lateinit var btnShowDialog: Button

    companion object {
        var EXTRA_NAME = "extra_name"
        var EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCategoryName = view.findViewById(R.id.tv_category_name)
        tvCategoryDescription = view.findViewById(R.id.tv_category_description)
        btnProfile = view.findViewById(R.id.btn_profile)
        btnShowDialog = view.findViewById(R.id.btn_show_dialog)

        btnProfile.setOnClickListener(this)
        btnShowDialog.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            val descFromBundle = savedInstanceState.getString(EXTRA_DESCRIPTION)
            descriptions = descFromBundle
        }

        // Menerima data menggunakan bundle
        if (arguments != null) {
            val categoryName = arguments?.getString(EXTRA_NAME)
            tv_category_name.text = categoryName
            tv_category_description.text = descriptions

            // coba
//            val categoryDesc = arguments?.getString(EXTRA_DESCRIPTION)
//            tv_category_description.text = categoryDesc
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_profile -> {
                val mIntent = Intent(activity,ProfileActivity::class.java) // untuk mendapatkan context dari activity di fragment menggunakan activity from getActivity karena this@MainActivity hanya berlaku untuk activity murni
                startActivity(mIntent)
            }

            R.id.btn_show_dialog -> { // tampilkan dialog sebagai anak dari fragmen ini / diatas fragment ini sebagai anak dari fragment ini
                val mOptionDialogFragment = OptionDialogFragment()

                val mFragmentManager = childFragmentManager
                mOptionDialogFragment.show(mFragmentManager,OptionDialogFragment::class.java.simpleName)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_DESCRIPTION, descriptions)
    }

    internal var optionDialogListener: OptionDialogFragment.OnOptionDialogListener = object : OptionDialogFragment.OnOptionDialogListener { // fungsi turunan dari interface yang ada di OptionDialogFragment untuk menampilkan toast hasil pilihan
        override fun onOptionChosen(text: String?) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }
}
