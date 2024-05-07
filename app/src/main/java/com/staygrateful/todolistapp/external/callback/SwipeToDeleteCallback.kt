package com.staygrateful.todolistapp.external.callback

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.staygrateful.todolistapp.R
import com.staygrateful.todolistapp.external.extension.toPx
import kotlin.math.absoluteValue

/**
 * Callback class to handle swipe-to-delete functionality in a RecyclerView.
 * This class provides the visual feedback for swiping and triggers the deletion action when swiped.
 *
 * @property context The context used to access resources, such as drawables.
 * @property listener The callback function to be invoked when an item is swiped and deleted.
 */
class SwipeToDeleteCallback(private val context: Context, private val listener: (Int) -> Unit) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    // Drawable icon for deletion
    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_delete)?.apply {
        DrawableCompat.setTint(this, Color.WHITE) // Set icon color to white
    }

    // Drawable background color for swiped item
    private val background: ColorDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.secondary))

    // Flag to indicate whether the initialization has been completed
    private var initiated: Boolean = false

    /**
     * Called when the user drags an item in the RecyclerView.
     * We do not handle drag events, so this method always returns false.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    /**
     * Called when the user swipes an item in the RecyclerView.
     * This method triggers the deletion action by invoking the listener callback.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        listener(position)
    }

    /**
     * Called when the RecyclerView item is being drawn.
     * This method handles the visual feedback for swiping, including drawing the delete icon and background color.
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Only perform initialization once
        if (!initiated) {
            // Hide the delete icon during initialization
            deleteIcon?.setBounds(0, 0, 0, 0)
            initiated = true
        }

        val itemView = viewHolder.itemView
        val itemViewHeight = itemView.height
        val backgroundCornerOffset = 0 // Adjust corner offset

        val iconMargin = (itemViewHeight - deleteIcon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemViewHeight - deleteIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + deleteIcon.intrinsicHeight

        // Draw delete icon and background color based on swipe direction
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + deleteIcon.intrinsicWidth
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        } else { // View is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        // Draw the background color
        background.draw(c)

        // Draw the delete icon only if swipe is currently active
        if (isCurrentlyActive) {
            // Calculate scale factor based on swipe distance
            val scaleFactor = calculateScaleFactor(
                (itemViewHeight.toFloat() / 2f),
                (itemViewHeight.toFloat() + 20f.toPx()),
                dX.absoluteValue
            )

            // Set scale for the delete icon
            c.save()
            c.scale(
                scaleFactor,
                scaleFactor,
                deleteIcon.bounds.centerX().toFloat(),
                deleteIcon.bounds.centerY().toFloat()
            )
            deleteIcon.draw(c)
            c.restore()
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Calculates the scale factor for the swipe animation based on the swipe distance.
     * @param swipeThresholdStart The starting threshold position in pixels.
     * @param swipeThresholdEnd The ending threshold position in pixels.
     * @param dX The horizontal swipe distance.
     * @return The calculated scale factor for the swipe animation, clamped between 0 and 1.
     */
    private fun calculateScaleFactor(
        swipeThresholdStart: Float,
        swipeThresholdEnd: Float,
        dX: Float
    ): Float {
        // Clamp dX within swipeThresholdStart and swipeThresholdEnd
        val clampedDX = dX.coerceIn(-swipeThresholdEnd, swipeThresholdEnd)

        // Calculate scale factor linearly based on clampedDX
        val scaleFactor =
            (clampedDX - swipeThresholdStart) / (swipeThresholdEnd - swipeThresholdStart)

        return scaleFactor.coerceIn(0f, 1f) // Clamp scaleFactor between 0 and 1
    }
}
