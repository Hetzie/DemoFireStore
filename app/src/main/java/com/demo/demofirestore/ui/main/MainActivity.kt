package com.demo.demofirestore.ui.main

import android.os.Bundle
import com.demo.demofirestore.BR
import com.demo.demofirestore.R
import com.demo.demofirestore.base.BaseActivity
import com.demo.demofirestore.databinding.ActivityMainBinding
import com.demo.demofirestore.ui.main.adapter.UserAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val bindingVariable: Int
        get() = BR.viewModel
    private lateinit var db: FirebaseFirestore
    lateinit var adapter: UserAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        db = FirebaseFirestore.getInstance()

        val query = db.collection(mViewModel.collection)

        adapter = UserAdapter(query, this, this)
        binding.rvMain.adapter = adapter

    }

    override fun setupObservable() {

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.startListening()
    }
}