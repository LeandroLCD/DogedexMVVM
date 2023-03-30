package com.leandrolcd.dogedexmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.leandrolcd.dogedexmvvm.databinding.ActivityWholeImageBinding
import java.io.File

class WholeImageActivity : AppCompatActivity() {
    companion object {
        const val PHOTO_URI = "photo_uri"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWholeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUri = intent.extras?.getString(PHOTO_URI)
        if (photoUri.isNullOrEmpty()) {
            Toast.makeText(this, "Error al capturar la imagen.", Toast.LENGTH_LONG).show()
        }

        binding.imageWhole.load(File(photoUri))
    }
}