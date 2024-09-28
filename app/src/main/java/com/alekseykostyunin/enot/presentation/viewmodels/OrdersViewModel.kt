package com.alekseykostyunin.enot.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alekseykostyunin.enot.data.repositoryimpl.OrdersRepositoryImpl
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.entities.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OrdersRepositoryImpl

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    private val _state2 = MutableStateFlow(State.Initial)
    val state2 = _state2

    private var _orders = MutableLiveData<List<Order>>(listOf())
    var orders: LiveData<List<Order>> = _orders

    private var _order = MutableLiveData<Order>(Order())
    var order: LiveData<Order> = _order

    private var _countActiveOrders = MutableLiveData<Int>(0)
    var countActiveOrders: LiveData<Int> = _countActiveOrders

    private var _countNotActiveOrders = MutableLiveData<Int>(0)
    var countNotActiveOrders: LiveData<Int> = _countNotActiveOrders

    private var _isShowBottomBar = MutableLiveData<Boolean>(false)
    var isShowBottomBar: LiveData<Boolean> = _isShowBottomBar

    fun notShowBottomBar() {
        _isShowBottomBar.value = false
    }

    fun showBottomBar() {
        _isShowBottomBar.value = true
    }

    private var _urlPhoto = MutableLiveData<String>("")
    var urlPhoto: LiveData<String> = _urlPhoto

    fun insertUrlPhoto(urlPhoto: String) {
        _urlPhoto.value = urlPhoto
    }

    fun addPhoto(photoUri: String) {
        val auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database.reference
        val user = auth.currentUser

        if (user != null) {
            val userId = user.uid
            val idOrder = order.value?.id
            var photos = order.value?.photos?.toMutableList()
            if(photos == null){
                photos = mutableListOf(Photo(photoUri))
            } else {
                photos.add(Photo(photoUri))
                Log.d("TEST_history", photos.toString())
            }

            val orderUpdate = order.value?.let {
                order ->
                Order(
                    id = idOrder,
                    client = order.client,
                    dateAdd = order.dateAdd,
                    dateClose = 0,
                    description = order.description,
                    type = order.type,
                    model = order.model,
                    priceZip = order.priceZip,
                    priceWork = order.priceWork,
                    isWork = true,
                    history = order.history,
                    photos = photos,
                    comment = order.comment,
                )
            }

            database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder!!)
                .setValue(orderUpdate)

            val dbNewOrderUpdate = database
                .child("users")
                .child(userId)
                .child("orders")
                .child(idOrder)

            dbNewOrderUpdate.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order2 = snapshot.getValue(Order::class.java)
                    if (order2 != null) {
                        Log.d(
                            "TEST_snapshot_OneOrderScreen",
                            order2.toString()
                        )
                        getOrderUser(order2)
                        updateOrders()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TEST_snapshot_error", error.message)
                }
            })
            //updateOrders()
        }
    }

    init {
        getAllOrdersUser()
    }

    fun updateOrders() {
        getAllOrdersUser()
    }

    private fun getAllOrdersUser() {
        _state.value = State.Loading
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
                    val ordersRevers = ordersDB.asReversed()
                    _orders.value = ordersRevers
                    val countActiveOrders = ordersDB.filter { it.isWork }.size
                    _countActiveOrders.value = countActiveOrders
                    _state.value = State.Success
                    Log.d("TEST_snapshot_countActiveOrders", _countActiveOrders.value.toString())
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

    // Раздел для экрана аналитики заказов
    private var _ordersForAnalytics = MutableLiveData<List<Order>>(listOf())
    var ordersForAnalytics: LiveData<List<Order>> = _ordersForAnalytics

    private var _priceZip = MutableLiveData<Int>(0)
    var priceZip: LiveData<Int> = _priceZip

    private var _profit = MutableLiveData<Int>(0)
    var profit: LiveData<Int> = _profit

    private var _countAllOrdersAsPeriod = MutableLiveData<Int>(0)
    var countAllOrdersAsPeriod: LiveData<Int> = _countAllOrdersAsPeriod

    private var _countActiveOrdersForPeriod = MutableLiveData<Int>(0)
    var countActiveOrdersForPeriod: LiveData<Int> = _countActiveOrdersForPeriod

    private var _countClosedOrdersForPeriod = MutableLiveData<Int>(0)
    var countClosedOrdersForPeriod: LiveData<Int> = _countClosedOrdersForPeriod

    private var _dataPriceZip = MutableLiveData<List<Float>>(listOf())
    var dataPriceZip: LiveData<List<Float>> = _dataPriceZip

    private var _dataProfit = MutableLiveData<List<Float>>(listOf())
    var dataProfit: LiveData<List<Float>> = _dataProfit

    fun getOrdersForAnalytics(dateStart: Long, dateEnd: Long) {
        _orders.value?.let {
            val ordersSort: List<Order>? = _orders.value?.filter { order ->
                order.dateAdd in dateStart..dateEnd
            }?.reversed()
            if (ordersSort == null){
                _countAllOrdersAsPeriod.value = 0
            } else {
                _ordersForAnalytics.value = ordersSort.toMutableList()
                _countAllOrdersAsPeriod.value = ordersSort.size
                _countActiveOrdersForPeriod.value = ordersSort.filter { order -> order.isWork }.size
                _countClosedOrdersForPeriod.value = ordersSort.filter { order ->!order.isWork }.size
                _priceZip.value = ordersSort.sumOf { order -> order.priceZip }
                _profit.value = ordersSort.sumOf { order -> order.priceWork }

                val preDataPriceZip = mutableListOf<Float>()
                val preDataProfit = mutableListOf<Float>()
                for (order in ordersSort) {
                    preDataPriceZip.add(order.priceZip.toFloat())
                    preDataProfit.add(order.priceWork.toFloat())
                }
                _dataPriceZip.value = preDataPriceZip
                _dataProfit.value = preDataProfit
            }
        }
    }

    // Заказы одного клиента
    private var _oneClientAllOrders = MutableLiveData<List<Order>>(listOf())
    var oneClientAllOrders: LiveData<List<Order>> = _oneClientAllOrders

    fun getOneClientAllOrdersOnId(idClient: String) {
        _orders.value?.let {
            _oneClientAllOrders.value = it.filter { order -> order.client?.id == idClient }
        }
    }



}