package model

import util.Generator

class Slot(
    val vendingMachineId: String,
    foodItemsInSlot: MutableMap<String, Int>
) {
    val slotId = Generator.generateSlotId()
    val foodItemsInSlot: MutableMap<String, Int> = foodItemsInSlot.toMutableMap()// To prevent external mutation of the entry

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

    override fun toString(): String =
        """
    Slot ID                : $slotId
    Vending Machine ID     : $vendingMachineId
    Food Items:
    ${
            foodItemsInSlot.entries.joinToString("\n") {
                "  ${it.key} -> ${it.value}"
            }
        }
    """.trimIndent()
}