package repository

import model.Slot

object SlotRepository : BaseRepository<Slot>() {

    private val slotsInEveryMachine = mutableMapOf<String, MutableList<Slot>>()

    override fun getId(entity: Slot) = entity.slotId

    override fun add(entity: Slot) {
        super.add(entity)
        slotsInEveryMachine.getOrPut(entity.vendingMachineId) { mutableListOf() }.add(entity)
    }

    override fun removeById(id: String) {
        val slotToRemove = findById(id)
        slotsInEveryMachine[slotToRemove.vendingMachineId]?.remove(slotToRemove)
        super.removeById(id)
    }

    fun findByVendingMachineId(vendingMachineId: String): List<Slot> {
        return slotsInEveryMachine[vendingMachineId]?.toList() ?: emptyList()
    }
}
