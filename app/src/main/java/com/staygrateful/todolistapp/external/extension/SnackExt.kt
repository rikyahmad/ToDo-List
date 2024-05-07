package com.staygrateful.todolistapp.external.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(title: String, actionText: String? = null, onActionClick: () -> Unit = {}) {
    val snackbar = Snackbar.make(this, title, Snackbar.LENGTH_LONG)
    actionText?.let {
        snackbar.setAction(actionText) {
            onActionClick.invoke()
        }
    }
    snackbar.show()
}