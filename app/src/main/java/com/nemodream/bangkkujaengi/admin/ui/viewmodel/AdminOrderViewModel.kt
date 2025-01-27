package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.checkbox.MaterialCheckBox
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.data.repository.AdminOrderRepository
import com.nemodream.bangkkujaengi.customer.data.model.Purchase
import kotlinx.coroutines.launch

class AdminOrderViewModel(
    private val repository: AdminOrderRepository = AdminOrderRepository() // 기본값으로 Repository 생성
) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>(emptyList())
    val orders: LiveData<List<Order>> get() = _orders

    private val selectedOrders = mutableSetOf<String>()

    private val _headerCheckboxState = MutableLiveData<Int>()
    val headerCheckboxState: LiveData<Int> get() = _headerCheckboxState

    private val _filteredOrders = MutableLiveData<List<Order>>()
    val filteredOrders: LiveData<List<Order>> get() = _filteredOrders

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // 선택된 항목 수 LiveData
    private val _selectedItemCount = MutableLiveData<Int>()
    val selectedItemCount: LiveData<Int> get() = _selectedItemCount

    init {
        // 초기값 설정
        _selectedItemCount.value = 0
    }

    fun loadOrders(state: OrderState) {
        viewModelScope.launch {
            try {
                val result = repository.fetchOrdersByState(state.name)
                _orders.value = result
                _filteredOrders.value = result
            } catch (e: Exception) {
                _errorMessage.value = "주문 데이터를 가져오는 데 실패했습니다."
            }
        }
    }


    fun handleNextState(order: Order) {
        // 다음 상태 처리 로직 (예: API 호출)
        // 필요시 LiveData를 업데이트
    }

    fun handleCancel(order: Order) {
        // 취소 처리 로직 (예: API 호출)
        // 필요시 LiveData를 업데이트
    }

    fun isOrderSelected(orderNumber: String): Boolean {
        return selectedOrders.contains(orderNumber)
    }

    fun updateOrderSelection(orderNumber: String, isChecked: Boolean) {
        if (isChecked) selectedOrders.add(orderNumber) else selectedOrders.remove(orderNumber)
        _selectedItemCount.value = selectedOrders.size
        updateCheckboxState()
    }

    fun toggleSelectAllOrders(selectAll: Boolean) {
        val currentOrders = _orders.value ?: return
        selectedOrders.clear()

        if (selectAll) {
            currentOrders.forEach { selectedOrders.add(it.orderNumber) }
        }
        _selectedItemCount.value = selectedOrders.size
        updateCheckboxState()

        // LiveData로 선택된 항목 업데이트를 트리거
        _orders.value = currentOrders.toList() // 강제로 LiveData 업데이트
    }

    fun getSelectedOrders(): List<Order> {
        return _orders.value?.filter { selectedOrders.contains(it.orderNumber) } ?: emptyList()
    }

    fun updateOrderState(order: Order, newState: OrderState) {
        viewModelScope.launch {
            try {
                repository.updateOrderState(order.orderNumber, newState.name)
                val updatedOrders = _orders.value?.map {
                    if (it.orderNumber == order.orderNumber) it.copy(state = newState) else it
                }
                _orders.value = updatedOrders!!
                _filteredOrders.value = updatedOrders?.filter { it.state == newState }
            } catch (e: Exception) {
                _errorMessage.value = "주문 상태를 업데이트하는 데 실패했습니다."
            }
        }
    }


    private fun updateCheckboxState() {
        val currentOrders = _orders.value ?: return
        val selectedCount = selectedOrders.size
        val totalCount = currentOrders.size

        _headerCheckboxState.value = when {
            selectedCount == 0 -> MaterialCheckBox.STATE_UNCHECKED
            selectedCount == totalCount -> MaterialCheckBox.STATE_CHECKED
            else -> MaterialCheckBox.STATE_INDETERMINATE
        }
    }
}
