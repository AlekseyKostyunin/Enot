package com.alekseykostyunin.enot.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.presentation.navigation.OrdersScreenState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrdersViewModel : ViewModel() {

    // State
    private val initialState = OrdersScreenState.OrdersState
    private var _ordersScreenState = MutableLiveData<OrdersScreenState>(initialState)
    var ordersState: LiveData<OrdersScreenState> = _ordersScreenState

    init {
        getAllOrdersUser()
    }
    fun setAddOrderState(){
        _ordersScreenState.value = OrdersScreenState.AddOrderState
    }
    fun setOrdersState(){
        _ordersScreenState.value = OrdersScreenState.OrdersState
    }
    fun setOneOrderState(){
        _ordersScreenState.value = OrdersScreenState.OneOrderState
    }

    // Orders - all
    private var _orders = MutableLiveData<List<Order>>(listOf())
    var orders: LiveData<List<Order>> = _orders

    fun getAllOrdersUser() {
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
                    _orders.value = ordersDB.asReversed() // развернул, чтобы выводить последний заказ первым
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            })
        }
    }

    // Order
    private val initialOrder = Order() // пока пустой!!!
    private var _order = MutableLiveData<Order>(initialOrder)
    var order: LiveData<Order> = _order
    fun getOrderUser(order: Order) {
        _order.value = order
    }




}