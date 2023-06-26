package com.example.safenotes.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class WaveFormView (context: Context?, attrs: AttributeSet?): View(context, attrs) {

    private var paint = Paint()
    private var amplitude = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private var radius = 6f
    private var width = 6f
    private var screenWidth = 0f
    private var screenHeight = 200f
    private var distance = 5f

    private var maxSpikes = 0

    init {
        paint.color = Color.rgb(244, 81, 30)
        screenWidth = resources.displayMetrics.widthPixels.toFloat()

        maxSpikes = (screenWidth/(width+distance)).toInt()
    }

    fun addAmplitude(amp: Float){
        var norm = Math.min(amp.toInt()/7, 200).toFloat()
        amplitude.add(norm)

        spikes.clear()
        var amps = amplitude.takeLast(maxSpikes)
        for(i in amps.indices){
            val left = screenWidth - i*(width+distance)
            val top = screenHeight/2 - amps[i]/2
            val right = left + width
            val bottom = top + amps[i]

            spikes.add(RectF(left, top, right, bottom))
        }
        invalidate()
    }

    fun clear(): ArrayList<Float>{
        val amps = amplitude.clone() as ArrayList<Float>
        amplitude.clear()
        spikes.clear()
        invalidate()

        return amps
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        spikes.forEach {
            canvas?.drawRoundRect(it, radius, radius, paint)
        }
    }

    //we can see the spikes overlaps with each other whenever it gets a list of amplitudes
    //so that we need spaces between our blocks
    /**
     * the mathematical formula :-
     * totalWidthOfScreen = sw (screen width)
     * width(w) = with of he each block
     * distance(d) = gap/distance between each blocks
     * we can derive a formula : (sw-w-d)->1st block , (sw-2(w+d))->2nd block, (sw-3(w+d))->3rd block ........ (sw-i(w+d))->ith index block
     *
     * so formula is: (sw-i(w+d))
     */


}