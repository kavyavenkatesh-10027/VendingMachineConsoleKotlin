package model

import exception.AvailabilityRequirementException
import exception.UnregisteredEntityException
import generator.IDGenerator

//The purpose of Slot is to represent individual racks of a vending machine and do the necessary actions, and it does this by class.
class Slot(
    val vendingMachineId: String,
    foodItemsInSlot: MutableMap<String, Int>
) {
    val slotId = IDGenerator.generateSlotId()

    // Defensive copy so external mutation of the caller's map can't corrupt slot state.
    private val foodItemsInSlot: MutableMap<String, Int> = foodItemsInSlot.toMutableMap()

    //Why? For encapsulating and restricting modification of the collection
    fun getFoodItemsInSlot(): Map<String, Int> {
        return foodItemsInSlot.toMap()
    }

    init {
        require(!vendingMachineId.isBlank()) {"Vending machine cannot be blank"}
        require(this.foodItemsInSlot.isNotEmpty()) {"Slot must have at least one food item"}
        //Runs along with primary const
    }

    //Why? For validating and to safely add a new food type to the Slot
    fun addNewFoodTypeToSlot(foodId: String, quantity: Int) {
        require(foodId.isNotBlank()) {"Food ID must not be left blank"}
        require(quantity > 0) {"Quantity must be greater than zero" }
        foodItemsInSlot[foodId] = quantity
    }

    //Why? To validate before refilling
    fun addMoreOfFoodItemToSlot(foodId: String, quantity: Int) {
        require(quantity > 0) {"Quantity must be greater than zero"}
        val current = foodItemsInSlot[foodId]
            ?: throw UnregisteredEntityException("Food", foodId,  "Slot", slotId, "User 'Add New Food Type' instead")
        foodItemsInSlot[foodId] = current + quantity
    }

    //Why? To validate before removing a food item from slot
    fun removeFoodItemFromSlot(foodId: String, quantity: Int) {
        require(quantity > 0) {"Quantity must be greater than zero"}
        val current = foodItemsInSlot[foodId]
            ?: throw UnregisteredEntityException("Food", foodId,  "Slot", slotId)
        if (quantity > current) {
            throw AvailabilityRequirementException("Cannot remove $quantity of food $foodId; only $current present in slot $slotId")
        }
        foodItemsInSlot[foodId] = current - quantity
    }

    //Why? To validate the food type before collectively removing food items
    fun removeFoodTypeFromSlot(foodId: String) {
        if (!foodItemsInSlot.contains(foodId)) throw UnregisteredEntityException("Food", foodId,  "Slot", slotId)
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