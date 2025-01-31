package com.nemodream.bangkkujaengi.customer.data.model

import com.google.firebase.Timestamp

data class Purchase(
    val documentId: String = "",
    val purchaseId: String = "",
    val nonMemberPassword: String = "",
    val memberId: String = "",
    val productTitle : String = "",
    val color : String = "",
    val images : String = "",
    val productId : String = "",
    val productCost : Int = 0,
    val couponSalePrice : Int = 0,
    val saleRate : Int = 0,
    val totPrice : Int = 0,
    val purchaseDate : Timestamp = Timestamp.now(),
    val purchaseState: String = "",
    val purchaseInvoiceNumber : Long  = 0,
    val purchaseQuantity : Int = 0,
    val Delete: Boolean = false,
    val purchaseDateTime: String = "",
    val deliveryCost: Int = 0,
    val receiverName: String = "",
    val receiverZipCode: String = "",
    val receiverAddr: String = "",
    val receiverDetailAddr: String = "",
    val receiverPhone: String = ""
)

enum class PurchaseState() {
    PAYMENT_COMPLETED,
    READY_TO_SHIP,
    SHIPPING,
    DELIVERED,
    PURCHASE_CONFIRMED,
}