package model

import util.*
import java.time.LocalDate

class VendingMachine(
    val vendingMachineLocation: Location,
    val establishedOn: LocalDate,
    private val slotsInVendingMachine: MutableList<Slot> = mutableListOf()
) {
    val vendingMachineId = Generator.generateVendingMachineId()
    val drawer = Drawer()

    fun getSlotsInVendingMachine(): List<Slot> {
        return slotsInVendingMachine.toList()
    }

    init {
        if (establishedOn > LocalDate.now()) throw VendingMachineException("Established date cannot be before the current date")
        slotsInVendingMachine.forEach { slot ->
            if (slot.vendingMachineId != vendingMachineId) {
                throw VendingMachineException("Slot ${slot.slotId} belongs to a different vending machine")
            }
        }
    }

    fun addSlotToVendingMachine(slot: Slot) {
        if (slot.vendingMachineId != vendingMachineId) {
            throw VendingMachineException("Slot belongs to a different vending machine")
        }
        slotsInVendingMachine.add(slot)
    }

    fun removeSlotFromVendingMachine(slot: Slot) {
        if (slot.vendingMachineId != vendingMachineId || !slotsInVendingMachine.contains(slot)) {
            throw VendingMachineException("Slot does not belong to this vending machine")
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