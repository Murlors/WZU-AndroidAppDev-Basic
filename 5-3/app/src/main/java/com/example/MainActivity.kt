package com.example.hlt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CounterViewModel : ViewModel() {
    private var count = 0f
    private var isRunning = false
    private var isPaused = false

    private val _countLiveData = MutableLiveData<Float>()
    val countLiveData: LiveData<Float>
        get() = _countLiveData

    private val _isRunningLiveData = MutableLiveData<Boolean>()
    val isRunningLiveData: LiveData<Boolean>
        get() = _isRunningLiveData

    private val _isPausedLiveData = MutableLiveData<Boolean>()
    val isPausedLiveData: LiveData<Boolean>
        get() = _isPausedLiveData

    init {
        _countLiveData.value = count
        _isRunningLiveData.value = isRunning
        _isPausedLiveData.value = isPaused
    }

    fun startOrPause() {
        if (isRunning) {
            isPaused = !isPaused
            _isPausedLiveData.postValue(isPaused)
        } else {
            isRunning = true
            isPaused = false
            _isRunningLiveData.postValue(isRunning)
            _isPausedLiveData.postValue(isPaused)

            Thread {
                while (isRunning) {
                    if (!isPaused) {
                        count += 0.01f
                        _countLiveData.postValue(count)
                    }
                    try {
                        Thread.sleep(10)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        break
                    }
                }
            }.start()
        }
    }

    fun stop() {
        count = 0f
        isRunning = false
        isPaused = false
        _countLiveData.postValue(count)
        _isRunningLiveData.postValue(isRunning)
        _isPausedLiveData.postValue(isPaused)
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private lateinit var btnStartPauseResume: Button
    private lateinit var btnStop: Button

    private lateinit var viewModel: CounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tv_result)
        btnStartPauseResume = findViewById(R.id.btn_start_pause_resume)
        btnStop = findViewById(R.id.btn_stop)

        viewModel = ViewModelProvider(this)[CounterViewModel::class.java]

        viewModel.countLiveData.observe(this) {
            tvResult.text = String.format("%.2f", it)
        }

        viewModel.isRunningLiveData.observe(this) {
            btnStartPauseResume.text = if (it) "Pause" else "Start"
            btnStop.isEnabled = it
        }

        viewModel.isPausedLiveData.observe(this) {
            if (!viewModel.isRunningLiveData.value!!) return@observe
            btnStartPauseResume.text = if (it) "Resume" else "Pause"
        }

        btnStartPauseResume.setOnClickListener {
            viewModel.startOrPause()
        }

        btnStop.setOnClickListener {
            viewModel.stop()
        }
    }
}