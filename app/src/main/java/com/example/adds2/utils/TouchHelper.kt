package com.example.adds2.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class TouchHelper() {

    companion object {
        var onMoveLambda: ((startPosition: Int, targetPosition: Int) -> Unit)? = null
    }

    private val callback = object : Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlag = UP or DOWN
            return makeMovementFlags(dragFlag, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            onMoveLambda?.invoke(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            when (actionState) {
                ACTION_STATE_DRAG -> viewHolder?.itemView?.alpha = 0.5f
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.alpha = 1.0f
        }

    }

    fun getItemTouchHelper() = ItemTouchHelper(callback)

}