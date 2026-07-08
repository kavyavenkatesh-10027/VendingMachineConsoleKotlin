package model

import util.Generator
import util.VendingMachineException

class Slot(
    val vendingMachineId: String,
    foodItemsInSlot: MutableMap<String, Int>
) {
    val slotId = Generator.generateSlotId()

    // Defensive copy so external mutation of the caller's map can't corrupt slot state.
    private val foodItemsInSlot: MutableMap<String, Int> = foodItemsInSlot.toMutableMap()

    fun getFoodItemsInSlot(): Map<String, Int> {
        return foodItemsInSlot.toMap() // Returns a read-only copy
    }

    init {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine cannot be blank")
        if (this.foodItemsInSlot.isEmpty()) throw VendingMachineException("Slot must have at least one food item")
    }

    fun addNewFoodTypeToSlot(foodId: String, quantity: Int) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID must not be left blank")
        if (quantity <= 0) throw VendingMachineException("Quantity must be greater than zero")
        foodItemsInSlot[foodId] = quantity
    }

    fun addMoreOfFoodItemToSlot(foodId: String, quantity: Int) {
        if (quantity <= 0) throw VendingMachineException("Quantity must be greater than zero")
        val current = foodItemsInSlot[foodId]
            ?: throw VendingMachineException("Food $foodId is not present in slot $slotId")
        foodItemsInSlot[foodId] = current + quantity
    }

    fun removeFoodItemFromSlot(foodId: String, quantity: Int) {
        if (quantity <= 0) throw VendingMachineException("Quantity must be greater than zero")
        val current = foodItemsInSlot[foodId]
            ?: throw VendingMachineException("Food $foodId is not present in slot $slotId")
        if (quantity > current) {
            throw VendingMachineException("Cannot remove $quantity of food $foodId; only $current present in slot $slotId")
        }
        foodItemsInSlot[foodId] = current - quantity
    }

    fun removeFoodTypeFromSlot(foodId: String) {
        foodItemsInSlot.remove(foodId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Slot) return false
        return slotId == other.slotId
    }

    override fun hashCode(): Int = slotId.hashCode()

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