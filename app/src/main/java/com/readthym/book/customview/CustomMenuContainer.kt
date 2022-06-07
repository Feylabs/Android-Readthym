package com.readthym.book.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.readthym.book.R
import com.readthym.book.databinding.CustomMenuDrawerItemBinding

class CustomMenuContainer : FrameLayout {
    private var text: String = ""
    private var binding: CustomMenuDrawerItemBinding =
        CustomMenuDrawerItemBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context) : super(context) {
        initView(context)
    }

    init { // inflate binding and add as view
        addView(binding.root)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        extractAttributes(attributeSet)
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        extractAttributes(attributeSet)
        initView(context)
    }

    private fun initView(context: Context?) {
        text(text)
    }

    fun text(text: String) {
        this.text = text
        binding.tvText.text = text
    }


    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RazAlertContainer)
        typedArray.apply {
            text = getString(R.styleable.RazAlertContainer_alertTitle) ?: text
        }

        typedArray.recycle()
    }
}