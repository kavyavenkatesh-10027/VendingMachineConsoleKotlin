package repository

import model.*

object AdminRepository : BaseRepository<Admin>() {
    override fun getId(entity: Admin) = entity.adminId
}

object FoodRepository : BaseRepository<Food>() {
    override fun getId(entity: Food) = entity.productId
}

object PurchaseRepository : BaseRepository<Purchase>() {
    override fun getId(entity: Purchase) = entity.purchaseId
}

object VendingMachineRepository : BaseRepository<VendingMachine>() {
    override fun getId(entity: VendingMachine) = entity.vendingMachineId
}

object SlotRepository : BaseRepository<Slot>() {

    private val slotsInEveryMachine = mutableMapOf<String, MutableList<Slot>>()

    override fun getId(entity: Slot) = entity.slotId

    override fun add(entity: Slot) {
        super.add(entity)
        slotsInEveryMachine.getOrPut(entity.vendingMachineId) { mutableListOf() }.add(entity)
    }

    fun findByVendingMachineId(vendingMachineId: String): List<Slot> {
        return slotsInEveryMachine[vendingMachineId]?.toList() ?: emptyList()
    }
}
