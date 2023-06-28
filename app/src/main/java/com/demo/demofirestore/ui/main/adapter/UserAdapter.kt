package com.demo.demofirestore.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.demo.demofirestore.R
import com.demo.demofirestore.base.BaseViewHolder
import com.demo.demofirestore.data.dummy.User
import com.demo.demofirestore.databinding.CustomListBinding
import com.demo.demofirestore.ui.main.MainActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


open class UserAdapter(
    val query: Query,
    var context: Context,
    var mainActivity: MainActivity

) :
    RecyclerSwipeAdapter<UserAdapter.ViewHolder>(), EventListener<QuerySnapshot> {

    lateinit var binding: CustomListBinding
    private val snapshot = ArrayList<DocumentSnapshot>()
    private var registration: ListenerRegistration? = null
    var myItemManger = SwipeItemRecyclerMangerImpl(this)

    inner class ViewHolder(binding: CustomListBinding) : BaseViewHolder(binding.root) {

        var mBinding = binding

        @SuppressLint("SetTextI18n")
        override fun onBind(position: Int) {
            myItemManger.bindView(itemView, position)
            var user: User
            getItem(position).let { snapshot ->
                user = snapshot.toObject(User::class.java)?: User()
                mBinding.model = user
            }

            mBinding.layoutButtons.imgEdit.setOnClickListener{
                mainActivity.mViewModel.updateParticularRecord(mainActivity, user.name!!, user.email!!)
                mBinding.swipeRootView.close()
            }
            mBinding.layoutButtons.imgDelete.setOnClickListener{
                myItemManger.closeAllItems()
                mainActivity.mViewModel.deleteParticularData(mainActivity, user.name!!)
            }
            mBinding.swipeRootView.addSwipeListener(object : SimpleSwipeListener() {
                override fun onStartOpen(layout: SwipeLayout?) {
                    super.onStartOpen(layout)
//                    mBinding.cvMainLayout.setRadius(0f,0)
                    mItemManger.closeAllExcept(layout)
                }

                override fun onStartClose(layout: SwipeLayout?) {
                    super.onStartClose(layout)
                    mBinding.cvUser.radius = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        context.resources.displayMetrics
                    )
                }

                override fun onOpen(layout: SwipeLayout?) {
                    super.onOpen(layout)
                    mBinding.cvUser.radius = 0f
                }
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CustomListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.ViewHolder(holder.mBinding).onBind(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeRootView
    }

    fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        registration!!.remove()
        registration = null
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e("onEvent:error", error.toString())
            return
        }

        for (change in value!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
            }
        }
    }

    protected open fun onDocumentAdded(change: DocumentChange) {
        snapshot.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            snapshot[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            snapshot.removeAt(change.oldIndex)
            snapshot.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected open fun onDocumentRemoved(change: DocumentChange) {
        snapshot.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun getItemCount(): Int {
        return snapshot.size
    }

    fun getItem(index: Int): DocumentSnapshot {
        return snapshot[index]
    }

}