package model

import util.*
import java.math.BigDecimal
import java.time.LocalDate

class Food(
    productName: String,
    brand: String,
    description: String,
    price: BigDecimal,
    manufacturingLocation: Location,
    manufacturingDate: LocalDate,
    val vegOrNonVeg: VegNonVeg,
    private val ingredients: MutableList<String>,
    val expiryDate: LocalDate,
    val foodType: FoodType,
    warning: String? = null,
) : Product(
    productName = productName,
    brand = brand,
    description = description,
    price = price,
    manufacturingLocation = manufacturingLocation,
    manufacturingDate = manufacturingDate,
    warning = warning
) {

    fun getIngredients(): List<String> {
        return ingredients.toList() // Returns a read-only copy
    }

    init {
        if (ingredients.isEmpty()) throw VendingMachineException("Ingredients must be provided")
        if (!expiryDate.isAfter(LocalDate.now())) throw VendingMachineException("Cannot register an already-expired food item.")
        require(expiryDate.isAfter(manufacturingDate)) {"Expiry date must be after the manufacturing date."}
    }

    fun addIngredient(ingredient: String) {
        if (ingredient.isBlank()) throw VendingMachineException("A product cannot have a blank ingredient")
        ingredients.add(ingredient)
    }

    fun removeIngredient(ingredient: String) {
        if (ingredient !in ingredients) {
            throw VendingMachineException("Ingredient : $ingredient hasn't been listed for\n Food Id : $productId \n Food Name : $productName")
        }
        ingredients.remove(ingredient)
    }

    override fun toString(): String =
        super.toString() + "\n" +
                """
    Food Type               : $foodType
    Category                : $vegOrNonVeg
    Ingredients             : ${ingredients.joinToString(", ")}
    Expiry Date             : $expiryDate
    """.trimIndent()
}