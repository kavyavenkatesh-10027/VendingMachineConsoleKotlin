package controller

import model.Purchase
import service.PurchaseService
import service.VendingMachineService
import util.IndianCurrency
import java.math.BigDecimal

class ConsumerController : BaseController() {

    fun buyProducts(
        vendingMachineId: String,
        cart: Map<String, Int>,
        inserted: Map<IndianCurrency, Int>
    ): Purchase {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be null or empty." }
        require(cart.isNotEmpty()) { "Cart is empty. Please select at least one product." }
        require(inserted.isNotEmpty()) { "No money inserted. Please insert payment." }

        val vm = VendingMachineService.getVendingMachineById(vendingMachineId)
        return PurchaseService.processPurchase(vm, cart, inserted)
    }

    fun getCartTotal(cart: Map<String, Int>): BigDecimal {
        require(cart.isNotEmpty()) { "Cart is empty." }
        return PurchaseService.getCartTotal(cart)
    }

    fun getAvailableStock(vendingMachineId: String, foodId: String): Int =
        getAvailableQuantityForOneProduct(vendingMachineId, foodId)
}