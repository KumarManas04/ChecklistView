package com.infinitysolutions.checklistview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChecklistView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs), ChecklistAdapter.DragListener{

    private val dragColor: Int
    private val removeColor: Int
    private val textColor: Int
    private val hintTextColor: Int
    init {
        val tA = context.obtainStyledAttributes(attrs, R.styleable.ChecklistView, 0, 0)
        dragColor = tA.getColor(R.styleable.ChecklistView_dragHandleColor, Color.BLACK)
        removeColor = tA.getColor(R.styleable.ChecklistView_removeButtonColor, Color.BLACK)
        textColor = tA.getColor(R.styleable.ChecklistView_textColor, Color.BLACK)
        hintTextColor = tA.getColor(R.styleable.ChecklistView_hintTextColor, Color.GRAY)
        tA.recycle()
    }

    private lateinit var mAdapter: ChecklistAdapter
    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                val adapter = recyclerView.adapter as ChecklistAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItem(from, to)
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

            override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    fun setList(content: String){
        val itemsList = ArrayList<ChecklistItem>()
        val items = content.split("\n")
        for(item in items){
            val arr = item.split(" ")
            if(arr[0] == "✓")
                itemsList.add(ChecklistItem(arr[1], true))
            else
                itemsList.add(ChecklistItem(arr[1], false))
        }
        setList(itemsList)
    }

    fun setList(itemsList: ArrayList<ChecklistItem>){
        itemsList.add(ChecklistItem("", false))
        this.layoutManager = LinearLayoutManager(context)
        mAdapter = ChecklistAdapter(itemsList, context, dragColor, removeColor, textColor, hintTextColor)
        mAdapter.setDragListener(this)
        this.adapter = mAdapter
        itemTouchHelper.attachToRecyclerView(this)
    }

    override fun toString(): String{
        val itemsList = mAdapter.getList()
        itemsList.removeAt(itemsList.size-1)
        val strBuilder = StringBuilder()
        for(i in 0 until itemsList.size){
            if(itemsList[i].isChecked)
                strBuilder.append("✓ ")
            else
                strBuilder.append("□ ")
            strBuilder.append(itemsList[i].text)
            if(i < itemsList.size-1)
                strBuilder.append("\n")
        }
        return strBuilder.toString()
    }

    override fun startDrag(viewHolder: ChecklistAdapter.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}