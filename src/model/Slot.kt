package model

import util.Generator

class Slot(
    val vendingMachineId: String,
    val foodItemsInSlot: MutableMap<String, Int>
) {
    val slotId = Generator.generateSlotId()

    init {
        require(vendingMachineId.isNotBlank())
        require(foodItemsInSlot.isNotEmpty())
    }

    fun addNewFoodTypeToSlot(foodId: String, quantity: Int) {
        require(foodId.isNotBlank())
        require(quantity > 0)
        foodItemsInSlot[foodId] = quantity
    }

    fun addMoreOfFoodItemToSlot(foodId: String, quantity: Int) {
        foodItemsInSlot[foodId]?.let {
            foodItemsInSlot[foodId] = it + quantity
        }
    }

    fun removeFoodItemFromSlot(foodId: String, quantity: Int) {
        foodItemsInSlot[foodId]?.let {
            foodItemsInSlot[foodId] = it - quantity
        }
    }

    fun removeFoodTypeFromSlot(foodId: String) {
        foodItemsInSlot.remove(foodId)
    }
}