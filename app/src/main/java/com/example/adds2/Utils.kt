package com.example.adds2

import android.content.Context
import android.widget.Toast


fun makeToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

var needCloseTheDialog = false