package org.acmebank.katas.bankaccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private Money balance;
    private final List<Transaction> transactions;
    private final String accountId;

    public Account() {
        this("ACCOUNT-" + System.currentTimeMillis());
    }

    public Account(String accountId) {
        this.accountId = accountId;
        this.balance = Money.zero();
        this.transactions = new ArrayList<>();
    }

    public Account(Money initialBalance) {
        this("ACCOUNT-" + System.currentTimeMillis(), initialBalance);
    }

    public Account(String accountId, Money initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        if (!initialBalance.equals(Money.zero())) {
            addTransaction(LocalDateTime.now(), initialBalance, Transaction.Type.DEPOSIT, "Initial deposit");
        }
    }

    public void deposit(Money amount) {
        if (amount == null || amount.isNegative()) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        addTransaction(LocalDateTime.now(), amount, Transaction.Type.DEPOSIT, "");
    }

    public void withdraw(Money amount) {
        if (amount == null || amount.isNegative()) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance.isLessThan(amount)) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }
        this.balance = this.balance.subtract(amount);
        addTransaction(LocalDateTime.now(), amount, Transaction.Type.WITHDRAWAL, "");
    }

    public void transferTo(Account toAccount, Money amount) {
        if (toAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }
        if (amount == null || amount.isNegative()) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (this.balance.isLessThan(amount)) {
            throw new IllegalStateException("Insufficient funds for transfer");
        }
        
        // Withdraw from this account
        this.balance = this.balance.subtract(amount);
        addTransaction(LocalDateTime.now(), amount, Transaction.Type.TRANSFER_OUT, 
                      "Transfer to " + toAccount.getAccountId());
        
        // Deposit to destination account
        toAccount.balance = toAccount.balance.add(amount);
        toAccount.addTransaction(LocalDateTime.now(), amount, Transaction.Type.TRANSFER_IN, 
                               "Transfer from " + this.getAccountId());
    }

    public Money getBalance() {
        return this.balance;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String printBalance() {
        return String.format("Account: %s | Balance: %s | Date: %s", 
                           accountId, balance.toString(), LocalDateTime.now().toString());
    }

    public String printStatement() {
        StringBuilder statement = new StringBuilder();
        statement.append("=== Account Statement ===\n");
        statement.append(String.format("Account: %s\n", accountId));
        statement.append(String.format("Current Balance: %s\n", balance.toString()));
        statement.append("=== Transaction History ===\n");
        statement.append("Date | Amount | Balance\n");
        statement.append("-------------------------\n");
        
        for (Transaction transaction : transactions) {
            statement.append(transaction.toString()).append("\n");
        }
        
        return statement.toString();
    }

    public String printStatement(List<Transaction> filteredTransactions) {
        StringBuilder statement = new StringBuilder();
        statement.append("=== Filtered Account Statement ===\n");
        statement.append(String.format("Account: %s\n", accountId));
        statement.append(String.format("Current Balance: %s\n", balance.toString()));
        statement.append("=== Filtered Transaction History ===\n");
        statement.append("Date | Amount | Balance\n");
        statement.append("-------------------------\n");
        
        for (Transaction transaction : filteredTransactions) {
            statement.append(transaction.toString()).append("\n");
        }
        
        return statement.toString();
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getDepositsOnly() {
        return StatementFilter.depositsOnly(transactions);
    }

    public List<Transaction> getWithdrawalsOnly() {
        return StatementFilter.withdrawalsOnly(transactions);
    }

    private void addTransaction(LocalDateTime date, Money amount, Transaction.Type type, String description) {
        Transaction transaction = new Transaction(date, amount, type, this.balance, description);
        transactions.add(transaction);
    }
}
