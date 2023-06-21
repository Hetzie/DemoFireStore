package com.demo.demofirestore.ui.main

import android.os.Bundle
import com.demo.demofirestore.BR
import com.demo.demofirestore.R
import com.demo.demofirestore.base.BaseActivity
import com.demo.demofirestore.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setupObservable() {

    }
}