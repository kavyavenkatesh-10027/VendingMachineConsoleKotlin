package service

import exception.ExistsAlreadyException
import exception.UnknownEntityException
import exception.UnregisteredEntityException
import model.Slot
import repository.FoodRepository
import repository.SlotRepository
import repository.VendingMachineRepository

//The purpose of SlotService is to do Crud in slots , and it does this by object (Singleton class in Java).
object SlotService {

    //Why? To avoid duplication by fetching data from repository once
    fun getSlotById(slotId: String): Slot = SlotRepository.findById(slotId)

    //Why? For consistency and maintaining Controller->Service->Repository flow
    fun getAllSlots() : Set<Slot> = SlotRepository.findAll()

    //Why? To validating and to safely add a new food type to the Slot
    fun addNewFoodTypeToSlot(slotId: String, foodId: String, quantity: Int) {
        val slot = getSlotById(slotId)
        if (!FoodRepository.existsById(foodId)) {
            throw UnknownEntityException(foodId, "Food","Register the food first.")
        }
        if (slot.getFoodItemsInSlot().containsKey(foodId)) {
            throw ExistsAlreadyException("Food : $foodId already exists. Use refillFoodInSlot instead.")
        }
        slot.addNewFoodTypeToSlot(foodId, quantity)
    }

    //Why? To validate the food type before collectively removing food items
    fun removeFoodTypeFromSlot(foodId: String) {
        SlotRepository.findAll().forEach { slot ->
            if (slot.getFoodItemsInSlot().containsKey(foodId)) {
                slot.removeFoodTypeFromSlot(foodId)
            }
        }
    }

    //Why? To ensure that the food actually exists in slot
    fun refillFoodInSlot(slotId: String, foodId: String, quantity: Int) {
        val slot = getSlotById(slotId)
        if (!slot.getFoodItemsInSlot().containsKey(foodId)) {
            throw UnregisteredEntityException("Food", foodId, "Slot", slotId,"Use 'Add New Food Type To Slot' instead.")
        }
        slot.addMoreOfFoodItemToSlot(foodId, quantity)//Validation in model class
    }

    //Why? To maintain the flow and ensure removal from both the repos.
    fun removeSlot(slotId: String) {
        val slot = getSlotById(slotId)
        VendingMachineRepository.findById(slot.vendingMachineId).removeSlotFromVendingMachine(slot)
        SlotRepository.removeById(slotId)
    }
}