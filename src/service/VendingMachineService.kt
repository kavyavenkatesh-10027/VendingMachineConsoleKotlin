package service

import model.Food
import model.Slot
import model.VendingMachine
import repository.FoodRepository
import repository.SlotRepository
import repository.VendingMachineRepository
import util.Location
import util.VendingMachineException
import java.time.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

object VendingMachineService {

    fun createVendingMachine(
        location: Location,
        establishedOn: LocalDate,
        firstSlotFoodItems: Map<String, Int>
    ): VendingMachine {
        if (establishedOn.isAfter(LocalDate.now())) {
            throw VendingMachineException("Established date cannot be in the future.")
        }
        val vm = VendingMachine(location, establishedOn)
        VendingMachineRepository.add(vm)

        val firstSlot = buildSlotForMachine(vm.vendingMachineId, firstSlotFoodItems)
        vm.addSlotToVendingMachine(firstSlot)
        SlotRepository.add(firstSlot)
        return vm
    }

    fun addSlotToVendingMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        val vm = getVendingMachineById(vendingMachineId)
        val slot = buildSlotForMachine(vendingMachineId, foodItems)
        vm.addSlotToVendingMachine(slot)
        SlotRepository.add(slot)
        return slot
    }

    private fun buildSlotForMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        validateFoodItems(foodItems)
        return Slot(vendingMachineId, foodItems.toMutableMap())
    }

    fun getVendingMachineById(vendingMachineId: String): VendingMachine =
        VendingMachineRepository.findById(vendingMachineId)

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
                } catch (e: Exception) {
                    throw VendingMachineException("Vending machine data has been corrupted. $vendingMachineId contains unregistered food item, ID : $foodId ")
                }
            }
            .toSet()
    }

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

    fun getAvailableQuantityForOneProduct(vendingMachineId: String, foodId: String): Int {
        if (!FoodRepository.existsById(foodId)) {
            throw VendingMachineException("Cannot check quantity for a product that does not exist")
        }
        val vm = getVendingMachineById(vendingMachineId)
        return vm.getSlotsInVendingMachine().sumOf { slot -> slot.getFoodItemsInSlot()[foodId] ?: 0 }
    }

    fun removeVendingMachine(vendingMachineId: String) {
        if (!VendingMachineRepository.existsById(vendingMachineId)) {
            throw VendingMachineException("Vending machine with ID $vendingMachineId does not exist")
        }
        SlotRepository.findByVendingMachineId(vendingMachineId).forEach {
            SlotRepository.removeById(it.slotId)
        }
        VendingMachineRepository.removeById(vendingMachineId)
    }

    fun getAllVendingMachines(): Set<VendingMachine> = VendingMachineRepository.findAll()

    private fun validateFoodItems(foodItems: Map<String, Int>) {
        if (foodItems.isEmpty()) throw VendingMachineException("A slot must contain at least one food item.")
        for ((foodId, qty) in foodItems) {
            if (foodId.isBlank()) throw VendingMachineException("Food ID in slot cannot be null or empty.")
            if (!FoodRepository.existsById(foodId)) throw VendingMachineException("No food of ID: $foodId has been registered")
            if (qty <= 0) throw VendingMachineException("Quantity for food '$foodId' must be greater than zero.")
        }
    }
}