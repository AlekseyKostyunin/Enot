package com.alekseykostyunin.enot.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.data.repositoryimpl.OrdersRepositoryImpl
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OrdersRepositoryImpl

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    private val _state2 = MutableStateFlow(State.Initial)
    val state2 = _state2

    private var _orders = MutableLiveData<List<Order>>(listOf())
    var orders: LiveData<List<Order>> = _orders

    private val initialOrder = Order()
    private var _order = MutableLiveData<Order>(initialOrder)
    var order: LiveData<Order> = _order
    
    private var _countActiveOrders = MutableLiveData<Int>(0)
    var countActiveOrders: LiveData<Int> = _countActiveOrders

    private var _countNotActiveOrders = MutableLiveData<Int>(0)
    var countNotActiveOrders: LiveData<Int> = _countNotActiveOrders

    private var _isShowBottomBar = MutableLiveData<Boolean>(true)
    var isShowBottomBar: LiveData<Boolean> = _isShowBottomBar

    fun notShowBottomBar() {
        _isShowBottomBar.value = false
    }
    fun showBottomBar() {
        _isShowBottomBar.value = true
    }

    init {
        getAllOrdersUser()
    }

    fun updateOrders(){
        getAllOrdersUser()
    }

    private fun getAllOrdersUser() {
        _state.value = State.Loading
        //delay(2000)
        val auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database.reference
        val user = auth.currentUser
        val ordersDB = mutableListOf<Order>()
        if (user != null) {
            val userId = user.uid
            val db = database.child("users").child(userId).child("orders")
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val order = i.getValue(Order::class.java)
                        if (order != null) {
                            ordersDB.add(order)
                        }
                    }
                    _orders.value = ordersDB.asReversed()
                    _countActiveOrders.value = ordersDB.filter {
                        it.isWork
                    }.size
                    _countNotActiveOrders.value = ordersDB.filter {
                        !it.isWork
                    }.size
                    _state.value = State.Success
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            })
        }
    }

    fun getOrderUser(order: Order) {
        _order.value = order
    }

}