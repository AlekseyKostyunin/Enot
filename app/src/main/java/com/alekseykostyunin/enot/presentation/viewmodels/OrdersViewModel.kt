package com.alekseykostyunin.enot.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alekseykostyunin.enot.domain.entities.Client
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.entities.StatusOrder
import com.alekseykostyunin.enot.domain.usecase.orders.AddHistoryStepUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddPhotoOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AllOrdersUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.CloseOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.EditOrderUseCase
import com.alekseykostyunin.enot.presentation.navigation.State
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

private const val ERROR_LOAD = "Произошла ошибка. Попробуйте позже."

class OrdersViewModel(
    private val allOrdersUseCase: AllOrdersUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val addPhotoOrderUseCase: AddPhotoOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val closeOrderUseCase: CloseOrderUseCase,
    private val addHistoryStepUseCase: AddHistoryStepUseCase,
) : ViewModel() {

    private var _state: MutableStateFlow<State> = MutableStateFlow(State.Initial)
    var state: StateFlow<State> = _state

    private var _orders = MutableStateFlow<List<Order>>(listOf())
    var orders: StateFlow<List<Order>> = _orders

    private var _order = MutableStateFlow(Order())
    var order: StateFlow<Order> = _order

    private var _oneClientAllOrders = MutableStateFlow<List<Order>>(listOf())
    var oneClientAllOrders: StateFlow<List<Order>> = _oneClientAllOrders

    private var _countActiveOrders = MutableStateFlow(0)
    var countActiveOrders: StateFlow<Int> = _countActiveOrders

    private var _isShowBottomBar = MutableStateFlow(false)
    var isShowBottomBar: StateFlow<Boolean> = _isShowBottomBar

    init {
        allOrders()
    }

    fun resetState() {
        _state.value = State.Initial
    }

    fun updateOrders() {
        getAllOrdersUser()
        allOrders()
    }

    private fun allOrders() {
        allOrdersUseCase.invoke()
            .onStart {
                _state.value = State.Loading
                Log.e("TEST_allOrders", "State.Loading")
            }
            .onEach { listOrders ->
                _orders.value = listOrders
                _countActiveOrders.value = listOrders.filter {
                    it.status == StatusOrder.OPEN || it.status == StatusOrder.PAUSED
                }.size
                _state.value = State.Success
                Log.e("TEST_allOrders", "State.Success")
                Log.e("TEST_allOrdersSuc", listOrders.toString())
            }.catch {
                _state.value = State.Error(ERROR_LOAD)
                Log.e("TEST_allOrders", it.message.toString())
            }.launchIn(viewModelScope)
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
                    val countActiveOrders = ordersDB.filter {
                        it.status == StatusOrder.OPEN || it.status == StatusOrder.PAUSED
                    }.size
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

    fun showBottomBar() {
        _isShowBottomBar.value = true
    }

    fun notShowBottomBar() {
        _isShowBottomBar.value = false
    }

    fun addOrder(order: Order) {
        addOrderUseCase.invoke(order)
    }

    var urlPhotoOrder = MutableStateFlow("")

    fun insertUrlPhoto(urlPhoto: String) {
        urlPhotoOrder.value = urlPhoto
    }

    fun addPhoto(photoUri: String) {
        addPhotoOrderUseCase.invoke(photoUri, order.value)
            .onStart { _state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                _state.value = State.Success
            }
            .catch {
                _state.value = State.Error(ERROR_LOAD)
                Log.e("TEST_addPhoto", it.message.toString())
            }
            .launchIn(viewModelScope)
    }

    fun closeOrder(order: Order) {
        closeOrderUseCase.invoke(order)
            .onStart {
                _state.value = State.Loading
                Log.e("TEST_closeOrder", "State.Loading")
            }
            .onEach { closedOrder ->
                getOrderUser(closedOrder)
                _state.value = State.Success
            }.catch { _state.value = State.Error(ERROR_LOAD) }
            .launchIn(viewModelScope)
    }

    fun editOrder(order: Order) {
        editOrderUseCase.invoke(order)
    }

    fun getOrderUser(order: Order) {
        _order.value = order
    }

    fun addHistoryStep(descStep: String) {
        addHistoryStepUseCase.invoke(order.value, descStep)
            .onStart { _state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                _state.value = State.Success
            }
            .catch {
                _state.value = State.Error(ERROR_LOAD)
            }
            .launchIn(viewModelScope)
    }

    fun addHistoryStepZipOrdered(descStep: String) {
        val orderStep = order.value.copy(
            status = StatusOrder.PAUSED
        )
        addHistoryStepUseCase.invoke(orderStep, descStep)
            .onStart { _state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                _state.value = State.Success
            }
            .catch {
                _state.value =
                    State.Error(ERROR_LOAD)
            }
            .launchIn(viewModelScope)
    }

    fun getOneClientAllOrdersOnId(client: Client) {
        _oneClientAllOrders.value = orders.value.filter { order ->
            order.client?.id == client.id
        }
    }

    /* Analytics */
    private var _ordersForAnalytics = MutableStateFlow<List<Order>>(listOf())
    var ordersForAnalytics: StateFlow<List<Order>> = _ordersForAnalytics

    private var _priceZip = MutableStateFlow(0)
    var priceZip: StateFlow<Int> = _priceZip

    private var _profit = MutableStateFlow(0)
    var profit: StateFlow<Int> = _profit

    private var _countAllOrdersAsPeriod = MutableStateFlow(0)
    var countAllOrdersAsPeriod: StateFlow<Int> = _countAllOrdersAsPeriod

    private var _countActiveOrdersForPeriod = MutableStateFlow(0)
    var countActiveOrdersForPeriod: StateFlow<Int> = _countActiveOrdersForPeriod

    private var _countClosedOrdersForPeriod = MutableStateFlow(0)
    var countClosedOrdersForPeriod: StateFlow<Int> = _countClosedOrdersForPeriod

    private var _dataPriceZip = MutableStateFlow<List<Float>>(listOf())
    var dataPriceZip: StateFlow<List<Float>> = _dataPriceZip

    private var _dataProfit = MutableStateFlow<List<Float>>(listOf())
    var dataProfit: StateFlow<List<Float>> = _dataProfit

    fun getOrdersForAnalytics(dateStart: Long, dateEnd: Long) {
        val ordersSort: List<Order> = _orders.value.filter { order ->
            order.dateAdd in dateStart..dateEnd
        }.reversed()
        _ordersForAnalytics.value = ordersSort.toMutableList()
        _countAllOrdersAsPeriod.value = ordersSort.size
        _countActiveOrdersForPeriod.value = ordersSort.filter { order ->
            order.status == StatusOrder.OPEN || order.status == StatusOrder.PAUSED
        }.size
        _countClosedOrdersForPeriod.value =
            ordersSort.filter { order ->
                order.status == StatusOrder.CLOSED
            }.size
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