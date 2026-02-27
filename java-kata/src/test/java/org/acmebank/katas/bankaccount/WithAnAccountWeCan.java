package org.acmebank.katas.bankaccount;

import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WithAnAccountWeCan {

    @Test
    public void createAnAccountWithZeroBalance() {
        Account account = new Account();
        assertThat(account.getBalance()).isEqualTo(Money.zero());
    }

    @Test
    public void createAnAccountWithInitialBalance() {
        Money initialBalance = new Money("100.00");
        Account account = new Account(initialBalance);
        assertThat(account.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    public void depositAnAmountToIncreaseTheBalance() {
        Account account = new Account();
        Money depositAmount = new Money("50.00");
        
        account.deposit(depositAmount);
        
        assertThat(account.getBalance()).isEqualTo(depositAmount);
    }

    @Test
    public void depositMultipleAmountsToIncreaseTheBalance() {
        Account account = new Account();
        Money firstDeposit = new Money("50.00");
        Money secondDeposit = new Money("30.00");
        
        account.deposit(firstDeposit);
        account.deposit(secondDeposit);
        
        assertThat(account.getBalance()).isEqualTo(new Money("80.00"));
    }

    @Test
    public void cannotDepositNegativeAmount() {
        Account account = new Account();
        Money negativeAmount = new Money("-10.00");
        
        assertThatThrownBy(() -> account.deposit(negativeAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Deposit amount must be positive");
    }

    @Test
    public void cannotDepositNullAmount() {
        Account account = new Account();
        
        assertThatThrownBy(() -> account.deposit(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Deposit amount must be positive");
    }

    @Test
    public void withdrawAnAmountToDecreaseTheBalance() {
        Account account = new Account(new Money("100.00"));
        Money withdrawAmount = new Money("30.00");
        
        account.withdraw(withdrawAmount);
        
        assertThat(account.getBalance()).isEqualTo(new Money("70.00"));
    }

    @Test
    public void cannotWithdrawMoreThanBalance() {
        Account account = new Account(new Money("50.00"));
        Money withdrawAmount = new Money("60.00");
        
        assertThatThrownBy(() -> account.withdraw(withdrawAmount))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Insufficient funds for withdrawal");
    }

    @Test
    public void cannotWithdrawNegativeAmount() {
        Account account = new Account(new Money("100.00"));
        Money negativeAmount = new Money("-10.00");
        
        assertThatThrownBy(() -> account.withdraw(negativeAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Withdrawal amount must be positive");
    }

    @Test
    public void cannotWithdrawNullAmount() {
        Account account = new Account(new Money("100.00"));
        
        assertThatThrownBy(() -> account.withdraw(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Withdrawal amount must be positive");
    }

    @Test
    public void transferMoneyBetweenAccounts() {
        Account fromAccount = new Account("FROM-ACCOUNT", new Money("100.00"));
        Account toAccount = new Account("TO-ACCOUNT", new Money("50.00"));
        Money transferAmount = new Money("30.00");
        
        fromAccount.transferTo(toAccount, transferAmount);
        
        assertThat(fromAccount.getBalance()).isEqualTo(new Money("70.00"));
        assertThat(toAccount.getBalance()).isEqualTo(new Money("80.00"));
    }

    @Test
    public void cannotTransferMoreThanBalance() {
        Account fromAccount = new Account(new Money("50.00"));
        Account toAccount = new Account(new Money("100.00"));
        Money transferAmount = new Money("60.00");
        
        assertThatThrownBy(() -> fromAccount.transferTo(toAccount, transferAmount))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Insufficient funds for transfer");
    }

    @Test
    public void cannotTransferToNullAccount() {
        Account fromAccount = new Account(new Money("100.00"));
        Money transferAmount = new Money("30.00");
        
        assertThatThrownBy(() -> fromAccount.transferTo(null, transferAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Destination account cannot be null");
    }

    @Test
    public void cannotTransferNegativeAmount() {
        Account fromAccount = new Account(new Money("100.00"));
        Account toAccount = new Account(new Money("50.00"));
        Money negativeAmount = new Money("-10.00");
        
        assertThatThrownBy(() -> fromAccount.transferTo(toAccount, negativeAmount))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Transfer amount must be positive");
    }

    @Test
    public void printAccountBalance() {
        Account account = new Account("TEST-ACCOUNT", new Money("100.50"));
        String balanceOutput = account.printBalance();
        
        assertThat(balanceOutput).contains("TEST-ACCOUNT");
        assertThat(balanceOutput).contains("100.5");
        assertThat(balanceOutput).contains("Balance:");
    }

    @Test
    public void printAccountStatement() {
        Account account = new Account("TEST-ACCOUNT");
        account.deposit(new Money("100.00"));
        account.withdraw(new Money("30.00"));
        
        String statement = account.printStatement();
        
        assertThat(statement).contains("Account Statement");
        assertThat(statement).contains("TEST-ACCOUNT");
        assertThat(statement).contains("70");
        assertThat(statement).contains("Transaction History");
    }

    @Test
    public void trackTransactionHistory() {
        Account account = new Account();
        account.deposit(new Money("100.00"));
        account.withdraw(new Money("30.00"));
        account.deposit(new Money("50.00"));
        
        List<Transaction> transactions = account.getTransactions();
        
        assertThat(transactions).hasSize(3);
        assertThat(transactions.get(0).getType()).isEqualTo(Transaction.Type.DEPOSIT);
        assertThat(transactions.get(1).getType()).isEqualTo(Transaction.Type.WITHDRAWAL);
        assertThat(transactions.get(2).getType()).isEqualTo(Transaction.Type.DEPOSIT);
    }

    @Test
    public void filterStatementForDepositsOnly() {
        Account account = new Account();
        account.deposit(new Money("100.00"));
        account.withdraw(new Money("30.00"));
        account.deposit(new Money("50.00"));
        
        List<Transaction> deposits = account.getDepositsOnly();
        
        assertThat(deposits).hasSize(2);
        assertThat(deposits).allMatch(Transaction::isDeposit);
    }

    @Test
    public void filterStatementForWithdrawalsOnly() {
        Account account = new Account();
        account.deposit(new Money("100.00"));
        account.withdraw(new Money("30.00"));
        account.withdraw(new Money("20.00"));
        
        List<Transaction> withdrawals = account.getWithdrawalsOnly();
        
        assertThat(withdrawals).hasSize(2);
        assertThat(withdrawals).allMatch(Transaction::isWithdrawal);
    }

    @Test
    public void filterStatementByDate() {
        Account account = new Account();
        account.deposit(new Money("100.00"));
        
        List<Transaction> transactions = account.getTransactions();
        List<Transaction> todayTransactions = StatementFilter.byDate(transactions, LocalDate.now());
        
        assertThat(todayTransactions).hasSize(1);
    }

    @Test
    public void transferRecordsTransactionsInBothAccounts() {
        Account fromAccount = new Account("FROM-ACCOUNT", new Money("100.00"));
        Account toAccount = new Account("TO-ACCOUNT", new Money("50.00"));
        Money transferAmount = new Money("30.00");
        
        fromAccount.transferTo(toAccount, transferAmount);
        
        List<Transaction> fromTransactions = fromAccount.getTransactions();
        List<Transaction> toTransactions = toAccount.getTransactions();
        
        assertThat(fromTransactions).hasSize(2); // Initial deposit + transfer out
        assertThat(toTransactions).hasSize(2);   // Initial deposit + transfer in
        
        Transaction transferOut = fromTransactions.get(1);
        Transaction transferIn = toTransactions.get(1);
        
        assertThat(transferOut.getType()).isEqualTo(Transaction.Type.TRANSFER_OUT);
        assertThat(transferIn.getType()).isEqualTo(Transaction.Type.TRANSFER_IN);
        assertThat(transferOut.getAmount()).isEqualTo(transferAmount);
        assertThat(transferIn.getAmount()).isEqualTo(transferAmount);
    }
}
