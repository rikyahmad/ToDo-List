package com.staygrateful.todolistapp.external.extension

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(title: String) {
    Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
}

fun View.showSnackbar(title: String, actionText: String? = null, onActionClick: () -> Unit = {}) {
    val snackbar = Snackbar.make(this, title, Snackbar.LENGTH_LONG)
    actionText?.let {
        snackbar.setAction(actionText) {
            onActionClick.invoke()
        }
    }
    snackbar.show()
}