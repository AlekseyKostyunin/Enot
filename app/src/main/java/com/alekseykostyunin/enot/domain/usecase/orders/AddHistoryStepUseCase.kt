package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class AddHistoryStepUseCase(private val ordersRepository: OrdersRepository) {

    fun addHistoryStep(idOrder: String, historyStep: HistoryStep) {
        ordersRepository.addHistoryStep(idOrder, historyStep)
    }
}