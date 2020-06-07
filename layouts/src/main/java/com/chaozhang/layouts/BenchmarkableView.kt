package com.chaozhang.layouts;

interface BenchmarkableView {

    fun getOnMeasureStart(): Long

    fun getOnMeasureEnd(): Long

    fun getOnLayoutStart(): Long

    fun getOnLayoutEnd(): Long
}
