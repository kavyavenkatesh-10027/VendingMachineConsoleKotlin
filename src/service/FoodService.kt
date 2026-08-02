package service

import model.Food
import repository.FoodRepository
import model.enum.FoodType
import model.enum.Location
import model.enum.VegNonVeg
import java.math.BigDecimal
import java.time.LocalDate

//The purpose of FoodService is to do Crud for all Foods, and it does this by object (Singleton class in Java).
object FoodService {

    fun registerFood(
        productName: String, brand: String, description: String,
        warning: String?, price: BigDecimal, manufacturingLocation: Location,
        manufacturingDate: LocalDate, vegOrNonVeg: VegNonVeg,
        ingredients: List<String>, expiryDate: LocalDate, foodType: FoodType
    ): Food {
        val food = Food(
            productName = productName,
            brand = brand,
            description = description,
            price = price,
            manufacturingLocation = manufacturingLocation,
            manufacturingDate = manufacturingDate,
            vegOrNonVeg = vegOrNonVeg,
            ingredients = ingredients.toMutableList(),
            expiryDate = expiryDate,
            foodType = foodType,
            warning = warning
        )
        FoodRepository.add(food)
        return food
    }

    //Why? To avoid duplication by fetching data from repository once
    fun getFoodById(foodId: String): Food = FoodRepository.findById(foodId)

    //Why? For consistency and maintaining Controller->Service->Repository flow
    fun getAllFoods(): Set<Food> = FoodRepository.findAll()

    //Why? For completing the chain
    fun editDescription(foodId: String, newDescription: String) {
        getFoodById(foodId).description = newDescription
    }

    //Why? For completing the chain
    fun editName(foodId: String, newName: String) {
        getFoodById(foodId).productName = newName
    }

    //Why? For completing the chain
    fun editBrand(foodId: String, newBrand: String) {
        getFoodById(foodId).brand = newBrand
    }

    //Why? For completing the chain
    fun editPrice(foodId: String, newPrice: BigDecimal) {
        getFoodById(foodId).price = newPrice
    }

    //Why? For completing the chain
    fun editWarning(foodId: String, newWarning: String?) {
        getFoodById(foodId).warning = newWarning
    }

    //Why? For verification and completion of the chain without inconsistency. (Slot contains food, to maintain Slot-Food relationship)
    fun removeFood(foodId: String) {
        getFoodById(foodId)  // verify existence
        SlotService.removeFoodTypeFromSlot(foodId)
        FoodRepository.removeById(foodId)
    }
}