package com.nekkiichi.storyapp.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.nekkiichi.storyapp.R

class EditTextPassword: AppCompatEditText{
    var isInputValid = true
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    init {
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod()
        addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isInputValid = s.toString().length >= 8
                doValidate()
            }

            override fun afterTextChanged(s: Editable?) {
                //do nothing
            }

        })
    }

    private fun doValidate() {
        if(isInputValid) {
            error = null
        }else {
            error = resources.getString(R.string.password_below_8_chars)
        }
    }
}