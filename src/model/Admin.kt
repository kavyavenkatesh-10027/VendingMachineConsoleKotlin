package model

import model.enum.Gender
import generator.IDGenerator
import java.time.LocalDate

//The purpose of Admin is to represent admin, and it does this by child class (inherits User).
class Admin(
    name: String,
    dob: LocalDate,
    gender: Gender
) : User(name, dob, gender) {

    val adminId: String = IDGenerator.generateAdminId()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Admin) return false

        return adminId == other.adminId
    }

    override fun hashCode(): Int {
        return adminId.hashCode()
    }

    override fun toString(): String {
        return super.toString() + "\nAdmin ID : $adminId"
    }
}