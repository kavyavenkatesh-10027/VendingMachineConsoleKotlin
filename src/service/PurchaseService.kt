package service

import model.Purchase
import model.VendingMachine
import repository.FoodRepository
import repository.PurchaseRepository
import util.*
import java.math.BigDecimal

//The purpose of PurchaseService is to handle purchases, and it does this by object (Singleton class in Java).
object PurchaseService {

    //Why? For preventing invalid purchases and managing stock data on a clear purchase.
    // For handling demand-supply problem. Insufficient payment, change making, and scarcity of denomination leads to refund .
    // Updating product stock on purchase.
    fun processPurchase(
        vm: VendingMachine,
        cart: Map<String, Int>,
        inserted: Map<IndianCurrency, Int>
    ): Purchase {
        // Validate cart items and stock
        for ((foodId, requestedQty) in cart) {
            require(foodId.isNotBlank()) { "Food ID in cart cannot be empty."}
            require(requestedQty > 0) {"Quantity for food $foodId must be greater than zero."}
            val food = FoodRepository.findById(foodId)
            val stock = getStockInMachine(vm, foodId)
            if (stock < requestedQty) {
                throw SupplyDemandException("Insufficient stock for '${food.productName}'. Available: $stock")
            }
        }

        val total = getCartTotal(cart)
        val amountPaid = CurrencyService.acceptPayment(vm.drawer, inserted)

        if (amountPaid < total) {
            CurrencyService.refund(vm.drawer, inserted)
            throw InsufficientPaymentException(
                "Insufficient payment. Total: Rs.$total, Paid: Rs.$amountPaid\nCollect refund from the inserting plate"
            )
        }

        val changeAmount = amountPaid - total
        try {
            CurrencyService.makeChange(vm.drawer, changeAmount)
        } catch (e: VendingMachineException) {
            CurrencyService.refund(vm.drawer, inserted)
            throw e
        }

        deductStockFromSlots(vm, cart)

        val purchase = Purchase(cart, total, amountPaid, changeAmount)
        PurchaseRepository.add(purchase)
        return purchase
    }

    //Why? To avoid duplication send cartTotal to the initial level (ie Controller, UI layers)
    fun getCartTotal(cart: Map<String, Int>): BigDecimal {
        var total = BigDecimal.ZERO

        for ((foodId, quantity) in cart) {
            val food = FoodRepository.findById(foodId)
            total += food.price * BigDecimal.valueOf(quantity.toLong())
        }

        return total
    }

    //Why? To get a consolidated value per product, instead of slot-wise
    fun getStockInMachine(vm: VendingMachine, foodId: String): Int =
        vm.getSlotsInVendingMachine().sumOf { it.getFoodItemsInSlot()[foodId] ?: 0 }

    //Why? For collecting food items scattered across various slots
    private fun deductStockFromSlots(vm: VendingMachine, cart: Map<String, Int>) {
        for ((foodId, requestedQty) in cart) {
            var remaining = requestedQty
            for (slot in vm.getSlotsInVendingMachine()) {
                if (remaining <= 0) break
                val inSlot = slot.getFoodItemsInSlot()[foodId] ?: 0
                if (inSlot > 0) {
                    val deduct = minOf(inSlot, remaining)
                    slot.removeFoodItemFromSlot(foodId, deduct)
                    remaining -= deduct
                }
            }
        }
    }

    //Why? For consistency and maintaining Controller->Service->Repository flow
    fun getAllPurchases(): Set<Purchase> = PurchaseRepository.findAll()
}