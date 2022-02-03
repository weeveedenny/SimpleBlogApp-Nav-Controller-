package com.decagon.android.sq009.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.databinding.CommentItemBinding
import com.decagon.android.sq009.model.CommentModel


class CommentAdapter(private var recyclerViewCommentList: MutableList<CommentModel>):

    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(val view: CommentItemBinding): RecyclerView.ViewHolder(view.root), View.OnClickListener{


        override fun onClick(p0: View?) {
        }

        //Passing data to the variable in xml
        fun bind (commentData: CommentModel){
            view.comment = commentData
            view.executePendingBindings()
        }
    }


    //create the viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    //get the size of items populating the recycler view
    override fun getItemCount(): Int {
        return recyclerViewCommentList.size
    }


    //bind the list with the viewHolders
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recyclerViewCommentList[position])

    }


    //function to add the new comment to the recyclerViewCommentList
    fun addNewComment(comment : CommentModel){
        recyclerViewCommentList.add(0,comment)
        notifyItemChanged(0)
    }


    //add the list of comment to the comment list
    fun addAllComment(comments: List<CommentModel>){
        this.recyclerViewCommentList = comments as MutableList<CommentModel>
        notifyDataSetChanged()
    }

}