package model

import util.IndianCurrency
import util.VendingMachineException
import java.math.BigDecimal
import java.util.EnumMap

class Drawer {

    private val denominations = EnumMap<IndianCurrency, Int>(IndianCurrency::class.java)

    init {
        for (denomination in IndianCurrency.entries) {
            denominations[denomination] = 0
        }
    }

    fun getCount(denomination: IndianCurrency): Int {
        return denominations[denomination] ?: 0
    }

    fun add(denomination: IndianCurrency, count: Int) {
        if (count <= 0) throw VendingMachineException("Count must be greater than zero.")

        denominations[denomination] = getCount(denomination) + count
    }

    fun deduct(denomination: IndianCurrency, count: Int) {
        val current = getCount(denomination)

        if (count <= 0) throw VendingMachineException("Entered value must be greater than zero.")
        if (count > current) {
            throw VendingMachineException("Insufficient denomination to deduct.")
        }

        denominations[denomination] = current - count
    }

    fun getDenominations(): Map<IndianCurrency, Int> {
        return denominations.toMap() // Returns a read-only copy
    }

    fun totalCash(): BigDecimal {
        var total = BigDecimal.ZERO

        for ((denomination, count) in denominations) {
            val denominationValue = BigDecimal.valueOf(denomination.value.toLong())
            total = total.add(
                denominationValue.multiply(BigDecimal.valueOf(count.toLong()))
            )
        }

        return total
    }

    override fun toString(): String =
        """
    Drawer
    ------
    ${
            denominations.entries.joinToString("\n") {
                "${it.key} : ${it.value}"
            }
        }
    Total Cash : ₹${totalCash()}
    """.trimIndent()
}