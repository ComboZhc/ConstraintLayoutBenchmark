package com.chaozhang.layouts

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class BenchmarkConstraintLayout : ConstraintLayout, BenchmarkableView {

    private var onMeasureStart: Long = 0
    override fun getOnMeasureStart() = onMeasureStart

    private var onMeasureEnd: Long = 0
    override fun getOnMeasureEnd() = onMeasureEnd

    private var onLayoutStart: Long = 0
    override fun getOnLayoutStart() = onLayoutStart

    private var onLayoutEnd: Long = 0
    override fun getOnLayoutEnd() = onLayoutEnd

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        onMeasureStart = System.nanoTime()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onMeasureEnd = System.nanoTime()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        onLayoutStart = System.nanoTime()
        super.onLayout(changed, left, top, right, bottom)
        onLayoutEnd = System.nanoTime()
    }
}
