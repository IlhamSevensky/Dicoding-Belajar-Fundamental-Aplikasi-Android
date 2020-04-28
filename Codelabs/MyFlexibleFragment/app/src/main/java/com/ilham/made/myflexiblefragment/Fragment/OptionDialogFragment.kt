package com.ilham.made.myflexiblefragment.Fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

import com.ilham.made.myflexiblefragment.R
import kotlinx.android.synthetic.main.fragment_option_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class OptionDialogFragment : DialogFragment(), View.OnClickListener { // Ubah Extend Fragment ke Dialog Fragment agar mengubahnya menjadi objek dialog

    private lateinit var btnChoose: Button
    private lateinit var btnClose: Button
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbSaf: RadioButton
    private lateinit var rbMou: RadioButton
    private lateinit var rbLvg: RadioButton
    private lateinit var rbMoyes: RadioButton
    private var optionDialogListener: OnOptionDialogListener? = null // inisialisasi option dialog dari interface OnOptionDialogListener (Buat Sendiri)


    interface OnOptionDialogListener {  // interface OnOptionDialogListener dengan fungsi onOptionChosen
        fun onOptionChosen(text: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChoose = view.findViewById(R.id.btn_choose)
        btnClose = view.findViewById(R.id.btn_close)
        btnChoose.setOnClickListener(this)
        btnClose.setOnClickListener(this)
        rgOptions = view.findViewById(R.id.rg_options)
        rbSaf = view.findViewById(R.id.rb_saf)
        rbMou = view.findViewById(R.id.rb_mou)
        rbLvg = view.findViewById(R.id.rb_lvg)
        rbMoyes = view.findViewById(R.id.rb_moyes)
    }

    override fun onAttach(context: Context) { // Activity diubah jadi context (jadi dialog akan di tempatkan pada context, yaitu fragment yang terakhir muncul)
        super.onAttach(context)

        val fragment = parentFragment // menggunakan parentFragment karena ini merupakan nested fragment ( sehingga untuk alasan performa disarankan menggunakan ini daripada fragmentmanager )

        if (fragment is DetailCategoryFragment) { // deteksi fragment yang akan ditumpuk oleh dialog, jika fragment adalah detail category fragment (dialog akan muncul di atas fragment ini )
            val detailCategoryFragment = fragment as DetailCategoryFragment?
            this.optionDialogListener = detailCategoryFragment?.optionDialogListener

        }
    }

    override fun onDetach() { // hilangkan dialog
        super.onDetach()
        this.optionDialogListener = null
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_close -> {
                dialog?.cancel()
            }

            R.id.btn_choose -> {
                val checkedRadioButton = rg_options.checkedRadioButtonId

                if (checkedRadioButton != -1){
                    var coach: String? = null

                    when (checkedRadioButton) {
                        R.id.rb_saf -> coach = rbSaf.text.toString().trim()

                        R.id.rb_mou -> coach = rbMou.text.toString().trim()

                        R.id.rb_lvg -> coach = rbLvg.text.toString().trim()

                        R.id.rb_moyes -> coach = rbLvg.text.toString().trim()
                    }

                    if (optionDialogListener!= null) { // jika terdapat dialog, maka akan menjalankan fungsi onOptionChosen yang ada di interface dialog
                        optionDialogListener?.onOptionChosen(coach)
                    }
                    dialog?.dismiss() // hilangkan dialog dari view
                }
            }
        }
    }

}
