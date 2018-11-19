package com.booisajerk.tastedivetester

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton.setOnClickListener {
            if (searchTermField.text.isEmpty()) {
                searchTermField.error = getString(R.string.blank_error_message)
            } else {
                val intent = Intent(this@MainActivity, SearchResultActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY, searchTermField.text.toString())
                startActivity(intent)
            }
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
