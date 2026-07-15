package controller

import model.Food
import model.Purchase
import model.Slot
import model.VendingMachine
import service.CurrencyService
import service.FoodService
import service.PurchaseService
import service.SlotService
import service.VendingMachineService
import util.FoodType
import util.IndianCurrency
import util.Location
import util.VegNonVeg
import java.math.BigDecimal
import java.time.LocalDate

class AdminController : BaseController() {

    fun createVendingMachine(
        location: Location,
        establishedOn: LocalDate,
        firstSlotFoodItems: Map<String, Int>
    ): VendingMachine {
        require(firstSlotFoodItems.isNotEmpty()) { "First slot must have at least one food item." }
        return VendingMachineService.createVendingMachine(location, establishedOn, firstSlotFoodItems)
    }

    fun addSlotToVendingMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be empty." }
        require(foodItems.isNotEmpty()) { "Slot must have at least one food item." }
        return VendingMachineService.addSlotToVendingMachine(vendingMachineId, foodItems)
    }

    fun registerFood(
        productName: String,
        brand: String,
        description: String,
        warning: String?,
        price: BigDecimal,
        manufacturingLocation: Location,
        manufacturingDate: LocalDate,
        vegOrNonVeg: VegNonVeg,
        ingredients: List<String>,
        expiryDate: LocalDate,
        foodType: FoodType
    ): Food {
        require(productName.isNotBlank()) { "Food name cannot be nempty." }
        require(brand.isNotBlank()) { "Brand cannot be empty." }
        require(description.isNotBlank()) { "Description cannot be empty." }
        require(price > BigDecimal.ZERO) { "Price cannot be zero or negative." }
        require(manufacturingDate <= LocalDate.now()) { "Manufacturing date cannot be in the future." }
        require(ingredients.isNotEmpty()) { "At least one ingredient must be provided." }

        return FoodService.registerFood(
            productName,
            brand,
            description,
            warning,
            price,
            manufacturingLocation,
            manufacturingDate,
            vegOrNonVeg,
            ingredients,
            expiryDate,
            foodType
        )
    }

    fun addNewFoodTypeToSlot(slotId: String, foodId: String, quantity: Int) {
        require(slotId.isNotBlank()) { "Slot ID cannot be empty." }
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(quantity > 0) { "Quantity must be greater than zero." }

        SlotService.addNewFoodTypeToSlot(slotId, foodId, quantity)
    }

    fun refillFoodInSlot(slotId: String, foodId: String, quantity: Int) {
        require(slotId.isNotBlank()) { "Slot ID cannot be empty." }
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(quantity > 0) { "Quantity must be greater than zero." }

        SlotService.refillFoodInSlot(slotId, foodId, quantity)
    }

    fun editFoodDescription(foodId: String, newDescription: String) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(newDescription.isNotBlank()) { "New description cannot be empty." }

        FoodService.editDescription(foodId, newDescription)
    }

    fun editFoodName(foodId: String, newName: String) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(newName.isNotBlank()) { "New name cannot be empty." }

        FoodService.editName(foodId, newName)
    }

    fun editFoodPrice(foodId: String, newPrice: BigDecimal) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(newPrice > BigDecimal.ZERO) { "Price cannot be zero or negative." }

        FoodService.editPrice(foodId, newPrice)
    }

    fun editFoodBrand(foodId: String, newBrand: String) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        require(newBrand.isNotBlank()) { "New brand cannot be empty." }

        FoodService.editBrand(foodId, newBrand)
    }

    fun editFoodWarning(foodId: String, newWarning: String?) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }
        // Warning can be blank

        FoodService.editWarning(foodId, newWarning)
    }

    fun removeVendingMachine(vendingMachineId: String) {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be empty." }

        VendingMachineService.removeVendingMachine(vendingMachineId)
    }

    fun getAllSlots(): Set<Slot> = SlotService.getAllSlots()

    fun removeSlot(slotId: String) {
        require(slotId.isNotBlank()) { "Slot ID cannot be empty." }

        SlotService.removeSlot(slotId)
    }

    fun removeFood(foodId: String) {
        require(foodId.isNotBlank()) { "Food ID cannot be empty." }

        FoodService.removeFood(foodId)
    }

    fun getAllFoods(): Set<Food> = FoodService.getAllFoods()

    fun getFoodById(foodId: String): Food {
        require(foodId.isNotBlank()) { "Product ID cannot be empty." }

        return FoodService.getFoodById(foodId)
    }

    fun getProductCountForMachine(vendingMachineId: String): Map<String, Int> =
        viewAvailableQuantityForAllProducts(vendingMachineId)

    fun addCashToDrawer(vendingMachineId: String, denominations: Map<IndianCurrency, Int>) {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be empty." }
        require(denominations.isNotEmpty()) { "Denomination map cannot be empty." }

        val vm = VendingMachineService.getVendingMachineById(vendingMachineId)

        for ((denom, count) in denominations) {
            CurrencyService.addToDrawer(vm.drawer, denom, count)
        }
    }

    fun getDenominationBreakdown(vendingMachineId: String): Map<IndianCurrency, Int> {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be empty." }

        return VendingMachineService.getVendingMachineById(vendingMachineId)
            .drawer
            .getDenominations()
    }

    fun getTotalCashInMachine(vendingMachineId: String): BigDecimal {
        require(vendingMachineId.isNotBlank()) { "Vending machine ID cannot be empty." }

        return VendingMachineService.getVendingMachineById(vendingMachineId)
            .drawer
            .totalCash()
    }

    fun getAllPurchases(): Set<Purchase> = PurchaseService.getAllPurchases()
}