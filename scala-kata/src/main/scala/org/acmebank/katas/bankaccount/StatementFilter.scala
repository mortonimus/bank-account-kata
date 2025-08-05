package org.acmebank.katas.bankaccount

import java.time.LocalDate

object StatementFilter {
  
  def depositsOnly(transactions: List[Transaction]): List[Transaction] = 
    transactions.filter(_.isDeposit)
  
  def withdrawalsOnly(transactions: List[Transaction]): List[Transaction] = 
    transactions.filter(_.isWithdrawal)
  
  def byDate(transactions: List[Transaction], date: LocalDate): List[Transaction] = 
    transactions.filter(_.date.toLocalDate == date)
  
  def byDateRange(transactions: List[Transaction], startDate: LocalDate, endDate: LocalDate): List[Transaction] = 
    transactions.filter { transaction =>
      val transactionDate = transaction.date.toLocalDate
      !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)
    }
  
  def applyFilter(transactions: List[Transaction], filter: Transaction => Boolean): List[Transaction] = 
    transactions.filter(filter)
}