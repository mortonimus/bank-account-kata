package org.acmebank.katas.bankaccount;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }

    private final LocalDateTime date;
    private final Money amount;
    private final Type type;
    private final Money balance;
    private final String description;

    public Transaction(LocalDateTime date, Money amount, Type type, Money balance) {
        this(date, amount, type, balance, "");
    }

    public Transaction(LocalDateTime date, Money amount, Type type, Money balance, String description) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.balance = balance;
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Money getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }

    public Money getBalance() {
        return balance;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDeposit() {
        return type == Type.DEPOSIT || type == Type.TRANSFER_IN;
    }

    public boolean isWithdrawal() {
        return type == Type.WITHDRAWAL || type == Type.TRANSFER_OUT;
    }

    @Override
    public String toString() {
        String operation = isDeposit() ? "+" : "-";
        return String.format("%s | %s%s | %s", 
            date.toString(), operation, amount.toString(), balance.toString());
    }
}