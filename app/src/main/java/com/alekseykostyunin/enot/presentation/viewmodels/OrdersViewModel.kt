package com.alekseykostyunin.enot.presentation.viewmodels

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class OrdersViewModel(
    private val allOrdersUseCase: AllOrdersUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val addPhotoOrderUseCase: AddPhotoOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val closeOrderUseCase: CloseOrderUseCase,
    private val addHistoryStepUseCase: AddHistoryStepUseCase,
) : ViewModel() {

    var state: MutableStateFlow<State> = MutableStateFlow(State.Initial)
    var orders = MutableStateFlow<List<Order>>(listOf())
    var order = MutableStateFlow(Order())
    var oneClientAllOrders = MutableStateFlow<List<Order>>(listOf())
    var countActiveOrders = MutableStateFlow(0)
    var isShowBottomBar = MutableStateFlow(false)

    init {
        allOrders()
    }

    fun resetState() {
        state.value = State.Initial
    }

    fun updateOrders() {
        allOrders()
    }

    private fun allOrders() {
        allOrdersUseCase.invoke()
            .onStart {
                state.value = State.Loading
            }.onEach { listOrders ->
                orders.value = listOrders
                countActiveOrders.value = listOrders.filter {
                    it.status == StatusOrder.OPEN || it.status == StatusOrder.PAUSED
                }.size
                state.value = State.Success
            }.catch {
                state.value = State.Error("Произошла ошибка. Попробуйте позже")
            }.launchIn(viewModelScope)
    }

    fun showBottomBar() {
        isShowBottomBar.value = true
    }

    fun notShowBottomBar() {
        isShowBottomBar.value = false
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
            .onStart { state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                state.value = State.Success
            }
            .catch { state.value = State.Error("Произошла ошибка. Попробуйте позже") }
            .launchIn(viewModelScope)
    }

    fun closeOrder(order: Order) {
        closeOrderUseCase.invoke(order)
            .onStart { state.value = State.Loading }
            .onEach { closedOrder ->
                getOrderUser(closedOrder)
                state.value = State.Success
            }.catch { state.value = State.Error("Произошла ошибка. Попробуйте позже") }
            .launchIn(viewModelScope)
    }

    fun editOrder(order: Order) {
        editOrderUseCase.invoke(order)
    }

    fun getOrderUser(order: Order) {
        this.order.value = order
    }

    fun addHistoryStep(descStep: String) {
        addHistoryStepUseCase.invoke(order.value, descStep)
            .onStart { state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                state.value = State.Success
            }
            .catch { state.value = State.Error("Произошла ошибка. Попробуйте позже") }
            .launchIn(viewModelScope)
    }

    fun addHistoryStepZipOrdered(descStep: String) {
        val orderStep = order.value.copy(
            status = StatusOrder.PAUSED
        )
        addHistoryStepUseCase.invoke(orderStep, descStep)
            .onStart { state.value = State.Loading }
            .onEach { updateOrder ->
                getOrderUser(updateOrder)
                state.value = State.Success
            }
            .catch { state.value = State.Error("Произошла ошибка. Попробуйте позже") }
            .launchIn(viewModelScope)
    }

    fun getOneClientAllOrdersOnId(client: Client) {
        oneClientAllOrders.value = orders.value.filter { order ->
            order.client?.id == client.id
        }
    }

    /* Раздел для экрана аналитики заказо */
    var ordersForAnalytics = MutableStateFlow<List<Order>>(listOf())
    var priceZip = MutableStateFlow(0)
    var profit = MutableStateFlow(0)
    var countAllOrdersAsPeriod = MutableStateFlow(0)
    var countActiveOrdersForPeriod = MutableStateFlow(0)
    var countClosedOrdersForPeriod = MutableStateFlow(0)
    var dataPriceZip = MutableStateFlow<List<Float>>(listOf())
    var dataProfit = MutableStateFlow<List<Float>>(listOf())

    fun getOrdersForAnalytics(dateStart: Long, dateEnd: Long) {
        val ordersSort: List<Order> = orders.value.filter { order ->
            order.dateAdd in dateStart..dateEnd
        }.reversed()
        ordersForAnalytics.value = ordersSort.toMutableList()
        countAllOrdersAsPeriod.value = ordersSort.size
        countActiveOrdersForPeriod.value = ordersSort.filter { order ->
            order.status == StatusOrder.OPEN || order.status == StatusOrder.PAUSED
        }.size
        countClosedOrdersForPeriod.value =
            ordersSort.filter { order ->
                order.status == StatusOrder.CLOSED
            }.size
        priceZip.value = ordersSort.sumOf { order -> order.priceZip }
        profit.value = ordersSort.sumOf { order -> order.priceWork }
        val preDataPriceZip = mutableListOf<Float>()
        val preDataProfit = mutableListOf<Float>()
        for (order in ordersSort) {
            preDataPriceZip.add(order.priceZip.toFloat())
            preDataProfit.add(order.priceWork.toFloat())
        }
        dataPriceZip.value = preDataPriceZip
        dataProfit.value = preDataProfit
    }

}