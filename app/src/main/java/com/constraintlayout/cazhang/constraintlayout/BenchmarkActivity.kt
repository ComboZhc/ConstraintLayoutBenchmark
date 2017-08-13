package com.constraintlayout.cazhang.constraintlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class BenchmarkActivity : AppCompatActivity() {

    val MS_TO_NS = 1000000.0

    companion object {
        var inflateStart: Long = 0
        var inflateEnd: Long = 0
        var onMeasureStart: Long = 0
        var onMeasureEnd: Long = 0
        var onLayoutStart: Long = 0
        var onLayoutEnd: Long = 0
        var onDrawStart: Long = 0
        var onDrawEnd: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.benchmark_activity)
        val benchmarkLayout: FrameLayout = findViewById(R.id.benchmark_layout)
        val infoTextView: TextView = findViewById(R.id.info)
        findViewById<View>(R.id.button_cl).setOnClickListener {
            inflateStart = System.nanoTime()
            val constraintLayout = layoutInflater.inflate(R.layout.constraint_layout, benchmarkLayout, false) as BenchmarkConstraintLayout
            inflateEnd = System.nanoTime()
            benchmarkLayout.addView(constraintLayout)
            benchmarkLayout.postDelayed({
                updateInfo(infoTextView, "ConstraintLayout")
                benchmarkLayout.removeAllViews()
            }, 1000)
        }
        findViewById<View>(R.id.button_rl).setOnClickListener {
            inflateStart = System.nanoTime()
            val relativeLayout = layoutInflater.inflate(R.layout.relative_layout, benchmarkLayout, false) as BenchmarkRelativeLayout
            inflateEnd = System.nanoTime()
            benchmarkLayout.addView(relativeLayout)
            benchmarkLayout.postDelayed({
                updateInfo(infoTextView, "RelativeLayout")
                benchmarkLayout.removeAllViews()
            }, 1000)
        }
    }

    private fun updateInfo(textView: TextView, title: String) {
        textView.setText(title + "\n"
                + "Layout inflation : " + (inflateEnd - inflateStart) / MS_TO_NS + " ms\n"
                + "onMeasure : " + (onMeasureEnd - onMeasureStart) / MS_TO_NS + " ms\n"
                + "onLayout : " + (onLayoutEnd - onLayoutStart) / MS_TO_NS + " ms\n"
                + "onDraw : " + (onDrawEnd - onDrawStart) / MS_TO_NS + " ms\n"
        )
    }
}
