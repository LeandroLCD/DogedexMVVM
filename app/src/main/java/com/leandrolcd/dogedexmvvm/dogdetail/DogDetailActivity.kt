package com.leandrolcd.dogedexmvvm.dogdetail

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.leandrolcd.dogedexmvvm.Dog
import com.leandrolcd.dogedexmvvm.R
import com.leandrolcd.dogedexmvvm.databinding.ActivityDogDetailBinding
import com.leandrolcd.dogedexmvvm.dogslist.UiStatus

class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }
    private val viewModel:DogDetailViewModel by viewModels()
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(DOG_KEY, Dog::class.java)

        } else {
            intent.getParcelableExtra<Dog>(DOG_KEY)
        }

        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY,false) ?: false

        if (dog == null) {
            Toast.makeText(this, getString(R.string.dog_not_found), Toast.LENGTH_LONG)
            finish()
            return
        }
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_format, dog.lifeExpectancy)
        binding.dogImage.load(dog.imageUrl)
        binding.dog = dog
        binding.closeButton.setOnClickListener {
            if(isRecognition){
                viewModel.addDogToUser(dogId = dog.id)
            }
            finish()
        }

        viewModel.status.observe(this) { status ->
            when (status) {
                is UiStatus.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
                }
                is UiStatus.Loading -> binding.pbLoading.visibility = View.VISIBLE
                is UiStatus.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    Toast.makeText(this, "!Felicidades has conseguido agregar un ${dog.name}, a tu colección!!!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        }
    }
}