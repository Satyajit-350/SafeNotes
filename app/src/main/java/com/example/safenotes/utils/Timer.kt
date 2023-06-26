package com.example.safenotes.utils

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import kotlin.math.min

class Timer(listener: OnTimerListener) {

    //here we need a handler and runnable to form the task
    //the handler schedule the update action and the runnable will carry out this action i.e. update the duration and notify the main
    //activity the time in seconds

    private var hanlder = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var duration = 0L
    private var delay = 100L

    init {
        //initialize the runnable
        runnable = Runnable{
            //increment the duration
            duration += delay
            //and rerun itself
            hanlder.postDelayed(runnable,delay)
            listener.onTimerChange(formatTimer())
        }
    }

    public fun start(){
        hanlder.postDelayed(runnable,delay)
    }

    public fun pause(){
        //remove the schedule runnable from handler
        hanlder.removeCallbacks(runnable)
    }

    public fun stop(){
        //remove the schedule runnable from handler same thing as pause
        hanlder.removeCallbacks(runnable)
        duration = 0L
    }

    fun formatTimer(): String{
        //make the milli seconds into human readable\
        val millis = duration % 1000
        val seconds = (duration/1000) % 60
        val minutes = (duration/(1000*60)) % 60
        val hours = (duration/(1000*60*60))

        val formattedString = if(hours>0)
            "%02d:%02d:%02d.%02d".format(hours, minutes,seconds,millis/10)
        else
            "%02d:%02d.%02d".format(minutes,seconds,millis/10)

        return formattedString
    }

    //now i need a way to inform the activity or fragment in every 100 milli second to update the textView
    //for that we will create callback function within the runnable and implement the action in the the activity or fragment
    //to this we will use an interface
    interface OnTimerListener{
        fun onTimerChange(duration: String)
    }
}