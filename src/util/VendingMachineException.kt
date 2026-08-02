package util

//The purpose of VendingMachineException is to clearly communicate the cause for the breakdown is , and it does this by inheriting Runtime.//todo working on it
open class VendingMachineException(message: String) : RuntimeException(message)

class CurrencyHandlingException(message: String)  : VendingMachineException(message)

class PurchaseHandlingException(message: String)  : VendingMachineException(message)

class FoodHandlingException(message: String)  : VendingMachineException(message)

class SlotHandlingException(message: String)  : VendingMachineException(message)

class VMHandlingException(message: String)  : VendingMachineException(message)
//Service wise

//class EntityDoesNotExistException(message: String) : VendingMachineException(message)
//
//class InsufficientStockException(message: String) : VendingMachineException(message)
//
//class InsufficientCashInputException(message: String) : VendingMachineException(message)
//
//class InsufficientDenominationTypesException(message: String) : VendingMachineException(message)

//Utility wise
//Exceptions
