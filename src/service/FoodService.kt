package service

import model.Food
import repository.FoodRepository
import util.FoodType
import util.Location
import util.VegNonVeg
import java.math.BigDecimal
import java.time.LocalDate

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

    fun getFoodById(foodId: String): Food = FoodRepository.findById(foodId)

    fun getAllFoods(): Set<Food> = FoodRepository.findAll()

    fun editDescription(foodId: String, newDescription: String) {
        getFoodById(foodId).description = newDescription
    }

    fun editName(foodId: String, newName: String) {
        getFoodById(foodId).productName = newName
    }

    fun editBrand(foodId: String, newBrand: String) {
        getFoodById(foodId).brand = newBrand
    }

    fun editPrice(foodId: String, newPrice: BigDecimal) {
        getFoodById(foodId).price = newPrice
    }

    fun editWarning(foodId: String, newWarning: String?) {
        getFoodById(foodId).warning = newWarning
    }

    fun removeFood(foodId: String) {
        getFoodById(foodId)  // verify exists
        SlotService.removeFoodTypeFromSlot(foodId)
        FoodRepository.removeById(foodId)
    }
}