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
        if (quantityOfProductsPurchased.isEmpty()) throw VendingMachineException("Purchase cannot be made with an empty cart. Apologies!")
        if (totalAmount <= BigDecimal.ZERO) throw VendingMachineException("Price cannot be negative or zero")
        if (moneyPaidByCustomer <= BigDecimal.ZERO) throw VendingMachineException("Cash paid cannot be negative or zero")
        if (moneyToBeReturnedByVendingMachine < BigDecimal.ZERO) throw VendingMachineException("Change cannot be negative. Only zero or a positive value")
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