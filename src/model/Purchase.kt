package model

import util.*
import java.math.BigDecimal
import java.time.LocalDateTime

data class Purchase(
    val quantityOfProductsPurchased: Map<String, Int>,
    val totalAmount: BigDecimal,
    val moneyPaidByCustomer: BigDecimal,
    val moneyToBeReturnedByVendingMachine: BigDecimal,
    val purchaseId: String = Generator.generatePurchaseId(),
    val purchaseTime: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(quantityOfProductsPurchased.isNotEmpty())
        require(totalAmount >= BigDecimal.ZERO)
        require(moneyPaidByCustomer >= BigDecimal.ZERO)
        require(moneyToBeReturnedByVendingMachine >= BigDecimal.ZERO)
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