package repository

import model.VendingMachine

//The purpose of VendingMachineRepository is to return vendingMachineId, and it does this by object(Singleton classes in Java) and inherits the BaseRepository for vending machine data handling.
object VendingMachineRepository : BaseRepository<VendingMachine>() {
    override fun getId(entity: VendingMachine) = entity.vendingMachineId
}