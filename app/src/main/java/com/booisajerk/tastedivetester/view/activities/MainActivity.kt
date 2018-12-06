package com.booisajerk.tastedivetester.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.booisajerk.tastedivetester.R
import com.booisajerk.tastedivetester.shared.Constants
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
        private const val TAG = "MainActivity"
    }
}
