package util

open class VendingMachineException(message: String) : RuntimeException(message)

class CurrencyHandlingException(message: String)  : VendingMachineException(message)

class PurchaseHandlingException(message: String)  : VendingMachineException(message)

class FoodHandlingException(message: String)  : VendingMachineException(message)

class SlotHandlingException(message: String)  : VendingMachineException(message)

class VMHandlingException(message: String)  : VendingMachineException(message)
//Service wise

class EntityDoesNotExistException(message: String) : VendingMachineException(message)

class InsufficientStockException(message: String) : VendingMachineException(message)

class InsufficientCashInputException(message: String) : VendingMachineException(message)

class InsufficientDenominationTypesException(message: String) : VendingMachineException(message)

//Utility wise
//Exceptions
