package com.constraintlayout.cazhang.constraintlayout

import android.os.Bundle
import android.os.Handler
import android.view.FrameMetrics
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

private const val MS_TO_NS = 1000000.0

class BenchmarkActivity : AppCompatActivity() {

    private val frameMetricsListener = Window.OnFrameMetricsAvailableListener {
        _, frameMetrics, _ ->
        layoutMeasureDuration += frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)
    }

    private lateinit var containerView: FrameLayout
    private lateinit var infoView: TextView

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
        findViewById<View>(R.id.button_cl).setOnClickListener {
            reset()
            showRunPicker { times ->
                benchmark(1, times, R.layout.constraint_layout, "Constraint Layout")
            }

        }
        findViewById<View>(R.id.button_rl).setOnClickListener {
            reset()
            showRunPicker { times ->
                benchmark(1, times, R.layout.relative_layout, "Relative Layout")
            }
        }
    }

    private fun reset() {
        inflateTotal = 0.0
        measureTotal = 0.0
        layoutTotal = 0.0
        layoutMeasureDuration = 0
        window.addOnFrameMetricsAvailableListener(frameMetricsListener, Handler())
    }

    private fun showRunPicker(runWithTimes: (Int) -> Unit) {
        val items = arrayOf("1", "10", "100", "1000")
        val builder = AlertDialog.Builder(this).apply {
            setTitle("How many times do you want to run?")
            setSingleChoiceItems(items, -1) { dialog, position ->
                runWithTimes(items[position].toInt())
                dialog.cancel()
            }
        }
        builder.create().show()
    }

    private fun benchmark(currentRun: Int, totalRun: Int, layoutId: Int, type: String) {
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
                    benchmark(currentRun + 1, totalRun, layoutId, type)
                }, 250)
            } else {
                containerView.removeAllViews()
                infoView.text = """
                    $totalRun runs of $type
                    inflation avg: ${inflateTotal / totalRun / MS_TO_NS} ms
                    custom onMeasure: ${measureTotal / totalRun / MS_TO_NS} ms
                    custom onLayout: ${layoutTotal / totalRun / MS_TO_NS} ms
                    frameMetrics layoutMeasure: ${layoutMeasureDuration / totalRun / MS_TO_NS} ms
                """.trimIndent()
                window.removeOnFrameMetricsAvailableListener(frameMetricsListener)
            }
        }, 250)
    }
}
