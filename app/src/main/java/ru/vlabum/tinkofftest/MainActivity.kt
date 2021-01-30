package ru.vlabum.tinkofftest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import ru.vlabum.tinkofftest.viewmodels.Category
import ru.vlabum.tinkofftest.viewmodels.Loading
import ru.vlabum.tinkofftest.viewmodels.MainState
import ru.vlabum.tinkofftest.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val colorOn = Color.argb(255, 50, 200, 50)
    private val colorOff = Color.argb(255, 100, 100, 100)

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageButtonNext.setOnClickListener {
            viewModel.getNext()
        }

        imageButtonBack.setOnClickListener {
            viewModel.getPrev()
        }

        imageButtonNext.setColorFilter(colorOn)
        imageButtonBack.setColorFilter(colorOff)

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

        Glide.with(this)
            .asGif()
            .load(state.currentGifUrl)
            .placeholder(R.drawable.ic_baseline_refresh_24)
            .error(R.drawable.ic_baseline_error_outline_24)
            .fitCenter()
            .into(ivImg)

        tvText.text = state.currentDescription

        imageButtonBack.isEnabled = !state.isDisableBack
       if (state.isDisableBack)
            imageButtonBack.setColorFilter(colorOff)
        else
            imageButtonBack.setColorFilter(colorOn)

        if (state.showError) {
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            ivImg.visibility = View.GONE
            tvText.visibility = View.GONE
        } else {
            ivError.visibility = View.GONE
            tvError.visibility = View.GONE
            ivImg.visibility = View.VISIBLE
            tvText.visibility = View.VISIBLE
        }

    }

    fun renderLoading(loadingState: Loading) {
        when (loadingState) {
            Loading.SHOW_LOADING -> progress.isVisible = true
            Loading.HIDE_LOADING -> progress.isVisible = false
        }
    }


}