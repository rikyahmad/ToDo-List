package com.staygrateful.todolistapp.external.extension

import android.content.res.Resources
import android.util.TypedValue

/**
 * Extension function to convert an integer value representing pixels to density-independent pixels (DP).
 * @return The converted value in DP.
 */
fun Int.toDp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

/**
 * Extension function to convert a float value representing density-independent pixels (DP) to pixels.
 * @return The converted value in pixels.
 */
fun Float.toPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}
