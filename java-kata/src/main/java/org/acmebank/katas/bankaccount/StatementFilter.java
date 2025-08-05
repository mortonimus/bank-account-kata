package org.acmebank.katas.bankaccount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatementFilter {
    
    public static List<Transaction> depositsOnly(List<Transaction> transactions) {
        return transactions.stream()
            .filter(Transaction::isDeposit)
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> withdrawalsOnly(List<Transaction> transactions) {
        return transactions.stream()
            .filter(Transaction::isWithdrawal)
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> byDate(List<Transaction> transactions, LocalDate date) {
        return transactions.stream()
            .filter(transaction -> transaction.getDate().toLocalDate().equals(date))
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> byDateRange(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
            .filter(transaction -> {
                LocalDate transactionDate = transaction.getDate().toLocalDate();
                return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
            })
            .collect(Collectors.toList());
    }
    
    public static List<Transaction> applyFilter(List<Transaction> transactions, Predicate<Transaction> filter) {
        return transactions.stream()
            .filter(filter)
            .collect(Collectors.toList());
    }
}