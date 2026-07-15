package ui

import controller.AdminController
import model.Food
import model.VendingMachine
import util.*
import java.util.EnumMap

class AdminUI() : Interactable {

    private val controller = AdminController()

    fun show() {
        var running = true
        while (running) {
            println("\n========== ADMIN MENU ==========")
            println("1.  Create vending machine")
            println("2.  Remove vending machine")
            println("3.  Add slot to vending machine")
            println("4.  Remove slot")
            println("5.  Register food item")
            println("6.  Remove food item")
            println("7.  Add food type to slot")
            println("8.  Refill food in slot")
            println("9.  Edit food description")
            println("10. Edit food name")
            println("11. Edit food price")
            println("12. Edit food brand")
            println("13. Edit food warning")
            println("14. View all vending machines")
            println("15. View all food items")
            println("16. View product count at a machine")
            println("17. View cash drawer")
            println("18. Add cash to drawer")
            println("19. View purchase history")
            println("0.  Exit")
            println("=================================")

            try {
                when (prompt("Please enter your choice :")) {
                    "1"  -> createVendingMachine()
                    "2"  -> removeVendingMachine()
                    "3"  -> addSlotToVendingMachine()
                    "4"  -> removeSlot()
                    "5"  -> registerFood()
                    "6"  -> removeFood()
                    "7"  -> addNewFoodTypeToSlot()
                    "8"  -> refillFoodInSlot()
                    "9"  -> editFoodDescription()
                    "10" -> editFoodName()
                    "11" -> editFoodPrice()
                    "12" -> editFoodBrand()
                    "13" -> editFoodWarning()
                    "14" -> viewAllVendingMachines()
                    "15" -> viewAllFoods()
                    "16" -> viewProductCount()
                    "17" -> viewCashDrawer()
                    "18" -> addCashToDrawer()
                    "19" -> viewPurchaseHistory()
                    "0"  -> running = false
                    else -> println("Invalid choice. Please try again.")
                }
            } catch (e: VendingMachineException) {
                println("[Error] ${e.message}")
            } catch (e: IllegalArgumentException) {
                println("[Input Error] ${e.message}")
            }
        }
    }

    private fun createVendingMachine() {
        println("\n--- Create Vending Machine ---")
        val location = readEnum(Location::class.java, "Location")
        val establishedOn = readDate("Established on (yyyy-MM-dd): ")
        displayFoodMenu()
        val firstSlotFoodItems = readFoodItemsMap("first slot")
        val vm = controller.createVendingMachine(location, establishedOn, firstSlotFoodItems)
        println("\nVending machine created successfully!")
        println(vm)
    }

    private fun removeVendingMachine() {
        println("\n--- Remove Vending Machine ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID to remove: ")
        controller.removeVendingMachine(vmId)
        println("Vending machine $vmId and all its slots have been removed.")
    }

    private fun addSlotToVendingMachine() {
        println("\n--- Add Slot to Vending Machine ---")
        displayVendingMachineMenu()
        val vendingMachineId = prompt("Vending machine ID: ")
        displayFoodMenu()
        val foodItems = readFoodItemsMap("new slot")
        val slot = controller.addSlotToVendingMachine(vendingMachineId, foodItems)
        println("\nSlot added successfully!")
        println(slot)
    }

    private fun removeSlot() {
        println("\n--- Remove Slot ---")
        val slotId = prompt("Slot ID to remove: ")
        controller.removeSlot(slotId)
        println("Slot $slotId removed.")
    }

    private fun registerFood() {
        println("\n--- Register Food Item ---")
        val productName = prompt("Product name: ")
        val brand = prompt("Brand: ")
        val description = prompt("Description: ")
        var warning: String = prompt("Warning (press Enter to skip): ")
        if (warning.isEmpty()) warning = "- nil -"

        val price = readBigDecimal("Price: ")
        val manufacturingLocation = readEnum(Location::class.java, "Manufacturing location")
        val manufacturingDate = readDate("Manufacturing date (yyyy-MM-dd): ")
        val expiryDate = readDate("Expiry date (yyyy-MM-dd): ")
        val vegOrNonVeg = readEnum(VegNonVeg::class.java, "Veg / Non-veg")

        val ingredients = prompt("Ingredients (comma-separated): ").trim().split(",")

        val foodType = readEnum(FoodType::class.java, "Food type")

        val food = controller.registerFood(
            productName, brand, description, warning,
            price, manufacturingLocation, manufacturingDate, vegOrNonVeg,
            ingredients, expiryDate, foodType
        )
        println("\nFood registered successfully!")
        println(food)
    }

    private fun removeFood() {
        println("\n--- Remove Food Item ---")
        displayFoodMenu()
        val foodId = prompt("Food ID to remove: ")
        controller.removeFood(foodId)
        println("Food $foodId removed from registry and from all slots.")
    }

    private fun addNewFoodTypeToSlot() {
        println("\n--- Add New Food Type to Slot ---")
        val slotId = prompt("Slot ID: ")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val quantity = readInt("Quantity")
        controller.addNewFoodTypeToSlot(slotId, foodId, quantity)
        println("Food added to slot successfully.")
    }

    private fun refillFoodInSlot() {
        println("\n--- Refill Food in Slot ---")
        val slotId = prompt("Slot ID: ")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val quantity = readInt("Quantity to add")
        controller.refillFoodInSlot(slotId, foodId, quantity)
        println("Slot refilled successfully.")
    }

    private fun editFoodDescription() {
        println("\n--- Edit Food Description ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newDescription = prompt("New description: ")
        controller.editFoodDescription(foodId, newDescription)
        println("Description updated.")
    }

    private fun editFoodName() {
        println("\n--- Edit Food Name ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newName = prompt("New name: ")
        controller.editFoodName(foodId, newName)
        println("Name updated.")
    }

    private fun editFoodPrice() {
        println("\n--- Edit Food Price ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newPrice = readBigDecimal("New price: ")
        controller.editFoodPrice(foodId, newPrice)
        println("Price updated.")
    }

    private fun editFoodBrand() {
        println("\n--- Edit Food Brand ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newBrand = prompt("New brand: ")
        controller.editFoodBrand(foodId, newBrand)
        println("Brand updated.")
    }

    private fun editFoodWarning() {
        println("\n--- Edit Food Warning ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        var newWarning: String? = prompt("New warning (press Enter to clear): ")
        if (newWarning!!.isEmpty()) newWarning = null
        controller.editFoodWarning(foodId, newWarning)
        println("Warning updated.")
    }

    private fun viewAllVendingMachines() {
        val machines = controller.viewAllVendingMachines()
        if (machines.isEmpty()) { println("No vending machines registered yet."); return }
        println("\n===== All Vending Machines =====")
        machines.forEach { println("$it\n--------------------------------") }
    }

    private fun viewAllFoods() {
        val foods = controller.getAllFoods()
        if (foods.isEmpty()) { println("No food items registered yet."); return }
        println("\n===== All Food Items =====")
        foods.forEach { println("$it\n-------------------------") }
    }

    private fun viewProductCount() {
        println("\n--- Product Count at Machine ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID: ")
        val stockMap = controller.getProductCountForMachine(vmId)
        if (stockMap.isEmpty()) { println("No products currently stocked."); return }
        println("\n  %-14s %-22s %8s  %6s".format("Food ID", "Name", "Price", "Stock"))
        println("  ──────────────────────────────────────────────────")
        for ((foodId, qty) in stockMap) {
            val food = controller.getFoodById(foodId)
            println("  %-14s %-22s Rs.%-5s  %6d".format(food.productId, food.productName, food.price, qty))
        }
        val total = stockMap.values.sum()
        println("  Total units : $total")
    }

    private fun viewCashDrawer() {
        println("\n--- View Cash Drawer ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID: ")
        println("\n===== Cash Drawer — $vmId =====")
        controller.getDenominationBreakdown(vmId).forEach { (denom, count) ->
            println("  Rs.%-4d  x  %d".format(denom.value, count))
        }
        println("  Total : Rs.${controller.getTotalCashInMachine(vmId)}")
    }

    private fun addCashToDrawer() {
        println("\n--- Add Cash to Drawer ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID: ")
        val denominations = EnumMap<IndianCurrency, Int>(IndianCurrency::class.java)

        println("Enter how many of each denomination to add (Enter to skip):")
        for (denom in IndianCurrency.entries) {
            val input = prompt("  Rs.${denom.value}: ")
            if (input.isEmpty()) continue
            try {
                val count = input.toInt()
                if (count > 0) denominations[denom] = count
                else println("  Skipped — must be greater than zero.")
            } catch (_: NumberFormatException) {
                println("  Invalid input, skipping Rs.${denom.value}")
            }
        }

        if (denominations.isEmpty()) { println("Nothing added."); return }

        controller.addCashToDrawer(vmId, denominations)
        println("\nCash added. Current drawer for $vmId:")
        controller.getDenominationBreakdown(vmId).forEach { (denom, count) ->
            println("  Rs.%-4d  x  %d".format(denom.value, count))
        }
        println("  Total : Rs.${controller.getTotalCashInMachine(vmId)}")
    }

    private fun viewPurchaseHistory() {
        val purchases = controller.getAllPurchases()
        if (purchases.isEmpty()) { println("No purchases recorded yet."); return }
        println("\n===== Purchase History =====")
        for (p in purchases) {
            println("  ID     : ${p.purchaseId}")
            println("  Time   : ${p.purchaseTime}")
            println("  Items  : ${p.getQuantityOfProductsPurchased()}")
            println("  Total  : Rs.${p.totalAmount}")
            println("  Paid   : Rs.${p.moneyPaidByCustomer}")
            println("  Change : Rs.${p.moneyToBeReturnedByVendingMachine}")
            println("  ────────────────────────────")
        }
    }

    private fun displayVendingMachineMenu(){
        val allVendingMachine: Set<VendingMachine> = controller.viewAllVendingMachines()
        println("""
            
            -----Vending Machine Menu-----
            
        """.trimIndent())
        allVendingMachine.forEach {
            println("${it.vendingMachineId} | ${it.establishedOn} ")
        }
        println()
    }

    private fun displayFoodMenu(){
        val allFoods: Set<Food> = controller.getAllFoods()
        println("""
            
            -----Food Menu-----
            
        """.trimIndent())
        allFoods.forEach {
            println("${it.productId} | ${it.productName} | ${it.brand} | ${it.price}")
        }
        println()
    }
}

