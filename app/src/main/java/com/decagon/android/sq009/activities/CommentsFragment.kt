package com.decagon.android.sq009.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.adapters.CommentAdapter
import com.decagon.android.sq009.databinding.FragmentCommentsBinding
import com.decagon.android.sq009.model.CommentModel
import com.decagon.android.sq009.repository.Repository
import com.decagon.android.sq009.viewmodels.CommentViewModel
import com.decagon.android.sq009.viewmodels.CommentViewModelFactory
import kotlinx.android.synthetic.main.fragment_comments.*
import java.util.zip.Inflater


class CommentsFragment : Fragment() {

    lateinit var commentViewModel: CommentViewModel
    lateinit var progressDialog: ProgressDialog
    private lateinit var commentRecyclerView: RecyclerView
    lateinit var commentRecyclerViewAdapter: CommentAdapter
    lateinit var commentViewModelFactory: CommentViewModelFactory
    private var commentList = mutableListOf<CommentModel>()
    private var postId: String? = null
    lateinit var binding : FragmentCommentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //initialise the view model repository class
        val repository = Repository()
        commentViewModelFactory = CommentViewModelFactory(repository)
        commentViewModel =
            ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel::class.java)

        //build the progressbar Dialog
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please hold ..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        getViews()
        viewModelToAdapter()
        observeComment()

    }

    // get the views and the input
    private fun getViews() {
        val postTitle = view?.findViewById<TextView>(R.id.comments_cardView_textView)
        val postBody = view?.findViewById<TextView>(R.id.comments_cardView_textViewbody)
        var title = arguments?.getString("title")
        var body = arguments?.getString("body")
        postId = arguments?.getString("postId")

        postTitle?.text = title
        postBody?.text = body

        commentViewModel.getComment(postId?.toInt()!!)
        commentRecyclerView = requireView().findViewById(R.id.comment_recyclerView)

    }


    //observe the list of comments from the view model. if its not empty, attach the list to populate the adapter.
    private fun viewModelToAdapter() {
        //val posId = intent.getIntExtra("position", 0)
        commentViewModel.comments.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                commentRecyclerView.setHasFixedSize(true)
                commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                commentRecyclerViewAdapter = CommentAdapter(it)
                commentRecyclerViewAdapter.addAllComment(getCommentsList(it, postId?.toInt()!!))
                commentRecyclerView.adapter = commentRecyclerViewAdapter
                progressDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please check connection", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    //onclick of the comment button, observe the new comment input
    private fun observeComment() {
        // val posId = intent.getIntExtra("position", 0)
        comment_button.setOnClickListener {
            val newComment = view?.findViewById<EditText>(R.id.comment_editText)?.text.toString()
            val comment_model =
                CommentModel(0, 0, "Weeveedenny", "dennisodalonu@gmail.com", newComment)

            if (newComment.isNotEmpty()) {
                postId?.let { it1 -> commentViewModel.postComment(it1.toInt(), comment_model) }

                commentViewModel.commentNew.observe(viewLifecycleOwner, Observer {
                    Log.d("NewComment", "onCreate: $it")
                    if (it != null) {
                        commentList.add(it)
                        //val posId = intent.getIntExtra("position", 0)

                        commentRecyclerView.setHasFixedSize(true)
                        commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        commentRecyclerViewAdapter.addNewComment(it)
                        commentRecyclerView.adapter = commentRecyclerViewAdapter
                        comment_editText.text.clear()
                    }
                })

            } else {
                Toast.makeText(requireContext(), "Please add a new comment", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    //get the list of comments and filter it by post id to display comment related to the post
    private fun getCommentsList(comments: List<CommentModel>, postId: Int): List<CommentModel> {
        return comments.filter { it.postId == postId }
    }

}