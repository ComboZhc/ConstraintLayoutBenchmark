package com.constraintlayout.cazhang.constraintlayout

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.FrameMetrics
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView

class BenchmarkActivity : AppCompatActivity() {

    val MS_TO_NS = 1000000.0
    val frameMetricsListener = Window.OnFrameMetricsAvailableListener {
        _, frameMetrics, _ ->
        layoutMeasureDuration += frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)
    }

    lateinit var containerView: FrameLayout
    lateinit var infoView: TextView

    companion object {
        var inflateStart: Long = 0
        var inflateEnd: Long = 0
        var onMeasureStart: Long = 0
        var onMeasureEnd: Long = 0
        var onLayoutStart: Long = 0
        var onLayoutEnd: Long = 0

        var inflateTotal: Double = 0.0
        var measureTotal: Double = 0.0
        var layoutTotal: Double = 0.0

        var layoutMeasureDuration: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.benchmark_activity)
        containerView = findViewById(R.id.benchmark_layout)
        infoView = findViewById(R.id.info)
        val totalRunTextView : EditText = findViewById<EditText>(R.id.total_run)
        findViewById<View>(R.id.button_cl).setOnClickListener {
            reset()
            benchmark(1, Integer.valueOf(totalRunTextView.text.toString()), R.layout.constraint_layout)
        }
        findViewById<View>(R.id.button_rl).setOnClickListener {
            reset()
            benchmark(1, Integer.valueOf(totalRunTextView.text.toString()), R.layout.relative_layout)
        }
    }

    private fun reset() {
        inflateTotal = 0.0
        measureTotal = 0.0
        layoutTotal = 0.0
        layoutMeasureDuration = 0
        window.addOnFrameMetricsAvailableListener(frameMetricsListener, Handler())
    }

    private fun benchmark(currentRun: Int, totalRun: Int, layoutId: Int) {
        inflateStart = System.nanoTime()
        val layout = layoutInflater.inflate(layoutId, containerView, false)
        inflateEnd = System.nanoTime()
        containerView.addView(layout)
        containerView.postDelayed({
            inflateTotal += inflateEnd - inflateStart
            measureTotal += onMeasureEnd - onMeasureStart
            layoutTotal += onLayoutEnd - onLayoutStart
            if (currentRun < totalRun) {
                containerView.removeAllViews()
                containerView.postDelayed({
                    benchmark(currentRun + 1, totalRun, layoutId)
                }, 250)
            } else {
                containerView.removeAllViews()
                infoView.setText(
                        "Total Run: " + totalRun + "\n"
                        + "inflation avg: " + inflateTotal / totalRun / MS_TO_NS + " ms\n"
                        + "custom onMeasure: " + measureTotal / totalRun / MS_TO_NS + " ms\n"
                        + "custom onLayout: " + layoutTotal / totalRun / MS_TO_NS + " ms\n"
                        + "frameMetrics layoutMeasure: " + layoutMeasureDuration / totalRun / MS_TO_NS + " ms\n"
                )
                window.removeOnFrameMetricsAvailableListener(frameMetricsListener)
            }
        }, 250)
    }
}
