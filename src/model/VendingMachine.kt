package model

import exception.CorruptedDataException
import exception.UnregisteredEntityException
import generator.IDGenerator
import model.enum.*
import java.time.LocalDate

//The purpose of VendingMachine is to represent a real-world vending machine and has methods to refactor itself, and it does this by using class.
class VendingMachine(
    val vendingMachineLocation: Location,
    val establishedOn: LocalDate,
    private val slotsInVendingMachine: MutableList<Slot> = mutableListOf()
) {
    val vendingMachineId = IDGenerator.generateVendingMachineId()
    val drawer = Drawer()

    //Why? For encapsulating and restricting modification of the collection (Slot is mutable)
    fun getSlotsInVendingMachine(): List<Slot> {
        return slotsInVendingMachine.toList()
    }

    init {
        require(establishedOn <= LocalDate.now()) {"Established date must be on or before the current date"}
        slotsInVendingMachine.forEach { slot ->
            if (slot.vendingMachineId != vendingMachineId) {
                throw CorruptedDataException("Slot ${slot.slotId} belongs to a different vending machine")
            }
        }
        //Runs along with primary const
    }

    fun addSlotToVendingMachine(slot: Slot) {
        if (slot.vendingMachineId != vendingMachineId) {
            throw CorruptedDataException("Slot belongs to a different vending machine")
        }
        slotsInVendingMachine.add(slot)
    }

    fun removeSlotFromVendingMachine(slot: Slot) {
        if (slot.vendingMachineId != vendingMachineId || !slotsInVendingMachine.contains(slot)) {
            throw UnregisteredEntityException("Slot does not belong to this vending machine")
        }
        slotsInVendingMachine.remove(slot)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VendingMachine) return false
        return vendingMachineId == other.vendingMachineId
    }

    override fun hashCode(): Int = vendingMachineId.hashCode()

    override fun toString(): String =
        """
    Vending Machine ID      : $vendingMachineId
    Location                : $vendingMachineLocation
    Established On          : $establishedOn
    Number of Slots         : ${slotsInVendingMachine.size}
    Cash Available          : ₹${drawer.totalCash()}
    """.trimIndent()
}