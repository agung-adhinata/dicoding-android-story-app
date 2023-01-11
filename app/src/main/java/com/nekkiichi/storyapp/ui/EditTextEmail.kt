package com.nekkiichi.storyapp.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.nekkiichi.storyapp.R

class EditTextEmail: AppCompatEditText {
    var isInputValid = true
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isInputValid = s?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }?: false
                doValidate()
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })
    }
    private fun doValidate() {
        error = if(isInputValid) {
            null
        }else {
            resources.getString(R.string.email_invalid)
        }
    }
}