package ru.vlabum.tinkofftest

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
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

    fun subscribeOnState(state: MainState) {
        Glide.with(this)
            .asGif()
            .load(state.currentGifUrl)
            .into(ivImg)
        tvText.text = state.currentDescription
        imageButtonBack.isEnabled = !state.isDisableBack
        if (state.isDisableBack)
            imageButtonBack.setColorFilter(colorOff)
        else
            imageButtonBack.setColorFilter(colorOn)

    }

    fun renderLoading(loadingState: Loading) {
        when (loadingState) {
            Loading.SHOW_LOADING -> progress.isVisible = true
            Loading.HIDE_LOADING -> progress.isVisible = false
        }
    }


}