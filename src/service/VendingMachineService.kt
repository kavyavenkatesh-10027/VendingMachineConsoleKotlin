package service

import exception.CorruptedDataException
import exception.UnknownEntityException
import exception.VendingMachineException
import model.Food
import model.Slot
import model.VendingMachine
import repository.FoodRepository
import repository.SlotRepository
import repository.VendingMachineRepository
import model.enum.*
import java.time.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

//The purpose of VendingMachineService is to manage vending machine, and it does this by object (Singleton class in Java).
object VendingMachineService {

    //Why? For maintaining the right order in the process of creating a vending machine.
    fun createVendingMachine(
        location: Location,
        establishedOn: LocalDate,
        firstSlotFoodItems: Map<String, Int>
    ): VendingMachine {
        require(establishedOn <= LocalDate.now()) {"Established date must be on or before the current date"}

        val vm = VendingMachine(location, establishedOn)
        VendingMachineRepository.add(vm)

        val firstSlot = buildSlotForMachine(vm.vendingMachineId, firstSlotFoodItems)
        vm.addSlotToVendingMachine(firstSlot)
        SlotRepository.add(firstSlot)
        return vm
    }

    //Why? For coupling data update in vending machine list as well as slot repo.
    fun addSlotToVendingMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        val vm = getVendingMachineById(vendingMachineId)
        val slot = buildSlotForMachine(vendingMachineId, foodItems)
        vm.addSlotToVendingMachine(slot)
        SlotRepository.add(slot)
        return slot
    }

    //Why? Internal method to verify food items, and then creating the Slot
    private fun buildSlotForMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        validateFoodItems(foodItems)
        return Slot(vendingMachineId, foodItems.toMutableMap())
    }

    //Why? To avoid duplication by fetching data from repository once
    fun getVendingMachineById(vendingMachineId: String): VendingMachine =
        VendingMachineRepository.findById(vendingMachineId)

    //Why? To filter out unavailable products and display unique available products.
    fun viewAvailableProducts(vendingMachineId: String): Set<Food> {
        val vendingMachine = getVendingMachineById(vendingMachineId)

        return vendingMachine.getSlotsInVendingMachine()
            .flatMap { slot ->
                slot.getFoodItemsInSlot().entries
            }//for destructuring
            .filter { (_, quantity) ->
                quantity > 0
            }
            .map { (foodId, _) ->
                try {
                    FoodRepository.findById(foodId)
                } catch (_: VendingMachineException) {
                    throw CorruptedDataException("Vending machine, ID : $vendingMachineId contains unregistered food item, ID : $foodId ")
                }
            }
            .toSet()
    }

    //Why? For machine specific all stock-quantity review.
    fun viewAvailableQuantityForAllProducts(vendingMachineId: String): Map<String, Int> {
        val vm = getVendingMachineById(vendingMachineId)
        val result = mutableMapOf<String, Int>()
        for (slot in vm.getSlotsInVendingMachine()) {
            for ((foodId, qty) in slot.getFoodItemsInSlot()) {
                if (qty > 0) result[foodId] = (result[foodId] ?: 0) + qty
            }
        }
        return result
    }

    //Why? For machine specific single product-quantity review.
    fun getAvailableQuantityForOneProduct(vendingMachineId: String, foodId: String): Int {
        if (!FoodRepository.existsById(foodId)) {
            throw UnknownEntityException(foodId, "Food","Cannot check quantity for a product that does not exist")
        }
        val vm = getVendingMachineById(vendingMachineId)
        return vm.getSlotsInVendingMachine().sumOf { slot -> slot.getFoodItemsInSlot()[foodId] ?: 0 }
    }

    //Why? For validating before removing. Ensuring cascading deletion of slots and food items within, then and finally removing vending machine from Repo.
    fun removeVendingMachine(vendingMachineId: String) {
        if (!VendingMachineRepository.existsById(vendingMachineId)) {
            throw UnknownEntityException(vendingMachineId,"Vending machine")
        }
        SlotRepository.findByVendingMachineId(vendingMachineId).forEach {
            SlotRepository.removeById(it.slotId)
        }
        VendingMachineRepository.removeById(vendingMachineId)
    }

    //Why? For consistency and maintaining Controller->Service->Repository flow
    fun getAllVendingMachines(): Set<VendingMachine> = VendingMachineRepository.findAll()

    //Why? Internal method for validating food items. Assists buildSlotForMachine()
    private fun validateFoodItems(foodItems: Map<String, Int>) {
        require(!foodItems.isEmpty()){"A slot must contain at least one food item."}
        for ((foodId, qty) in foodItems) {
            require(foodId.isNotBlank()) {"Food ID in slot cannot be empty."}
            require(qty > 0) { "Quantity for food '$foodId' must be greater than zero."}
            if (!FoodRepository.existsById(foodId)) throw UnknownEntityException(foodId, "Food")
        }
    }
}