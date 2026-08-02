package exception

//The purpose of VendingMachineException is to clearly communicate the cause for the breakdown is , and it does this by inheriting Runtime.//done
//Why? To have a common type for all the exception for better readability and handling
abstract class VendingMachineException(message: String) : RuntimeException(message)


class SupplyDemandException(message: String)  : VendingMachineException(message)

class InsufficientPaymentException(message: String)  : VendingMachineException(message)

class InsufficientDenominationForChangeException(message: String) : VendingMachineException(message)

class UnknownEntityException(message: String) : VendingMachineException(message)

class UnregisteredEntityException(message: String)  : VendingMachineException(message)

class IllegalNegativeValueException(message: String)  : VendingMachineException(message)

class ExistsAlreadyException(message: String)  : VendingMachineException(message)

class CorruptedDataException(message: String)  : VendingMachineException(message)

