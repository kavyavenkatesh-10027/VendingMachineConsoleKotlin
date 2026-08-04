package exception

import java.math.BigDecimal

//The purpose of VendingMachineException is to clearly communicate the cause for the breakdown is , and it does this by inheriting Runtime.//done
//Why? To have a common type for all the exception for better readability and handling
abstract class VendingMachineException(message: String) : RuntimeException(message)


class AvailabilityRequirementException(message: String)  : VendingMachineException(message)

class InsufficientPaymentException(total : BigDecimal, amountPaid : BigDecimal)  : VendingMachineException(  
    "Insufficient payment. Total: Rs.$total, Paid: Rs.$amountPaid\nCollect refund from the inserting plate"
)

class InsufficientDenominationForChangeException(changeAmount: BigDecimal) : VendingMachineException(
    "Machine cannot make exact change of Rs.$changeAmount."
)

class UnknownEntityException(entityDetail : String, entity: String = "Entity", suggestion : String = "") : VendingMachineException(
    "$entity : $entityDetail does not exist. $suggestion"
)

class UnregisteredEntityException(item : String, itemId : String, container : String, containerId : String, suggestion : String = "")  : VendingMachineException(
    "$item : $itemId is not present in $container $containerId. $suggestion"
)

class IllegalNegativeValueException(valueName: String)  : VendingMachineException(
    "$valueName cannot be negative."
)

class ExistsAlreadyException(message: String)  : VendingMachineException(message)

class SlotVendingMachineMismatchException(slotId : String) : VendingMachineException(
    "Slot $slotId belongs to a different vending machine"
)

class CorruptedDataException(corruptedDataDetails: String)  : VendingMachineException(
    "Vending machine data has been corrupted. $corruptedDataDetails"
)

