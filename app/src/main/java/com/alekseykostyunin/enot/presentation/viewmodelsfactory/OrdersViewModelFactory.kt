package com.alekseykostyunin.enot.presentation.viewmodelsfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alekseykostyunin.enot.domain.usecase.orders.AddHistoryStepUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AddPhotoOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.AllOrdersUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.CloseOrderUseCase
import com.alekseykostyunin.enot.domain.usecase.orders.EditOrderUseCase
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.UserViewModel

class OrdersViewModelFactory(
    private val allOrdersUseCase: AllOrdersUseCase,
    private val addOrderUseCase: AddOrderUseCase,
    private val addPhotoOrderUseCase: AddPhotoOrderUseCase,
    private val editOrderUseCase: EditOrderUseCase,
    private val closeOrderUseCase: CloseOrderUseCase,
    private val addHistoryStepUseCase: AddHistoryStepUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrdersViewModel(
                allOrdersUseCase = allOrdersUseCase,
                addOrderUseCase = addOrderUseCase,
                addPhotoOrderUseCase = addPhotoOrderUseCase,
                editOrderUseCase = editOrderUseCase,
                closeOrderUseCase = closeOrderUseCase,
                addHistoryStepUseCase = addHistoryStepUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}