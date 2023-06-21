package com.demo.demofirestore.base


import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import javax.inject.Inject


abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity() {
    abstract val layoutId: Int
    abstract val bindingVariable: Int

    @Inject
    lateinit var mViewModel: V
    lateinit var binding: T
    lateinit var fragManager: FragmentManager

    lateinit var activity: Activity
    lateinit var baseActivity: BaseActivity<T, V>

    @Inject
    lateinit var gson: Gson

    var applyPaddingBottomToWindow = true
    var isShown = false
    var flag = true
    var liveActivity = false

    abstract fun setupObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = this
        fragManager = supportFragmentManager
        activity = this
        performDataBinding()

    }

    private fun performDataBinding() {
        activity = this
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(bindingVariable, mViewModel)
        binding.executePendingBindings()
        setupObservable()
        binding.root.rootView.clearFocus()
    }
}