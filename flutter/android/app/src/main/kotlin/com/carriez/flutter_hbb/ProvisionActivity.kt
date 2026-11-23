package com.carriez.flutter_hbb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import ffi.FFI

/**
 * Endpoint Manager: lets the phone app (same signing key, see the PROVISION permission) set our
 * server, key and permanent password, get the ID back, and start screen sharing ("start" = true:
 * Android then asks for screen capture once).
 */
class ProvisionActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(KEY_SHARED_PREFERENCES, MODE_PRIVATE)
        var dir = prefs.getString(KEY_APP_DIR_CONFIG_PATH, "") ?: ""
        if (dir.isEmpty()) { // what Flutter's getApplicationDocumentsDirectory() gives the UI
            dir = getDir("flutter", MODE_PRIVATE).path
            prefs.edit().putString(KEY_APP_DIR_CONFIG_PATH, dir).apply()
        }
        fun extra(k: String) = intent.getStringExtra(k) ?: ""
        val r = FFI.provision(dir, extra("host"), extra("relay"), extra("key"), extra("password"))
        Log.d("provision", if (r.startsWith("error")) r else "ok")
        setResult(if (r.startsWith("error")) RESULT_CANCELED else RESULT_OK, Intent().putExtra("result", r))
        if (!r.startsWith("error") && intent.getBooleanExtra("start", false)) {
            startActivity(Intent(this, PermissionRequestTransparentActivity::class.java).apply {
                action = ACT_REQUEST_MEDIA_PROJECTION
            })
        }
        finish()
    }
}
