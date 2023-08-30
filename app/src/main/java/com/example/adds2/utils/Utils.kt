package com.example.adds2.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

const val DEFAULT_TAG = "global"

fun createLog(msg: String) {
    Log.d(DEFAULT_TAG, msg)
}

fun createLog(tag: String, msg: String) {
    Log.d(tag, msg)
}

fun makeToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

var needCloseTheDialog = false