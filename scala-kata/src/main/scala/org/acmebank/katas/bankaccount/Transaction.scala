package org.acmebank.katas.bankaccount

import java.time.LocalDateTime

object TransactionType extends Enumeration {
  type TransactionType = Value
  val DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT = Value
}

import TransactionType._

case class Transaction(
  date: LocalDateTime,
  amount: Money,
  transactionType: TransactionType,
  balance: Money,
  description: String = ""
) {
  
  def isDeposit: Boolean = transactionType == DEPOSIT || transactionType == TRANSFER_IN
  def isWithdrawal: Boolean = transactionType == WITHDRAWAL || transactionType == TRANSFER_OUT

  override def toString: String = {
    val operation = if (isDeposit) "+" else "-"
    s"$date | $operation${amount.toString} | ${balance.toString}"
  }
}