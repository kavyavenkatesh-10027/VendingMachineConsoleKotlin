package repository

import model.Slot

//The purpose of SlotRepository is to return slotId and override the parent method for unique implementation, and it does this by object(Singleton classes in Java) and inherits the BaseRepository for common, non-specific Slot data handling.
object SlotRepository : BaseRepository<Slot>() {

    private val slotsInEveryMachine = mutableMapOf<String, MutableList<Slot>>()

    //Why? To avoid duplication
    override fun getId(entity: Slot) = entity.slotId

    //Why? To maintain slot-machine relation
    override fun add(entity: Slot) {
        super.add(entity)
        slotsInEveryMachine.getOrPut(entity.vendingMachineId) { mutableListOf() }.add(entity)
    }

    //Why? To avoid inconsistency in slot-machine data on delete
    override fun removeById(id: String) {
        val slotToRemove = findById(id)
        slotsInEveryMachine[slotToRemove.vendingMachineId]?.remove(slotToRemove)
        super.removeById(id)
    }

    //Why? To filter slot by their container, which is unique to slot entity only
    fun findByVendingMachineId(vendingMachineId: String): List<Slot> {
        return slotsInEveryMachine[vendingMachineId]?.toList() ?: emptyList()
    }
}
