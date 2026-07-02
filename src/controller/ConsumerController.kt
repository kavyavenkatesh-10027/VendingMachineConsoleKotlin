package controller

import model.Purchase
import service.PurchaseService
import service.VendingMachineService
import util.IndianCurrency
import util.VendingMachineException
import java.math.BigDecimal

class ConsumerController : BaseController() {

    fun buyProducts(
        vendingMachineId: String,
        cart: Map<String, Int>,
        inserted: Map<IndianCurrency, Int>
    ): Purchase {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        if (cart.isEmpty()) throw VendingMachineException("Cart is empty. Please select at least one product.")
        if (inserted.isEmpty()) throw VendingMachineException("No money inserted. Please insert payment.")
        val vm = VendingMachineService.getVendingMachineById(vendingMachineId)
        return PurchaseService.processPurchase(vm, cart, inserted)
    }

    fun getCartTotal(cart: Map<String, Int>): BigDecimal {
        if (cart.isEmpty()) throw VendingMachineException("Cart is empty.")
        return PurchaseService.getCartTotal(cart)
    }

    fun getAvailableStock(vendingMachineId: String, foodId: String): Int =
        getAvailableQuantityForOneProduct(vendingMachineId, foodId)

}