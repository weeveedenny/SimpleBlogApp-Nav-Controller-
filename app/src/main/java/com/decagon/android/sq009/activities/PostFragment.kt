package com.decagon.android.sq009.activities

import android.app.ProgressDialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq009.R
import com.decagon.android.sq009.adapters.PostAdapter
import com.decagon.android.sq009.model.PostModel
import com.decagon.android.sq009.repository.Repository
import com.decagon.android.sq009.viewmodels.PostViewModel
import com.decagon.android.sq009.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_post.*
import java.util.*
import kotlin.collections.ArrayList

class PostFragment : Fragment(), PostAdapter.OnItemClickListener {
    lateinit var progressDialog : ProgressDialog
    lateinit var viewModel: PostViewModel
    lateinit var postRecyclerView: RecyclerView
    lateinit var recyclerViewAdapter: PostAdapter
    lateinit var viewModelFactory: ViewModelFactory
    private val repository = Repository() // one
    private var newPost: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelFactory = ViewModelFactory(repository) // three

        //initialise the viewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java) // four
        postRecyclerView = view.findViewById(R.id.postActivity_recyclerView)
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postRecyclerView.isNestedScrollingEnabled = false
        recyclerViewAdapter = PostAdapter( this)
        postRecyclerView.adapter = recyclerViewAdapter

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please hold ..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        observeViewModel()
        receiveIntent()
        listenToSearch()
        fab()
    }



    //open another activity for a new post
    private fun fab(){
        postActivity_FAB.setOnClickListener{
            findNavController().navigate(R.id.makeNewPostFragment)

        }
    }



    //observe the list of post from the view model and attach to adapter
    private fun observeViewModel(){
        viewModel.post.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerViewAdapter.submitList(it)
                recyclerViewAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            } else {
                progressDialog.dismiss()
            }
        }
    }



    //receive the intent passing the new post from the AddPostActivity
    private fun receiveIntent(){
        newPost = arguments?.getString("newPost")

        if (newPost != null){
            val postObject = newPost?.let { PostModel(1,0,"Recent Post", it) }

            if (postObject != null) {
                viewModel.addPost(postObject)
            }

            if (newPost!!.isNotEmpty() ) {
                //observe the new postList and update changes to the adapter
                viewModel.newPost.observe(viewLifecycleOwner) {
                    if (it != null) {
                        //Toast.makeText(requireContext(), "Viewmodel Response ==>>>$it", Toast.LENGTH_SHORT).show()
                        recyclerViewAdapter.addNewPost(it)
                        postRecyclerView.adapter = recyclerViewAdapter
                        recyclerViewAdapter.notifyDataSetChanged()


                    }
                }
            }
        }
    }




    //Listen to SearchView for input values
    private fun listenToSearch(){
        postActivity_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (postActivity_searchView.isEmpty()){
                    postRecyclerView.adapter = recyclerViewAdapter
                }
                else{
                    filter(newText)
                    postActivity_searchView.setIconifiedByDefault(true)
                }
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        } )
    }



    // Search post list by title and attach the filtered list to the adapter
    fun filter (text: String){

        val filteredPost = ArrayList<PostModel>()
        viewModel.post.observe(viewLifecycleOwner) { post ->
            post.filterTo(filteredPost) { postModel ->
                postModel.title.lowercase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
            }
            recyclerViewAdapter.submitList(filteredPost)
        }
    }


    //On click of a post, pass the content on the post to the comments activity
    override fun onItemClick(post: PostModel, view: View) {
        val bundle = Bundle()
        bundle.putString("title", post.title)
        bundle.putString("body", post.body)
        bundle.putString("postId", post.id.toString())

        findNavController().navigate(R.id.commentsFragment, bundle)


    }

}