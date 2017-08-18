package com.kokutouda.dnote.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kokutouda.dnote.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        val TITLE = "title"
        val CONTENT = "content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(TITLE, editTitle.text.toString()).
                putExtra(CONTENT, editContent.text.toString())
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}
