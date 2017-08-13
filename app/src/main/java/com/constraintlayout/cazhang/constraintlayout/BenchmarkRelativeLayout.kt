package com.constraintlayout.cazhang.constraintlayout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout

class BenchmarkRelativeLayout : RelativeLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        BenchmarkActivity.onMeasureStart = System.nanoTime()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        BenchmarkActivity.onMeasureEnd = System.nanoTime()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        BenchmarkActivity.onLayoutStart = System.nanoTime()
        super.onLayout(changed, left, top, right, bottom)
        BenchmarkActivity.onLayoutEnd = System.nanoTime()
    }

    override fun onDraw(canvas: Canvas) {
        BenchmarkActivity.onDrawStart = System.nanoTime()
        super.onDraw(canvas)
        BenchmarkActivity.onDrawEnd = System.nanoTime()
    }
}
