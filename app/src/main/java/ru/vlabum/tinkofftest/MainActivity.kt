package ru.vlabum.tinkofftest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.vlabum.tinkofftest.databinding.ActivityMainBinding
import ru.vlabum.tinkofftest.viewmodels.Category
import ru.vlabum.tinkofftest.viewmodels.Loading
import ru.vlabum.tinkofftest.viewmodels.MainState
import ru.vlabum.tinkofftest.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    private val colorOn = Color.argb(255, 50, 200, 50)
    private val colorOff = Color.argb(255, 180, 180, 180)

    val col = Color.argb(255, 50, 200, 50)

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.imageButtonNext.setOnClickListener {
            viewModel.getNext()
        }

        binding.imageButtonBack.setOnClickListener {
            viewModel.getPrev()
        }

        binding.imageButtonNext.setColorFilter(colorOn)
        binding.imageButtonBack.setColorFilter(colorOff)

        viewModel.observeState(this) { subscribeOnState(it) }
        viewModel.observeLoading(this) { renderLoading(it) }

        viewModel.getCurrent()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rbRandom -> if (checked) { viewModel.setCategory(Category.RANDOM) }
                R.id.rbHot -> if (checked) { viewModel.setCategory(Category.HOT) }
                R.id.rbLatest -> if (checked) { viewModel.setCategory(Category.LATEST) }
                R.id.rbTop -> if (checked) { viewModel.setCategory(Category.TOP) }
            }
        }
    }

    fun subscribeOnState(state: MainState) {

        Log.d("sonstate", state.currentGifUrl)
        Log.d("sonstate", state.showError.toString())

        Glide.with(this)
            .asGif()
            .load(state.currentGifUrl)
            .placeholder(R.drawable.ic_baseline_refresh_24)
            .error(R.drawable.ic_baseline_error_outline_24)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.ivImg)

        binding.tvText.text = state.currentDescription

        binding.imageButtonBack.isEnabled = !state.isDisableBack
        if (state.isDisableBack)
            binding.imageButtonBack.setColorFilter(colorOff)
        else
            binding.imageButtonBack.setColorFilter(colorOn)

        if (state.showError) {
            binding.ivError.visibility = View.VISIBLE
            binding.tvError.visibility = View.VISIBLE
            binding.ivImg.visibility = View.GONE
            binding.tvText.visibility = View.GONE
        } else {
            binding.ivError.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.ivImg.visibility = View.VISIBLE
            binding.tvText.visibility = View.VISIBLE
        }

    }

    fun renderLoading(loadingState: Loading) {
        when (loadingState) {
            Loading.SHOW_LOADING -> binding.progress.isVisible = true
            Loading.HIDE_LOADING -> binding.progress.isVisible = false
        }
    }

}