package com.app.purchase.Views.stats

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

abstract class ValueFormatter : IAxisValueFormatter, IValueFormatter {

    /**
     * **DO NOT USE**, only for backwards compatibility and will be removed in future versions.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return formatted string label
     */
    @Deprecated("", ReplaceWith("getFormattedValue(value)"))
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return getFormattedValue(value)
    }

    /**
     * **DO NOT USE**, only for backwards compatibility and will be removed in future versions.
     * @param value           the value to be formatted
     * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
     * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return formatted string label
     */
    @Deprecated("")
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        return getFormattedValue(value)
    }

    /**
     * Called when drawing any label, used to change numbers into formatted strings.
     *
     * @param value float to be formatted
     * @return formatted string label
     */
    open fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    /**
     * Used to draw axis labels, calls [.getFormattedValue] by default.
     *
     * @param value float to be formatted
     * @param axis  axis being labeled
     * @return formatted string label
     */
    fun getAxisLabel(value: Float, axis: AxisBase): String {
        return getFormattedValue(value)
    }

    /**
     * Used to draw bar labels, calls [.getFormattedValue] by default.
     *
     * @param barEntry bar being labeled
     * @return formatted string label
     */
    fun getBarLabel(barEntry: BarEntry): String {
        return getFormattedValue(barEntry.y)
    }

    /**
     * Used to draw stacked bar labels, calls [.getFormattedValue] by default.
     *
     * @param value        current value to be formatted
     * @param stackedEntry stacked entry being labeled, contains all Y values
     * @return formatted string label
     */
    fun getBarStackedLabel(value: Float, stackedEntry: BarEntry): String {
        return getFormattedValue(value)
    }

    /**
     * Used to draw line and scatter labels, calls [.getFormattedValue] by default.
     *
     * @param entry point being labeled, contains X value
     * @return formatted string label
     */
    fun getPointLabel(entry: Entry): String {
        return getFormattedValue(entry.y)
    }

    /**
     * Used to draw pie value labels, calls [.getFormattedValue] by default.
     *
     * @param value    float to be formatted, may have been converted to percentage
     * @param pieEntry slice being labeled, contains original, non-percentage Y value
     * @return formatted string label
     */
    fun getPieLabel(value: Float, pieEntry: PieEntry): String {
        return getFormattedValue(value)
    }

    /**
     * Used to draw radar value labels, calls [.getFormattedValue] by default.
     *
     * @param radarEntry entry being labeled
     * @return formatted string label
     */
    fun getRadarLabel(radarEntry: RadarEntry): String {
        return getFormattedValue(radarEntry.y)
    }

    /**
     * Used to draw bubble size labels, calls [.getFormattedValue] by default.
     *
     * @param bubbleEntry bubble being labeled, also contains X and Y values
     * @return formatted string label
     */
    fun getBubbleLabel(bubbleEntry: BubbleEntry): String {
        return getFormattedValue(bubbleEntry.size)
    }

    /**
     * Used to draw high labels, calls [.getFormattedValue] by default.
     *
     * @param candleEntry candlestick being labeled
     * @return formatted string label
     */
    fun getCandleLabel(candleEntry: CandleEntry): String {
        return getFormattedValue(candleEntry.high)
    }

}