package org.acmebank.katas.bankaccount

import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer
import TransactionType._

object Account {
  def apply(): Account = Account(s"ACCOUNT-${System.currentTimeMillis()}", Money.zero, List.empty)
  def apply(accountId: String): Account = Account(accountId, Money.zero, List.empty)
  def apply(initialBalance: Money): Account = {
    val accountId = s"ACCOUNT-${System.currentTimeMillis()}"
    val transactions = if (initialBalance != Money.zero) {
      List(Transaction(LocalDateTime.now(), initialBalance, DEPOSIT, initialBalance, "Initial deposit"))
    } else {
      List.empty
    }
    Account(accountId, initialBalance, transactions)
  }
  def apply(accountId: String, initialBalance: Money): Account = {
    val transactions = if (initialBalance != Money.zero) {
      List(Transaction(LocalDateTime.now(), initialBalance, DEPOSIT, initialBalance, "Initial deposit"))
    } else {
      List.empty
    }
    Account(accountId, initialBalance, transactions)
  }
}

case class Account(accountId: String, balance: Money, transactions: List[Transaction]) {
  
  def deposit(amount: Money): Account = {
    require(amount != null && !amount.isNegative, "Deposit amount must be positive")
    val newBalance = balance + amount
    val transaction = Transaction(LocalDateTime.now(), amount, DEPOSIT, newBalance)
    copy(balance = newBalance, transactions = transactions :+ transaction)
  }

  def withdraw(amount: Money): Account = {
    require(amount != null && !amount.isNegative, "Withdrawal amount must be positive")
    require(balance >= amount, "Insufficient funds for withdrawal")
    val newBalance = balance - amount
    val transaction = Transaction(LocalDateTime.now(), amount, WITHDRAWAL, newBalance)
    copy(balance = newBalance, transactions = transactions :+ transaction)
  }

  def transferTo(toAccount: Account, amount: Money): (Account, Account) = {
    require(toAccount != null, "Destination account cannot be null")
    require(amount != null && !amount.isNegative, "Transfer amount must be positive")
    require(balance >= amount, "Insufficient funds for transfer")
    
    val fromNewBalance = balance - amount
    val toNewBalance = toAccount.balance + amount
    
    val fromTransaction = Transaction(LocalDateTime.now(), amount, TRANSFER_OUT, fromNewBalance, s"Transfer to ${toAccount.accountId}")
    val toTransaction = Transaction(LocalDateTime.now(), amount, TRANSFER_IN, toNewBalance, s"Transfer from $accountId")
    
    val newFromAccount = copy(balance = fromNewBalance, transactions = transactions :+ fromTransaction)
    val newToAccount = toAccount.copy(balance = toNewBalance, transactions = toAccount.transactions :+ toTransaction)
    
    (newFromAccount, newToAccount)
  }

  def printBalance(): String = 
    s"Account: $accountId | Balance: ${balance.toString} | Date: ${LocalDateTime.now().toString}"

  def printStatement(): String = {
    val statement = new StringBuilder
    statement.append("=== Account Statement ===\n")
    statement.append(s"Account: $accountId\n")
    statement.append(s"Current Balance: ${balance.toString}\n")
    statement.append("=== Transaction History ===\n")
    statement.append("Date | Amount | Balance\n")
    statement.append("-------------------------\n")
    
    transactions.foreach { transaction =>
      statement.append(transaction.toString).append("\n")
    }
    
    statement.toString()
  }

  def printStatement(filteredTransactions: List[Transaction]): String = {
    val statement = new StringBuilder
    statement.append("=== Filtered Account Statement ===\n")
    statement.append(s"Account: $accountId\n")
    statement.append(s"Current Balance: ${balance.toString}\n")
    statement.append("=== Filtered Transaction History ===\n")
    statement.append("Date | Amount | Balance\n")
    statement.append("-------------------------\n")
    
    filteredTransactions.foreach { transaction =>
      statement.append(transaction.toString).append("\n")
    }
    
    statement.toString()
  }

  def getDepositsOnly(): List[Transaction] = StatementFilter.depositsOnly(transactions)
  def getWithdrawalsOnly(): List[Transaction] = StatementFilter.withdrawalsOnly(transactions)
}
