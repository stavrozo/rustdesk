package com.carriez.flutter_hbb

import android.app.Activity
import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle

/** Endpoint Manager: shows over a swipe lock screen and dismisses it (no PIN, so Android allows it). */
class UnlockActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 27) { finish(); return } // these calls need Android 8.1+
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        (getSystemService(KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(this,
            object : KeyguardManager.KeyguardDismissCallback() {
                override fun onDismissSucceeded() = finish()
                override fun onDismissError() = finish()
                override fun onDismissCancelled() = finish()
            })
    }
}
