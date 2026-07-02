package service

import model.Drawer
import util.IndianCurrency
import util.VendingMachineException
import java.math.BigDecimal
import java.util.EnumMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.minus
import kotlin.plus

object CurrencyService {

    fun acceptPayment(drawer: Drawer, inserted: Map<IndianCurrency, Int>): BigDecimal {
        var total = BigDecimal.ZERO
        for ((denomination, count) in inserted) {  // destructuring map entries with for ((k, v) in $%map)
            if (count <= 0) throw VendingMachineException("Count cannot be zero or negative.")
            drawer.add(denomination, count)
            total += BigDecimal.valueOf(denomination.value.toLong()) * BigDecimal.valueOf(count.toLong())
        }
        return total
    }

    fun makeChange(drawer: Drawer, changeAmount: BigDecimal): Map<IndianCurrency, Int> {
        if (changeAmount < BigDecimal.ZERO) throw VendingMachineException("Change amount cannot be negative.")
        if (changeAmount == BigDecimal.ZERO) return emptyMap()

        val change = EnumMap<IndianCurrency, Int>(IndianCurrency::class.java)//enum requires a java class
        var remaining = changeAmount
        val denominations = IndianCurrency.values()

        for (i in denominations.indices.reversed()) {  // loop backwards
            if (remaining == BigDecimal.ZERO) break
            val denom = denominations[i]
            val denomValue = BigDecimal.valueOf(denom.value.toLong())
            val available = drawer.getCount(denom)
            val canUse = remaining.divideToIntegralValue(denomValue).toInt()
            val use = minOf(canUse, available)
            if (use > 0) {
                change[denom] = use
                remaining -= denomValue * BigDecimal.valueOf(use.toLong())
            }
        }

        if (remaining != BigDecimal.ZERO) {
            throw VendingMachineException("Machine cannot make exact change of Rs.$changeAmount.")
        }

        for ((denom, count) in change) {
            drawer.deduct(denom, count)
        }
        return change
    }

    fun refund(drawer: Drawer, inserted: Map<IndianCurrency, Int>) {
        if (inserted.isEmpty()) return
        for ((denom, count) in inserted) {
            drawer.deduct(denom, count)
        }
    }

    fun addToDrawer(drawer: Drawer, denomination: IndianCurrency, count: Int) {
        if (count <= 0) throw VendingMachineException("Count cannot be zero or negative.")
        drawer.add(denomination, count)
    }
}