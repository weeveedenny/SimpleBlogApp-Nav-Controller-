package com.decagon.android.sq009.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.databinding.PostLayoutBinding
import com.decagon.android.sq009.model.PostModel

class PostAdapter( private val onClickListener: OnItemClickListener):
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var recyclerViewPostList: MutableList<PostModel> = mutableListOf()

    inner class ViewHolder(val view: PostLayoutBinding): RecyclerView.ViewHolder(view.root), View.OnClickListener {


      init {
          view.root.setOnClickListener(this) }

      //function to respond to click on the recycler item
      override fun onClick(v: View?) {
                  val position = adapterPosition
                  if (position != RecyclerView.NO_POSITION){
                  Log.i("Position","$adapterPosition")
                  onClickListener.onItemClick(recyclerViewPostList[adapterPosition], v!!)
              }
          }

        //Passing data to the variable in xml
        fun bind (postData: PostModel){
            view.post = postData
            view.executePendingBindings()
            }

        }

        fun submitList(post: MutableList<PostModel>){
            recyclerViewPostList = post
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)
            return ViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    //Get the size of the list
        override fun getItemCount(): Int {
           return recyclerViewPostList.size
        }

    //Binds the view with the data
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(recyclerViewPostList[position])
        }


    //add the new post to the list of posts
        fun addNewPost(post: PostModel){
            recyclerViewPostList.add(0, post)
            notifyItemChanged(0)
        }

    //interface for the click on recyclerview
        interface OnItemClickListener{
            fun onItemClick(post: PostModel, view: View)
        }
}