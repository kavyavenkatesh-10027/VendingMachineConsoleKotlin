package controller

import model.*
import service.*
import util.*

open class BaseController {

    fun viewVendingMachine(vendingMachineId: String): VendingMachine {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return VendingMachineService.getVendingMachineById(vendingMachineId)
    }

    fun viewAllVendingMachines(): Set<VendingMachine> = VendingMachineService.getAllVendingMachines()

    fun viewAvailableProducts(vendingMachineId: String): Set<Food> {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return VendingMachineService.viewAvailableProducts(vendingMachineId)
    }

    fun viewAvailableQuantityForAllProducts(vendingMachineId: String): Map<String, Int> {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return VendingMachineService.viewAvailableQuantityForAllProducts(vendingMachineId)
    }

    fun getAvailableQuantityForOneProduct(vendingMachineId: String, foodId: String): Int {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        return VendingMachineService.getAvailableQuantityForOneProduct(vendingMachineId, foodId)
    }
}
