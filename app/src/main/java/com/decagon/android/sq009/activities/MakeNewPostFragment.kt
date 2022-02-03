package com.decagon.android.sq009.activities


import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.decagon.android.sq009.R
import kotlinx.android.synthetic.main.fragment_make_new_post.*


class MakeNewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //sends the new post to the main Activity
        post_button.setOnClickListener{
            val newPost = view.findViewById<EditText>(R.id.make_newpost_editText).text.toString()
            val bundle = Bundle()
            bundle.putString("newPost", newPost)
            findNavController().navigate(R.id.postFragment, bundle)


        }
    }

}