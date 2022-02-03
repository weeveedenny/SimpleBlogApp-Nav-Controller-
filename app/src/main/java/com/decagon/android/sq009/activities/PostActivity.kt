package com.decagon.android.sq009.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.decagon.android.sq009.R
import com.decagon.android.sq009.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPostBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_post);
        binding.heading = "General Post"

    }
}