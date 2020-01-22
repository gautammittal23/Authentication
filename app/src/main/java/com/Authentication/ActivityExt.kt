package com.Authentication

import android.app.Activity
import android.util.Log
import android.widget.Toast

/**
 * @author Gautam Mittal
 * 23/1/20
 */

fun Activity.showToast(message: String) {
    Log.d("ToastMessage->", message)
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}