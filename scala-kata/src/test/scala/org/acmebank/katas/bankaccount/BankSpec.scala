package org.acmebank.katas.bankaccount

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.time.LocalDate

class BankSpec extends AnyFlatSpec with Matchers {

  "Account" should "be created with zero balance" in {
    val account = Account()
    account.balance shouldEqual Money.zero
  }

  it should "be created with initial balance" in {
    val initialBalance = Money("100.00")
    val account = Account(initialBalance)
    account.balance shouldEqual initialBalance
  }

  "With an account we" can "deposit an amount to increase the balance" in {
    val account = Account()
    val depositAmount = Money("50.00")
    
    val updatedAccount = account.deposit(depositAmount)
    
    updatedAccount.balance shouldEqual depositAmount
  }

  it can "deposit multiple amounts to increase the balance" in {
    val account = Account()
    val firstDeposit = Money("50.00")
    val secondDeposit = Money("30.00")
    
    val updatedAccount = account.deposit(firstDeposit).deposit(secondDeposit)
    
    updatedAccount.balance shouldEqual Money("80.00")
  }

  it should "not allow depositing negative amounts" in {
    val account = Account()
    val negativeAmount = Money("-10.00")
    
    an[IllegalArgumentException] should be thrownBy {
      account.deposit(negativeAmount)
    }
  }

  it should "not allow depositing null amounts" in {
    val account = Account()
    
    an[IllegalArgumentException] should be thrownBy {
      account.deposit(null)
    }
  }

  it can "withdraw an amount to decrease the balance" in {
    val account = Account(Money("100.00"))
    val withdrawAmount = Money("30.00")
    
    val updatedAccount = account.withdraw(withdrawAmount)
    
    updatedAccount.balance shouldEqual Money("70.00")
  }

  it should "not allow withdrawing more than balance" in {
    val account = Account(Money("50.00"))
    val withdrawAmount = Money("60.00")
    
    an[IllegalArgumentException] should be thrownBy {
      account.withdraw(withdrawAmount)
    }
  }

  it should "not allow withdrawing negative amounts" in {
    val account = Account(Money("100.00"))
    val negativeAmount = Money("-10.00")
    
    an[IllegalArgumentException] should be thrownBy {
      account.withdraw(negativeAmount)
    }
  }

  it should "not allow withdrawing null amounts" in {
    val account = Account(Money("100.00"))
    
    an[IllegalArgumentException] should be thrownBy {
      account.withdraw(null)
    }
  }

  it can "transfer money between accounts" in {
    val fromAccount = Account("FROM-ACCOUNT", Money("100.00"))
    val toAccount = Account("TO-ACCOUNT", Money("50.00"))
    val transferAmount = Money("30.00")
    
    val (updatedFromAccount, updatedToAccount) = fromAccount.transferTo(toAccount, transferAmount)
    
    updatedFromAccount.balance shouldEqual Money("70.00")
    updatedToAccount.balance shouldEqual Money("80.00")
  }

  it should "not allow transferring more than balance" in {
    val fromAccount = Account(Money("50.00"))
    val toAccount = Account(Money("100.00"))
    val transferAmount = Money("60.00")
    
    an[IllegalArgumentException] should be thrownBy {
      fromAccount.transferTo(toAccount, transferAmount)
    }
  }

  it should "not allow transferring to null account" in {
    val fromAccount = Account(Money("100.00"))
    val transferAmount = Money("30.00")
    
    an[IllegalArgumentException] should be thrownBy {
      fromAccount.transferTo(null, transferAmount)
    }
  }

  it should "not allow transferring negative amounts" in {
    val fromAccount = Account(Money("100.00"))
    val toAccount = Account(Money("50.00"))
    val negativeAmount = Money("-10.00")
    
    an[IllegalArgumentException] should be thrownBy {
      fromAccount.transferTo(toAccount, negativeAmount)
    }
  }

  it can "print account balance" in {
    val account = Account("TEST-ACCOUNT", Money("100.50"))
    val balanceOutput = account.printBalance()
    
    balanceOutput should include("TEST-ACCOUNT")
    balanceOutput should include("100.5")
    balanceOutput should include("Balance:")
  }

  it can "print account statement" in {
    val account = Account("TEST-ACCOUNT")
      .deposit(Money("100.00"))
      .withdraw(Money("30.00"))
    
    val statement = account.printStatement()
    
    statement should include("Account Statement")
    statement should include("TEST-ACCOUNT")
    statement should include("70")
    statement should include("Transaction History")
  }

  it can "track transaction history" in {
    val account = Account()
      .deposit(Money("100.00"))
      .withdraw(Money("30.00"))
      .deposit(Money("50.00"))
    
    account.transactions should have size 3
    account.transactions(0).transactionType shouldEqual TransactionType.DEPOSIT
    account.transactions(1).transactionType shouldEqual TransactionType.WITHDRAWAL
    account.transactions(2).transactionType shouldEqual TransactionType.DEPOSIT
  }

  it can "filter statement for deposits only" in {
    val account = Account()
      .deposit(Money("100.00"))
      .withdraw(Money("30.00"))
      .deposit(Money("50.00"))
    
    val deposits = account.getDepositsOnly()
    
    deposits should have size 2
    deposits.forall(_.isDeposit) shouldBe true
  }

  it can "filter statement for withdrawals only" in {
    val account = Account()
      .deposit(Money("100.00"))
      .withdraw(Money("30.00"))
      .withdraw(Money("20.00"))
    
    val withdrawals = account.getWithdrawalsOnly()
    
    withdrawals should have size 2
    withdrawals.forall(_.isWithdrawal) shouldBe true
  }

  it can "filter statement by date" in {
    val account = Account().deposit(Money("100.00"))
    
    val todayTransactions = StatementFilter.byDate(account.transactions, LocalDate.now())
    
    todayTransactions should have size 1
  }

  it can "record transactions in both accounts during transfer" in {
    val fromAccount = Account("FROM-ACCOUNT", Money("100.00"))
    val toAccount = Account("TO-ACCOUNT", Money("50.00"))
    val transferAmount = Money("30.00")
    
    val (updatedFromAccount, updatedToAccount) = fromAccount.transferTo(toAccount, transferAmount)
    
    updatedFromAccount.transactions should have size 2 // Initial deposit + transfer out
    updatedToAccount.transactions should have size 2   // Initial deposit + transfer in
    
    val transferOut = updatedFromAccount.transactions(1)
    val transferIn = updatedToAccount.transactions(1)
    
    transferOut.transactionType shouldEqual TransactionType.TRANSFER_OUT
    transferIn.transactionType shouldEqual TransactionType.TRANSFER_IN
    transferOut.amount shouldEqual transferAmount
    transferIn.amount shouldEqual transferAmount
  }
}
