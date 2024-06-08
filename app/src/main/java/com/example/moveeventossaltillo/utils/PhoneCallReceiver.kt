package com.example.moveeventossaltillo.utils

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File

class PhoneCallReceiver : BroadcastReceiver() {

    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var callStartTime: Long = 0
    private var savedNumber: String? = null
    private var isCallHandled = false
    private var onCallEndedListener: ((number: String, duration: Long) -> Unit)? = null
    private val handler = Handler(Looper.getMainLooper())
    private var startRecordingRunnable: Runnable? = null

    override fun onReceive(context: Context, intent: Intent) {
        val stateStr = intent.extras?.getString(TelephonyManager.EXTRA_STATE)
        var state = 0
        when (stateStr) {
            TelephonyManager.EXTRA_STATE_IDLE -> state = TelephonyManager.CALL_STATE_IDLE
            TelephonyManager.EXTRA_STATE_OFFHOOK -> state = TelephonyManager.CALL_STATE_OFFHOOK
            TelephonyManager.EXTRA_STATE_RINGING -> state = TelephonyManager.CALL_STATE_RINGING
        }

        when {
            lastState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_OFFHOOK -> {
                // Llamada entrante contestada
                if (!isCallHandled) {
                    isCallHandled = true
                    onCallStarted(context, savedNumber ?: "", System.currentTimeMillis())
                }
            }
            lastState != TelephonyManager.CALL_STATE_IDLE && state == TelephonyManager.CALL_STATE_IDLE -> {
                // Llamada terminada
                if (isCallHandled) {
                    onCallEnded(context, savedNumber ?: "", callStartTime, System.currentTimeMillis())
                    isCallHandled = false
                }
            }
            lastState == TelephonyManager.CALL_STATE_IDLE && state == TelephonyManager.CALL_STATE_OFFHOOK -> {
                // Llamada saliente iniciada
                if (!isCallHandled) {
                    isCallHandled = true
                    onCallStarted(context, savedNumber ?: "", System.currentTimeMillis())
                }
            }
        }

        lastState = state
    }

    private fun onCallStarted(context: Context, number: String, start: Long) {
        callStartTime = start
        savedNumber = number
      /*  Log.d("llamadaGRabada1", "llamda")
        if (isForegroundServiceRunning(context)) {
            Log.d("llamadaGRabada2", "llamda")
            stopForegroundService(context)

        }

        // Retraso de 2 segundos antes de iniciar la grabación
        startRecordingRunnable = Runnable {
            Log.d("llamadaGRabada3", "llamda")
            if (!isForegroundServiceRunning(context)) {
                val serviceIntent = Intent(context, MyForegroundServiceCall::class.java)
                serviceIntent.action = MyForegroundServiceCall.ACTION_START_FOREGROUND_SERVICE
                ContextCompat.startForegroundService(context, serviceIntent)

                Log.d("llamadaGRabada4", "llamda")

            }
        }
        handler.postDelayed(startRecordingRunnable!!, 2000)*/
    }

   /* private fun stopForegroundService(context: Context) {
        Log.d("llamadaGRabada5", "llamda")
        val intent = Intent(context, MyForegroundServiceCall::class.java).apply {
            action = MyForegroundServiceCall.ACTION_STOP_FOREGROUND_SERVICE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("llamadaGRabada6", "llamda")
            ContextCompat.startForegroundService(context, intent)
        } else {
            Log.d("llamadaGRabada7", "llamda")
            context.startService(intent)
        }
    }*/

    private fun onCallEnded(context: Context, number: String, start: Long, end: Long) {
       // handler.removeCallbacks(startRecordingRunnable!!)
        startRecordingRunnable = null
        Log.d("llamadaGRabada8", "llamda")
       /* if (isForegroundServiceRunning(context)) {
            Log.d("llamadaGRabada9", "llamda")
            stopForegroundService(context)
        }*/

        val callDuration = end - start
        Log.d("PhoneCallReceiver", "Llamada terminada: $number, duración: $callDuration ms")

        onCallEndedListener?.invoke(number, callDuration)
    }

  /*  private fun isForegroundServiceRunning(context: Context): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)

        for (service in runningServices) {
            if (service.service.className == "com.abiel.jasso.services.MyForegroundServiceCall") {
                return true
            }
        }
        return false

    }*/
        fun setCallDurationListener(listener: (number: String, duration: Long) -> Unit) {
        onCallEndedListener = listener
    }
}
