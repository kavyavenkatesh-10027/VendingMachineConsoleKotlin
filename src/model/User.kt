package model

import util.Gender
import util.VendingMachineException
import java.time.LocalDate

abstract class User(
    val name: String,
    val dob: LocalDate,
    val gender: Gender
) {

    init {
        if (name.isBlank()) {
            throw VendingMachineException("Name cannot be empty")
        }

        if (dob.isAfter(LocalDate.now())) {
            throw VendingMachineException("Date of Birth must be on or before the current date")
        }
    }

    override fun toString(): String =
        "Name : $name\n" +
                "Date of Birth : $dob\n" +
                "Gender : $gender"
}