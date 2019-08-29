package com.infinitysolutions.checklistview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
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
        holder.itemCheckBox.isChecked = itemsList[position].isChecked
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

                        if (button.isPressed) {
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

        if (holder.adapterPosition == itemsList.size - 1) {
            holder.itemContent.addTextChangedListener {
                if (it.toString() != "") {
                    if (holder.adapterPosition == itemsList.size - 1) {
                        itemsList.add(ChecklistItem("", false))
                        notifyItemInserted(itemsList.size)
                        notifyItemChanged(
                            holder.adapterPosition,
                            holder.itemContent.text.toString()
                        )
                    }
                }
            }
        } else {
            holder.itemRemove.setOnClickListener {
                val pos = holder.adapterPosition
                itemsList.removeAt(pos)
                notifyItemRemoved(pos)
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

                            if (button.isPressed) {
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
            holder.itemRemove.setOnClickListener {
                val pos = holder.adapterPosition
                itemsList.removeAt(pos)
                notifyItemRemoved(pos)
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
        if (to1 < from)
            itemsList.add(to1, fromItem)
        else
            itemsList.add(to1 - 1, fromItem)
        notifyItemMoved(from, to1)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun setDragListener(dragListener: DragListener) {
        mDragListener = dragListener
    }

    fun getList(): ArrayList<ChecklistItem> {
        return itemsList
    }

    interface DragListener {
        fun startDrag(viewHolder: ViewHolder)
    }
}