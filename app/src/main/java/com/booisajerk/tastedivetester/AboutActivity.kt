package com.booisajerk.tastedivetester

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        showTastediveButton()
    }

    private fun showTastediveButton() {
        val tasteDiveButton: Button = findViewById(R.id.openTasteDiveAbout)
        tasteDiveButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.BASE_URL)
            startActivity(intent)
        }
    }
}
