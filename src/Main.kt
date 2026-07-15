import ui.*
import java.util.Scanner
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val adminCaller = AdminUI()

    val consumerCaller = ConsumerUI()

    SampleData.load()

    fun validateAdmin(): Boolean{
        println("Enter the passcode")
        val codeInput: String = readln()
        return codeInput == "Aloha"
    }

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
        (2) Enter as a Customer
        (0) Exit
        
    """.trimIndent()
        )

        val option = readln().trim()
        when(option){
            "1" -> {
                if (validateAdmin()){
                    adminCaller.show()
                }else{
                    println("Invalid passcode, try again!")
                    continue
                }
            }
            "2" -> consumerCaller.show()
            "0" -> {running = false}
            else -> println("Invalid choice")
        }
    }



}