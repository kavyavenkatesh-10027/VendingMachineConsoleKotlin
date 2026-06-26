package util

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.Scanner

interface Interactable {

    companion object {
        val scanner = Scanner(System.`in`)
    }

    fun prompt(label: String): String {
        print(label)
        return scanner.nextLine().trim()
    }

    fun readInt(prompt: String): Int {
        while (true) {
            print("$prompt : ")
            try {
                val value = scanner.nextLine().trim().toInt()
                if (value > 0) {
                    return value
                }
                println("Please enter a number greater than zero.")
            } catch (_: NumberFormatException) {
                println("Invalid number. Please enter a whole number greater than zero.")
            }
        }
    }

    fun readFoodItemsMap(context: String): MutableMap<String, Int> {
        val foodItems = mutableMapOf<String, Int>()

        println("Enter food items for the $context (blank food ID to stop):")

        while (true) {
            print("  Food ID: ")
            val foodId = scanner.nextLine().trim()

            if (foodId.isEmpty()) {
                if (foodItems.isEmpty()) {
                    println("  At least one food item is required. Try again.")
                    continue
                }
                break
            }

            val qty = readInt("Quantity")
            foodItems[foodId] = (foodItems[foodId] ?: 0) + qty
        }

        return foodItems
    }

    fun readDate(prompt: String): LocalDate {
        while (true) {
            print(prompt)
            try {
                return LocalDate.parse(scanner.nextLine().trim())
            } catch (_: DateTimeParseException) {
                println("Invalid date. Please use the format yyyy-MM-dd.")
            }
        }
    }

    fun readBigDecimal(prompt: String): BigDecimal {
        while (true) {
            print(prompt)
            try {
                val value = BigDecimal(scanner.nextLine().trim())

                if (value <= BigDecimal.ZERO) {
                    println("Please enter a number greater than zero.")
                    continue
                }

                return value
            } catch (_: NumberFormatException) {
                println("Invalid number. Please enter a number greater than zero.")
            }
        }
    }

    fun <T : Enum<T>> readEnum(clazz: Class<T>, label: String): T {
        val constants = clazz.enumConstants

        println("$label options:")
        constants.forEachIndexed { index, value ->
            println("  ${index + 1}. $value")
        }

        while (true) {
            print("Choose (1-${constants.size}): ")

            try {
                val choice = scanner.nextLine().trim().toInt()
                if (choice in 1..constants.size) {
                    return constants[choice - 1]
                }
            } catch (_: NumberFormatException) {
            }

            println("Invalid choice.")
        }
    }
}