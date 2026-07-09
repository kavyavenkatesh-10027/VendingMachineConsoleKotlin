package model

import util.Gender
import java.time.LocalDate

abstract class User(
    val name: String,
    val dob: LocalDate,
    val gender: Gender
) {

    init {
        require(!name.isBlank()) { "Name cannot be empty" }
        require(!dob.isAfter(LocalDate.now())) {"Date of Birth must be on or before the current date" }
    }

    override fun toString(): String =
        "Name : $name\n" +
                "Date of Birth : $dob\n" +
                "Gender : $gender"
}