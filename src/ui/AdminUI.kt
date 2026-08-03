package ui

import controller.AdminController
import exception.VendingMachineException
import model.Food
import model.Slot
import model.VendingMachine
import model.enum.*
import java.util.EnumMap

//The purpose of AdminUI is to get input from the admin and display the fetched data and status of the system in a human friendly manner, and it does this by using class that implements the model.enum(Interface Interactable).
class AdminUI() : Interactable {

    //Why? For looping the options till exit request
    fun show() {
        var running = true
        while (running) {
            println("\n========== ADMIN MENU ==========")
            println("1.  Create vending machine")
            println("2.  View vending machine")
            println("3.  Remove vending machine")
            println("4.  Add slot to vending machine")
            println("5.  Remove slot")
            println("6.  Register food item")
            println("7.  Remove food item")
            println("8.  Add food type to slot")
            println("9.  Refill food in slot")
            println("10. Edit food description")
            println("11. Edit food name")
            println("12. Edit food price")
            println("13. Edit food brand")
            println("14. Edit food warning")
            println("15. View all vending machines")
            println("16. View all food items")
            println("17. View product count at a machine")
            println("18. View cash drawer")
            println("19. Add cash to drawer")
            println("20. View purchase history")
            println("0.  Exit")
            println("=================================")

            try {
                when (prompt("Please enter your choice : ")) {
                    "1"  -> createVendingMachine()
                    "2"  -> viewVendingMachine()
                    "3"  -> removeVendingMachine()
                    "4"  -> addSlotToVendingMachine()
                    "5"  -> removeSlot()
                    "6"  -> registerFood()
                    "7"  -> removeFood()
                    "8"  -> addNewFoodTypeToSlot()
                    "9"  -> refillFoodInSlot()
                    "10" -> editFoodDescription()
                    "11" -> editFoodName()
                    "12" -> editFoodPrice()
                    "13" -> editFoodBrand()
                    "14" -> editFoodWarning()
                    "15" -> viewAllVendingMachines()
                    "16" -> viewAllFoods()
                    "17" -> viewProductCount()
                    "18" -> viewCashDrawer()
                    "19" -> addCashToDrawer()
                    "20" -> viewPurchaseHistory()
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

    //Why? Vending machine creation flow, UI and presentation, along with forward calls
    private fun createVendingMachine() {
        println("\n--- Create Vending Machine ---")
        val location = readEnum(Location::class.java, "Location")
        val establishedOn = readDate("Established on (yyyy-MM-dd): ")
        displayFoodMenu()
        val firstSlotFoodItems = readFoodItemsMap("first slot")
        val vm = AdminController.createVendingMachine(location, establishedOn, firstSlotFoodItems)
        println("\nVending machine created successfully!")
        println(vm)
    }

    //Why? UI and presentation, along with forward calls.
    private fun viewVendingMachine() {
        println("\n--- View Vending Machine ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID to view: ")
        val vm = AdminController.viewVendingMachine(vmId)
        println("\n $vm")
    }

    //Why? UI and presentation, along with forward calls.
    private fun removeVendingMachine() {
        println("\n--- Remove Vending Machine ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID to remove: ")
        AdminController.removeVendingMachine(vmId)
        println("Vending machine $vmId and all its slots have been removed.")
    }

    //Why? To maintain slot, UI and presentation, along with forward calls
    private fun addSlotToVendingMachine() {
        println("\n--- Add Slot to Vending Machine ---")
        displayVendingMachineMenu()
        val vendingMachineId = prompt("Vending machine ID: ")
        displayFoodMenu()
        val foodItems = readFoodItemsMap("new slot")
        val slot = AdminController.addSlotToVendingMachine(vendingMachineId, foodItems)
        println("\nSlot added successfully!")
        println(slot)
    }

    //Why? UI and presentation, along with forward calls
    private fun removeSlot() {
        println("\n--- Remove Slot ---")
        displaySlotMenu()
        val slotId = prompt("Slot ID to remove: ")
        AdminController.removeSlot(slotId)
        println("Slot $slotId removed.")
    }

    //Why? Validation and forward calling along with UI
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

        val food = AdminController.registerFood(
            productName, brand, description, warning,
            price, manufacturingLocation, manufacturingDate, vegOrNonVeg,
            ingredients, expiryDate, foodType
        )
        println("\nFood registered successfully!")
        println(food)
    }

    //Why? UI and presentation, along with forward calls
    private fun removeFood() {
        println("\n--- Remove Food Item ---")
        displayFoodMenu()
        val foodId = prompt("Food ID to remove: ")
        AdminController.removeFood(foodId)
        println("Food $foodId removed from registry and from all slots.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun addNewFoodTypeToSlot() {
        println("\n--- Add New Food Type to Slot ---")
        displaySlotMenu()
        val slotId = prompt("Slot ID: ")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val quantity = readInt("Quantity")
        AdminController.addNewFoodTypeToSlot(slotId, foodId, quantity)
        println("Food added to slot successfully.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun refillFoodInSlot() {
        println("\n--- Refill Food in Slot ---")
        displaySlotMenu()
        val slotId = prompt("Slot ID: ")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val quantity = readInt("Quantity to add")
        AdminController.refillFoodInSlot(slotId, foodId, quantity)
        println("Slot refilled successfully.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun editFoodDescription() {
        println("\n--- Edit Food Description ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newDescription = prompt("New description: ")
        AdminController.editFoodDescription(foodId, newDescription)
        println("Description updated.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun editFoodName() {
        println("\n--- Edit Food Name ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newName = prompt("New name: ")
        AdminController.editFoodName(foodId, newName)
        println("Name updated.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun editFoodPrice() {
        println("\n--- Edit Food Price ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newPrice = readBigDecimal("New price: ")
        AdminController.editFoodPrice(foodId, newPrice)
        println("Price updated.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun editFoodBrand() {
        println("\n--- Edit Food Brand ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        val newBrand = prompt("New brand: ")
        AdminController.editFoodBrand(foodId, newBrand)
        println("Brand updated.")
    }

    //Why? UI and presentation, along with forward calls.
    private fun editFoodWarning() {
        println("\n--- Edit Food Warning ---")
        displayFoodMenu()
        val foodId = prompt("Food ID: ")
        var newWarning: String? = prompt("New warning (press Enter to clear): ")
        if (newWarning!!.isEmpty()) newWarning = null
        AdminController.editFoodWarning(foodId, newWarning)
        println("Warning updated.")
    }

    //Why? UI and presentation.
    private fun viewAllVendingMachines() {
        val machines = AdminController.viewAllVendingMachines()
        if (machines.isEmpty()) { println("No vending machines registered yet."); return }
        println("\n===== All Vending Machines =====")
        machines.forEach { println("$it\n--------------------------------") }
    }

    //Why? UI and presentation.
    private fun viewAllFoods() {
        val foods = AdminController.getAllFoods()
        if (foods.isEmpty()) { println("No food items registered yet."); return }
        println("\n===== All Food Items =====")
        foods.forEach { println("$it\n-------------------------") }
    }

    //Why? UI and presentation.
    private fun viewProductCount() {
        println("\n--- Product Count at Machine ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID: ")
        val stockMap = AdminController.getProductCountForMachine(vmId)
        if (stockMap.isEmpty()) { println("No products currently stocked."); return }
        println("\n  %-14s %-22s %8s  %6s".format("Food ID", "Name", "Price", "Stock"))
        println("  ──────────────────────────────────────────────────")
        for ((foodId, qty) in stockMap) {
            val food = AdminController.getFoodById(foodId)
            println("  %-14s %-22s Rs.%-5s  %6d".format(food.productId, food.productName, food.price, qty))
        }
        val total = stockMap.values.sum()
        println("  Total units : $total")
    }

    //Why? UI and presentation.
    private fun viewCashDrawer() {
        println("\n--- View Cash Drawer ---")
        displayVendingMachineMenu()
        val vmId = prompt("Vending machine ID: ")
        println("\n===== Cash Drawer — $vmId =====")
        AdminController.getDenominationBreakdown(vmId).forEach { (denom, count) ->
            println("  Rs.%-4d  x  %d".format(denom.value, count))
        }
        println("  Total : Rs.${AdminController.getTotalCashInMachine(vmId)}")
    }

    //Why? Easy cash refilling (UX).
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

        AdminController.addCashToDrawer(vmId, denominations)
        println("\nCash added. Current drawer for $vmId:")
        AdminController.getDenominationBreakdown(vmId).forEach { (denom, count) ->
            println("  Rs.%-4d  x  %d".format(denom.value, count))
        }
        println("  Total : Rs.${AdminController.getTotalCashInMachine(vmId)}")
    }

    //Why? UI and presentation.
    private fun viewPurchaseHistory() {
        val purchases = AdminController.getAllPurchases()
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

    //Why? For UX, a short menu display for all vending machine, for easy selection for the user
    private fun displayVendingMachineMenu(){
        val allVendingMachine: Set<VendingMachine> = AdminController.viewAllVendingMachines()
        println("""
            
            -----Vending Machine Menu-----
            
        """.trimIndent())
        allVendingMachine.forEach {
            println("${it.vendingMachineId} | ${it.establishedOn} ")
        }
        println()
    }

    //Why? For UX, a short menu display for all vending machine, for easy selection for the user
    private fun displayFoodMenu(){
        val allFoods: Set<Food> = AdminController.getAllFoods()
        println("""
            
            -----Food Menu-----
            
        """.trimIndent())
        allFoods.forEach {
            println("${it.productId} | ${it.productName} | ${it.brand} | ${it.price}")
        }
        println()
    }

    //Why? For UX, a short menu display for all vending machine, for easy selection for the user
    private fun displaySlotMenu(){
        val allSlots: Set<Slot> = AdminController.getAllSlots()
        println("""
            
            -----Slot Menu-----
            
        """.trimIndent())
        allSlots.forEach {
            println("${it.slotId} | ${it.vendingMachineId} ")
        }
        println()
    }
}

