package model

import util.*
import java.math.BigDecimal
import java.time.LocalDateTime

data class Purchase(
    private val quantityOfProductsPurchased: Map<String, Int>,
    val totalAmount: BigDecimal,
    val moneyPaidByCustomer: BigDecimal,
    val moneyToBeReturnedByVendingMachine: BigDecimal,
    val purchaseTime: LocalDateTime = LocalDateTime.now()
) {

    val purchaseId: String = Generator.generatePurchaseId()

    fun getQuantityOfProductsPurchased(): Map<String, Int> {
        return quantityOfProductsPurchased.toMap() // Returns a read-only copy
    }

    init {
        require(quantityOfProductsPurchased.isNotEmpty()) { "Purchase cannot be made with an empty cart." }
        require(totalAmount > BigDecimal.ZERO) { "Total amount must be greater than zero." }
        require(moneyPaidByCustomer > BigDecimal.ZERO) { "Cash paid must be greater than zero." }
        require(moneyToBeReturnedByVendingMachine >= BigDecimal.ZERO) { "Change cannot be negative." }
    }

    override fun toString(): String =
        """
    Purchase ID             : $purchaseId
    Purchase Time           : $purchaseTime
    Products                : $quantityOfProductsPurchased
    Total Amount            : ₹$totalAmount
    Amount Paid             : ₹$moneyPaidByCustomer
    Change Returned         : ₹$moneyToBeReturnedByVendingMachine
    """.trimIndent()
}