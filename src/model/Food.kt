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
    val ingredients: MutableList<String>,
    val expiryDate: LocalDate,
    val foodType: FoodType,
    warning: String? = null
) : Product(
    productName = productName,
    brand = brand,
    description = description,
    price = price,
    manufacturingLocation = manufacturingLocation,
    manufacturingDate = manufacturingDate,
    warning = warning
) {

    init {
        require(ingredients.isNotEmpty()) { "Ingredients must be provided" }
    }

    fun addIngredient(ingredient: String) {
        require(ingredient.isNotBlank())
        ingredients.add(ingredient)
    }

    fun removeIngredient(ingredient: String) {
        require(ingredient in ingredients)
        ingredients.remove(ingredient)
    }

    override fun toString(): String =
        """
    ${super.toString()}
    Food Type               : $foodType
    Category                : $vegOrNonVeg
    Ingredients             : ${ingredients.joinToString(", ")}
    Expiry Date             : $expiryDate
    """.trimIndent()
}