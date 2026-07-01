package service

import model.Purchase
import model.VendingMachine
import repository.FoodRepository
import repository.PurchaseRepository
import util.IndianCurrency
import util.VendingMachineException
import java.math.BigDecimal

object PurchaseService {

    fun processPurchase(
        vm: VendingMachine,
        cart: Map<String, Int>,
        inserted: Map<IndianCurrency, Int>
    ): Purchase {
        // Validate cart items and stock
        for ((foodId, requestedQty) in cart) {
            if (foodId.isBlank()) throw VendingMachineException("Food ID in cart cannot be null or empty.")
            if (requestedQty <= 0) throw VendingMachineException("Quantity for food $foodId must be greater than zero.")
            val food = FoodRepository.findById(foodId)
            val stock = getStockInMachine(vm, foodId)
            if (stock < requestedQty) {
                throw VendingMachineException("Insufficient stock for '${food.productName}'. Available: $stock")
            }
        }

        val total = calculateTotal(cart)
        val amountPaid = CurrencyService.acceptPayment(vm.drawer, inserted)

        if (amountPaid < total) {
            CurrencyService.refund(vm.drawer, inserted)
            throw VendingMachineException(
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

    private fun calculateTotal(cart: Map<String, Int>): BigDecimal =
        cart.entries.fold(BigDecimal.ZERO) { sum, (foodId, qty) ->
            sum + FoodRepository.findById(foodId).price * BigDecimal.valueOf(qty.toLong())
        }//todo

    fun getCartTotal(cart: Map<String, Int>): BigDecimal = calculateTotal(cart)

    fun getStockInMachine(vm: VendingMachine, foodId: String): Int =
        vm.slotsInVendingMachine.sumOf { it.foodItemsInSlot[foodId] ?: 0 }

    private fun deductStockFromSlots(vm: VendingMachine, cart: Map<String, Int>) {
        for ((foodId, requestedQty) in cart) {
            var remaining = requestedQty
            for (slot in vm.slotsInVendingMachine) {
                if (remaining <= 0) break
                val inSlot = slot.foodItemsInSlot[foodId] ?: 0
                if (inSlot > 0) {
                    val deduct = minOf(inSlot, remaining)
                    slot.removeFoodItemFromSlot(foodId, deduct)
                    remaining -= deduct
                }
            }
        }
    }

    fun getAllPurchases(): Set<Purchase> = PurchaseRepository.findAll()
}