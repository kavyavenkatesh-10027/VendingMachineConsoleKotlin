import ui.*
import java.util.Scanner
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val adminCaller = AdminUI()

    val consumerCaller = ConsumerUI()


    var running = true

    println("""
        --------------------------
        Welcome to Vending Machine
        --------------------------
        
    """.trimIndent())

    while (running) {
        println(
            """
        (1) Enter as an Admin
        (2) Buy a product
        (0) Exit
        
    """.trimIndent()
        )

        val option = readln().trim()
        when(option){
            "1" -> adminCaller.show()
            "2" -> consumerCaller.show()
            "0" -> {running = false}
            else -> println("Invalid choice")
        }
    }

}