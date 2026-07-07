import ui.*
import java.util.Scanner

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val adminCaller = AdminUI(Scanner(System.`in`))
    adminCaller.show()
    val consumerCaller = ConsumerUI(Scanner(System.`in`))
    consumerCaller.show()
}