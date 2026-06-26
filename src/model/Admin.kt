package model

import util.Gender
import util.Generator
import java.time.LocalDate

class Admin(
    name: String,
    dob: LocalDate,
    gender: Gender
) : User(name, dob, gender) {

    val adminId: String = Generator.generateAdminId()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Admin
        return adminId == other.adminId
    }

    override fun hashCode(): Int {
        return adminId.hashCode()
    }

    override fun toString(): String {
        return super.toString() + "\nAdmin ID : $adminId"
    }
}