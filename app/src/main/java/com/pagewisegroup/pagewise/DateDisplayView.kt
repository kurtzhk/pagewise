package com.pagewisegroup.pagewise

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import kotlin.text.StringBuilder

class DateDisplayView : AppCompatEditText {
    var picker: DatePickerFragment = DatePickerFragment(this)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun updateText() {
        val builder = StringBuilder()
        builder.append("Date: ")
            .append(picker.month)
            .append("/")
            .append(picker.day)
            .append("/")
            .append(picker.year)
        setText(builder.toString())
    }
}