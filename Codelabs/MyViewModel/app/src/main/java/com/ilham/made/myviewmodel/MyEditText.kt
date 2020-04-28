package com.ilham.made.myviewmodel

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat

class MyEditText : AppCompatEditText {

    internal lateinit var mClearButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super( context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr : Int) : super( context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = "Masukkan Nama Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_close_black_24dp, null) as Drawable

        setOnTouchListener(OnTouchListener { _, motionEvent ->
            if (compoundDrawablesRelative[2] != null) {
                val clearButtonStart: Float
                val clearButtonEnd: Float
                var isClearButtonClicked = false

                when (layoutDirection) {
                    View.LAYOUT_DIRECTION_RTL -> {

                        clearButtonEnd = (mClearButtonImage.intrinsicWidth + paddingStart).toFloat()

                        when {
                            motionEvent.x < clearButtonEnd -> isClearButtonClicked = true
                        }

                    }
                    else -> {

                        clearButtonStart = (width - paddingEnd - mClearButtonImage.intrinsicWidth).toFloat()

                        when {
                            motionEvent.x > clearButtonStart -> isClearButtonClicked = true
                        }
                    }
                }
                when {
                    isClearButtonClicked -> when {
                        motionEvent.action== MotionEvent.ACTION_DOWN-> {
                            mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_close_black_24dp, null) as Drawable
                            showClearButton()
                            return@OnTouchListener true
                        }

                        motionEvent.action == MotionEvent.ACTION_UP -> {
                            mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_close_black_24dp, null) as Drawable
                            when {
                                text != null -> text?.clear()
                            }
                            hideClearButton()
                            return@OnTouchListener true
                        }
                        else-> return@OnTouchListener false
                    }
                    else-> return@OnTouchListener false
                }
            }
            false
        })

        addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do Nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    !s.toString().isEmpty() -> showClearButton()
                }
            }
        })
    }

    private fun showClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, // Top of Text
            mClearButtonImage, null ) // Start of Text
    }

    private fun hideClearButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, null, null) // Start of Text
    }
}