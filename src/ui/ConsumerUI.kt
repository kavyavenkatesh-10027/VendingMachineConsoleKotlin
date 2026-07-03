package ui

import controller.ConsumerController
import model.Purchase
import util.IndianCurrency
import util.Interactable
import util.VendingMachineException
import java.math.BigDecimal
import java.util.EnumMap
import java.util.Scanner
import kotlin.collections.set
import kotlin.plus

class ConsumerUI(private val scanner: Scanner) : Interactable {

    private val controller = ConsumerController()

    fun show() {
        var running = true
        while (running) {
            println("\n=====================================")
            println("  CUSTOMER MENU")
            println("=====================================")
            println("  1. View all vending machines")
            println("  2. Browse products at a machine")
            println("  3. Buy products")
            println("  0. Exit")
            println("=====================================")

            try {
                when (prompt("Your choice")) {
                    "1"  -> viewAllMachines()
                    "2"  -> viewProducts()
                    "3"  -> buyProducts()
                    "0"  -> running = false
                    else -> println("  Invalid choice, please enter a choice from 0–3.")
                }
            } catch (e: VendingMachineException) {
                println("\n  [!] ${e.message}")
            }
        }
        println("\n  Goodbye!\n")
    }

    private fun viewAllMachines() {
        val machines = controller.viewAllVendingMachines()
        println("\n=====================================")
        println("  VENDING MACHINES")
        println("=====================================")
        if (machines.isEmpty()) { println("  No machines available yet."); return }
        for (vm in machines) {
            println("  ID       : ${vm.vendingMachineId}")
            println("  Location : ${vm.vendingMachineLocation}")
            println("  Since    : ${vm.establishedOn}")
            println("  Slots    : ${vm.slotsInVendingMachine.size}")
            println("─────────────────────────────────────")
        }
    }

    private fun viewProducts() {
        val vmId = prompt("Vending machine ID")
        printAvailableProducts(vmId)
    }

    private fun printAvailableProducts(vmId: String) {
        val products = controller.viewAvailableProducts(vmId)
        println("\n=====================================")
        println("  AVAILABLE PRODUCTS")
        println("=====================================")
        if (products.isEmpty()) { println("  No products in stock at this machine."); return }
        println("  %-14s %-22s %8s  %6s  %s".format("Food ID", "Name", "Price", "Stock", "Type"))
        println("─────────────────────────────────────")
        for (food in products) {
            val qty = controller.getAvailableStock(vmId, food.productId)
            println("  %-14s %-22s Rs.%-5s  %6d  %s / %s".format(
                food.productId, food.productName, food.price, qty, food.foodType, food.vegOrNonVeg
            ))
        }
    }

    private fun buyProducts() {
        val vmId = prompt("Vending machine ID")
        val products = controller.viewAvailableProducts(vmId)
        if (products.isEmpty()) { println("\n  No products in stock at this machine."); return }

        printAvailableProducts(vmId)

        val cart = buildCart(vmId)
        if (cart.isEmpty()) { println("\n  Nothing added to cart. Returning to menu."); return }

        val total = controller.getCartTotal(cart)
        println("\n  Cart total : Rs.$total")

        val payment = collectPayment(total)
        if (payment.isEmpty()) { println("  Purchase cancelled — no payment received."); return }

        val purchase = controller.buyProducts(vmId, cart, payment)
        printReceipt(purchase)
    }

    private fun buildCart(vmId: String): Map<String, Int> {
        val cart = mutableMapOf<String, Int>()
        println("\n  Add items to cart (leave Food ID blank when done):")
        while (true) {
            val foodId = prompt("    Food ID")
            if (foodId.isBlank()) break

            val available = try {
                controller.getAvailableStock(vmId, foodId)
            } catch (e: VendingMachineException) {
                println("    [!] ${e.message}"); continue
            }

            if (available == 0) { println("    [!] That item is out of stock."); continue }

            val qty = readInt("    Quantity (available: $available)")
            if (qty > available) { println("    [!] Only $available units available."); continue }

            cart[foodId] = (cart[foodId] ?: 0) + qty
            println("    Added $qty × $foodId")
        }
        return cart
    }

    private fun collectPayment(totalRequired: BigDecimal): Map<IndianCurrency, Int> {
        val payment = EnumMap<IndianCurrency, Int>(IndianCurrency::class.java)
        var paid = BigDecimal.ZERO

        println("\n  Accepted denominations:")
        IndianCurrency.values().forEach { print("  ${it.name}(Rs.${it.value})") }
        println()
        println("  Type a denomination name to insert it, or DONE to cancel.\n")

        while (paid < totalRequired) {
            val remaining = totalRequired - paid
            println("  Paid: Rs.%-8s  Still needed: Rs.%s".format(paid, remaining))
            val input = prompt("  Insert").uppercase()

            if (input == "DONE") {
                println("  Payment cancelled.")
                return EnumMap(IndianCurrency::class.java)
            }

            try {
                val coin = IndianCurrency.valueOf(input)
                payment[coin] = (payment[coin] ?: 0) + 1
                paid += BigDecimal.valueOf(coin.value.toLong())
                println("  Accepted Rs.${coin.value}  |  Total inserted so far: Rs.$paid")
            } catch (e: IllegalArgumentException) {
                println("  [!] Unknown denomination. Try again.")
            }
        }

        return payment
    }

    private fun printReceipt(purchase: Purchase) {
        println("\n=====================================")
        println("              RECEIPT")
        println("=====================================")
        println("  Purchase ID : ${purchase.purchaseId}")
        println("  Time        : ${purchase.purchaseTime}")
        println("  Items       : ${purchase.quantityOfProductsPurchased}")
        println("  Total       : Rs.${purchase.totalAmount}")
        println("  Paid        : Rs.${purchase.moneyPaidByCustomer}")
        println("  Change      : Rs.${purchase.moneyToBeReturnedByVendingMachine}")
        println("=====================================")
        if (purchase.moneyToBeReturnedByVendingMachine > BigDecimal.ZERO) {
            println("  Please collect your change: Rs.${purchase.moneyToBeReturnedByVendingMachine}")
        }
        println("  Thank you for your purchase!")
        println("=====================================\n")
    }
}