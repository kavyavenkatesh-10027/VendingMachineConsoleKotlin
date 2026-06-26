package model

import util.*
import java.time.LocalDate

class VendingMachine(
    val vendingMachineLocation: Location,
    val establishedOn: LocalDate,
    val slotsInVendingMachine: MutableList<Slot>
) {
    val vendingMachineId = Generator.generateVendingMachineId()
    val drawer = Drawer()

    init {
        require(establishedOn <= LocalDate.now())
    }

    fun addSlotToVendingMachine(slot: Slot) {
        require(slot.vendingMachineId == vendingMachineId) {
            "Slot belongs to a different vending machine"
        }
        slotsInVendingMachine.add(slot)
    }

    fun removeSlotFromVendingMachine(slot: Slot) {
        slotsInVendingMachine.remove(slot)
    }
}