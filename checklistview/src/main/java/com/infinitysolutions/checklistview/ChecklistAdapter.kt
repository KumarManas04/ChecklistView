package com.infinitysolutions.checklistview

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_item_view.view.*

class ChecklistAdapter(
    private val itemsList: ArrayList<ChecklistItem>,
    private val context: Context,
    private val dragColor: Int,
    private val removeColor: Int,
    private val textColor: Int,
    private val hintTextColor: Int
) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {
    private lateinit var mDragListener: DragListener
    private lateinit var mParent: View
    private var moveCheckedToBottom = true
    private var showUndoOption = true
    private var removedItem: ChecklistItem? = null
    private var removedItemPos: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dragMarker.setColorFilter(dragColor)
        holder.itemRemove.setColorFilter(removeColor)
        holder.itemContent.setTextColor(textColor)
        holder.itemContent.setHintTextColor(hintTextColor)
        holder.itemContent.setText(itemsList[position].text)

        if (holder.adapterPosition < itemsList.size - 1) {
            holder.itemCheckBox.isEnabled = true
            holder.itemCheckBox.setOnCheckedChangeListener { button, isChecked ->
                itemsList[holder.adapterPosition].isChecked = isChecked
                if (holder.itemContent.text.isNotEmpty()) {
                    val content = holder.itemContent.text.toString()
                    val spannableString = SpannableString(content)
                    if (isChecked) {
                        spannableString.setSpan(StrikethroughSpan(), 0, content.length, 0)
                        holder.itemContent.alpha = 0.5f

                        if (button.isPressed && moveCheckedToBottom) {
                            val from = holder.adapterPosition
                            val to = itemsList.size - 2
                            val item = itemsList[from]
                            itemsList.removeAt(from)
                            itemsList.add(to, item)
                            notifyItemMoved(from, to)
                        }
                    } else {
                        spannableString.setSpan(StrikethroughSpan(), 0, 0, 0)
                        holder.itemContent.alpha = 1f
                    }
                    holder.itemContent.setText(spannableString)
                }
            }
        } else {
            holder.itemCheckBox.isEnabled = false
        }
        holder.itemCheckBox.isChecked = itemsList[position].isChecked

        holder.dragMarker.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN && holder.adapterPosition != itemsList.size - 1) {
                mDragListener.startDrag(holder)
            }
            return@setOnTouchListener true
        }

        holder.itemContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && holder.adapterPosition != itemsList.size - 1) {
                holder.itemRemove.visibility = View.VISIBLE
            } else {
                holder.itemRemove.visibility = View.GONE
            }
        }

        holder.itemContent.addTextChangedListener {
            itemsList[holder.adapterPosition].text = it.toString()
            if (it.toString() != "" && holder.adapterPosition == itemsList.size - 1) {
                itemsList.add(ChecklistItem("", false))
                notifyItemInserted(itemsList.size)
                notifyItemChanged(
                    holder.adapterPosition,
                    holder.itemContent.text.toString()
                )
            }
        }

        if (holder.adapterPosition != itemsList.size - 1) {
            holder.itemRemove.setOnClickListener {
                val pos = holder.adapterPosition
                if (showUndoOption) {
                    removedItem = itemsList[pos]
                    removedItemPos = pos
                }
                itemsList.removeAt(pos)
                notifyItemRemoved(pos)
                if (showUndoOption) {
                    Snackbar.make(mParent, "Undo item remove", 5000)
                        .setAction("Yes") {
                            itemsList.add(removedItemPos!!, removedItem!!)
                            notifyItemInserted(removedItemPos!!)
                        }
                        .show()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.dragMarker.setColorFilter(dragColor)
            holder.itemRemove.setColorFilter(removeColor)
            holder.itemContent.setTextColor(textColor)
            holder.itemContent.setHintTextColor(hintTextColor)
            val contentText = payloads[0] as String
            holder.itemContent.setText(contentText)

            if (holder.adapterPosition < itemsList.size - 1) {
                holder.itemCheckBox.isEnabled = true
                holder.itemCheckBox.setOnCheckedChangeListener { button, isChecked ->
                    itemsList[holder.adapterPosition].isChecked = isChecked
                    if (holder.itemContent.text.isNotEmpty()) {
                        val content = holder.itemContent.text.toString()
                        val spannableString = SpannableString(content)
                        if (isChecked) {
                            spannableString.setSpan(StrikethroughSpan(), 0, content.length, 0)
                            holder.itemContent.alpha = 0.5f

                            if (button.isPressed && moveCheckedToBottom) {
                                val from = holder.adapterPosition
                                val to = itemsList.size - 2
                                val item = itemsList[from]
                                itemsList.removeAt(from)
                                itemsList.add(to, item)
                                notifyItemMoved(from, to)
                            }
                        } else {
                            spannableString.setSpan(StrikethroughSpan(), 0, 0, 0)
                            holder.itemContent.alpha = 1f
                        }
                        holder.itemContent.setText(spannableString)
                    }
                }
            } else {
                holder.itemCheckBox.isEnabled = false
            }
            holder.itemCheckBox.isChecked = itemsList[position].isChecked

            holder.dragMarker.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN && holder.adapterPosition != itemsList.size - 1) {
                    mDragListener.startDrag(holder)
                }
                return@setOnTouchListener true
            }

            holder.itemContent.addTextChangedListener {
                itemsList[holder.adapterPosition].text = it.toString()
                if (it.toString() != "" && holder.adapterPosition == itemsList.size - 1) {
                    itemsList.add(ChecklistItem("", false))
                    notifyItemInserted(itemsList.size)
                    notifyItemChanged(
                        holder.adapterPosition,
                        holder.itemContent.text.toString()
                    )
                }
            }

            if (holder.adapterPosition != itemsList.size - 1) {
                holder.itemRemove.setOnClickListener {
                    val pos = holder.adapterPosition
                    if (showUndoOption) {
                        removedItem = itemsList[pos]
                        removedItemPos = pos
                    }
                    itemsList.removeAt(pos)
                    notifyItemRemoved(pos)
                    if (showUndoOption) {
                        Snackbar.make(mParent, "Undo item remove", 5000)
                            .setAction("Yes") {
                                itemsList.add(removedItemPos!!, removedItem!!)
                                notifyItemInserted(removedItemPos!!)
                            }
                            .show()
                    }
                }
            }

            holder.itemRemove.visibility = View.VISIBLE
            holder.itemContent.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && holder.adapterPosition != itemsList.size - 1) {
                    holder.itemRemove.visibility = View.VISIBLE
                } else {
                    holder.itemRemove.visibility = View.GONE
                }
            }
            holder.itemContent.setSelection(contentText.length)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dragMarker: ImageView = itemView.drag_marker
        val itemCheckBox: CheckBox = itemView.item_check_box
        val itemContent: EditText = itemView.item_content
        val itemRemove: ImageView = itemView.item_remove
    }

    fun moveItem(to: Int, from: Int) {
        var to1 = to
        if (from == itemsList.size - 1)
            to1++
        val fromItem = itemsList[from]
        itemsList.removeAt(from)
        itemsList.add(to1, fromItem)
        notifyItemMoved(from, to1)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mParent = recyclerView
    }

    fun setDragListener(dragListener: DragListener) {
        mDragListener = dragListener
    }

    fun setMoveCheckedToBottom(shouldMove: Boolean) {
        moveCheckedToBottom = shouldMove
    }

    fun setShowUndoOption(shouldShow: Boolean) {
        showUndoOption = shouldShow
    }

    fun getList(): ArrayList<ChecklistItem> {
        return itemsList
    }

    interface DragListener {
        fun startDrag(viewHolder: ViewHolder)
    }
}