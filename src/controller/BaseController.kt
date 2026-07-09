package controller

import model.*
import service.*
import util.*

open class BaseController {

    fun viewVendingMachine(vendingMachineId: String): VendingMachine {
        require(vendingMachineId.isNotBlank()) {"Vending machine ID cannot be null or empty."}
        return VendingMachineService.getVendingMachineById(vendingMachineId)
    }

    fun viewAllVendingMachines(): Set<VendingMachine> = VendingMachineService.getAllVendingMachines()

    fun viewAvailableProducts(vendingMachineId: String): Set<Food> {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be null or empty." }
        return VendingMachineService.viewAvailableProducts(vendingMachineId)
    }

    fun viewAvailableQuantityForAllProducts(vendingMachineId: String): Map<String, Int> {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be null or empty." }
        return VendingMachineService.viewAvailableQuantityForAllProducts(vendingMachineId)
    }

    fun getAvailableQuantityForOneProduct(
        vendingMachineId: String,
        foodId: String
    ): Int {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be null or empty." }
        require(foodId.isNotBlank()) { "Food ID cannot be null or empty." }

        return VendingMachineService.getAvailableQuantityForOneProduct(
            vendingMachineId,
            foodId
        )
    }
}
