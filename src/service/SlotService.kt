package service

import model.Slot
import repository.FoodRepository
import repository.SlotRepository
import repository.VendingMachineRepository
import util.VendingMachineException

object SlotService {

    fun getSlotById(slotId: String): Slot = SlotRepository.findById(slotId)

    fun addNewFoodTypeToSlot(slotId: String, foodId: String, quantity: Int) {
        val slot = getSlotById(slotId)
        if (!FoodRepository.existsById(foodId)) {
            throw VendingMachineException("Food with ID $foodId does not exist. Register the food first.")
        }
        if (slot.foodItemsInSlot.containsKey(foodId)) {
            throw VendingMachineException("Food $foodId is already in slot $slotId. Use refillFoodInSlot instead.")
        }
        slot.addNewFoodTypeToSlot(foodId, quantity)
    }

    fun removeFoodTypeFromSlot(foodId: String) {
        SlotRepository.findAll().forEach { slot ->
            if (slot.foodItemsInSlot.containsKey(foodId)) {
                slot.removeFoodTypeFromSlot(foodId)
            }
        }
    }

    fun refillFoodInSlot(slotId: String, foodId: String, quantity: Int) {
        val slot = getSlotById(slotId)
        if (!slot.foodItemsInSlot.containsKey(foodId)) {
            throw VendingMachineException("Food $foodId is not in slot $slotId. Use addNewFoodTypeToSlot instead.")
        }
        slot.addMoreOfFoodItemToSlot(foodId, quantity)
    }

    fun removeSlot(slotId: String) {
        val slot = getSlotById(slotId)
        VendingMachineRepository.findById(slot.vendingMachineId).removeSlotFromVendingMachine(slot)
        SlotRepository.removeById(slotId)
    }
}