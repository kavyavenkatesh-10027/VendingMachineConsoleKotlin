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
import util.VendingMachineException
import java.math.BigDecimal
import java.time.LocalDate

class AdminController : BaseController() {

    fun createVendingMachine(
        location: Location,
        establishedOn: LocalDate,
        firstSlotFoodItems: Map<String, Int>
    ): VendingMachine {
        if (firstSlotFoodItems.isEmpty()) throw VendingMachineException("First slot must have at least one food item.")
        return VendingMachineService.createVendingMachine(location, establishedOn, firstSlotFoodItems)
    }

    fun addSlotToVendingMachine(vendingMachineId: String, foodItems: Map<String, Int>): Slot {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        if (foodItems.isEmpty()) throw VendingMachineException("Slot must have at least one food item.")
        return VendingMachineService.addSlotToVendingMachine(vendingMachineId, foodItems)
    }

    fun registerFood(
        productName: String, brand: String, description: String,
        warning: String?, price: BigDecimal, manufacturingLocation: Location,
        manufacturingDate: LocalDate, vegOrNonVeg: VegNonVeg,
        ingredients: List<String>, expiryDate: LocalDate, foodType: FoodType
    ): Food {
        if (productName.isBlank()) throw VendingMachineException("Food name cannot be null or empty.")
        if (brand.isBlank()) throw VendingMachineException("Brand cannot be null or empty.")
        if (description.isBlank()) throw VendingMachineException("Description cannot be null or empty.")
        if (price <= BigDecimal.ZERO) throw VendingMachineException("Price cannot be zero or negative.")
        if (manufacturingDate.isAfter(LocalDate.now())) throw VendingMachineException("Manufacturing date cannot be in the future.")
        if (ingredients.isEmpty()) throw VendingMachineException("At least one ingredient must be provided.")
        return FoodService.registerFood(
            productName, brand, description, warning, price,
            manufacturingLocation, manufacturingDate, vegOrNonVeg,
            ingredients, expiryDate, foodType
        )
    }

    fun addNewFoodTypeToSlot(slotId: String, foodId: String, quantity: Int) {
        if (slotId.isBlank()) throw VendingMachineException("Slot ID cannot be null or empty.")
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (quantity <= 0) throw VendingMachineException("Quantity must be greater than zero.")
        SlotService.addNewFoodTypeToSlot(slotId, foodId, quantity)
    }

    fun refillFoodInSlot(slotId: String, foodId: String, quantity: Int) {
        if (slotId.isBlank()) throw VendingMachineException("Slot ID cannot be null or empty.")
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (quantity <= 0) throw VendingMachineException("Quantity must be greater than zero.")
        SlotService.refillFoodInSlot(slotId, foodId, quantity)
    }

    fun editFoodDescription(foodId: String, newDescription: String) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (newDescription.isBlank()) throw VendingMachineException("New description cannot be null or empty.")
        FoodService.editDescription(foodId, newDescription)
    }

    fun editFoodName(foodId: String, newName: String) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (newName.isBlank()) throw VendingMachineException("New name cannot be null or empty.")
        FoodService.editName(foodId, newName)
    }

    fun editFoodPrice(foodId: String, newPrice: BigDecimal) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (newPrice <= BigDecimal.ZERO) throw VendingMachineException("Price cannot be zero or negative.")
        FoodService.editPrice(foodId, newPrice)
    }

    fun editFoodBrand(foodId: String, newBrand: String) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        if (newBrand.isBlank()) throw VendingMachineException("New brand cannot be null or empty.")
        FoodService.editBrand(foodId, newBrand)
    }

    fun editFoodWarning(foodId: String, newWarning: String?) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        //Warning can be blank, therefore no issues
        FoodService.editWarning(foodId, newWarning)
    }

    fun removeVendingMachine(vendingMachineId: String) {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        VendingMachineService.removeVendingMachine(vendingMachineId)
    }

    fun removeSlot(slotId: String) {
        if (slotId.isBlank()) throw VendingMachineException("Slot ID cannot be null or empty.")
        SlotService.removeSlot(slotId)
    }

    fun removeFood(foodId: String) {
        if (foodId.isBlank()) throw VendingMachineException("Food ID cannot be null or empty.")
        FoodService.removeFood(foodId)
    }

    fun getAllFoods(): Set<Food> = FoodService.getAllFoods()

    fun getFoodById(foodId: String): Food {
        if (foodId.isBlank()) throw VendingMachineException("Product ID cannot be null or empty")
        return FoodService.getFoodById(foodId)
    }

    fun getProductCountForMachine(vendingMachineId: String): Map<String, Int> {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return viewAvailableQuantityForAllProducts(vendingMachineId)
    }

    fun addCashToDrawer(vendingMachineId: String, denominations: Map<IndianCurrency, Int>) {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        if (denominations.isEmpty()) throw VendingMachineException("Denomination map cannot be null or empty.")
        val vm = VendingMachineService.getVendingMachineById(vendingMachineId)
        for ((denom, count) in denominations) {
            CurrencyService.addToDrawer(vm.drawer, denom, count)
        }
    }

    fun getDenominationBreakdown(vendingMachineId: String): Map<IndianCurrency, Int> {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return VendingMachineService.getVendingMachineById(vendingMachineId).drawer.getDenominations()
    }

    fun getTotalCashInMachine(vendingMachineId: String): BigDecimal {
        if (vendingMachineId.isBlank()) throw VendingMachineException("Vending machine ID cannot be null or empty.")
        return VendingMachineService.getVendingMachineById(vendingMachineId).drawer.totalCash()
    }

    fun getAllPurchases(): Set<Purchase> = PurchaseService.getAllPurchases()
}