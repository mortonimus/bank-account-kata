package org.suggs.katas.bankaccount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.suggs.katas.bankaccount.Account.anAccountWith;
import static org.suggs.katas.bankaccount.Account.anEmptyAccount;
import static org.suggs.katas.bankaccount.AccountBalanceComparator.ofBalances;
import static org.suggs.katas.bankaccount.Money.anAmountOf;

/*
 - I can print out an Account balance (date, amount, balance)
 - I can print a statementWriter of account activity (statementWriter)
 - I can apply StatementWriter filters (just deposits, withdrawal, date)
 */
@RunWith(MockitoJUnitRunner.class)
public class WithAnAccountWeCan {

    @Mock
    StatementWriter statementWriter;

    @Test
    public void compareTwoAccountsHaveTheSameBalance() {
        Account account = anAccountWith(anAmountOf(10.0d));
        assertThat(account).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(10.0d)));
    }

    @Test
    public void depositAnAmountToIncreaseTheBalance() {
        Account account = anEmptyAccount();
        account.deposit(anAmountOf(10.0d));
        assertThat(account).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(10.0d)));
    }

    @Test
    public void withdrawAnAmountToDecreaseTheBalance() {
        Account account = anAccountWith(anAmountOf(20.0d));
        account.withdraw(anAmountOf(10.0d));
        assertThat(account).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(10.0d)));
    }

    @Test(expected = IllegalStateException.class)
    public void throwsExceptionIfYouTryToWithdrawMoreThanTheBalance() {
        Account account = anAccountWith(anAmountOf(20.0d));
        account.withdraw(anAmountOf(30.0d));
    }

    @Test
    public void transferMoneyFromOneAccountToAnother() {
        Account destinationAccount = anEmptyAccount();
        Account sourceAccount = anAccountWith(anAmountOf(50.0d));

        sourceAccount.transferTo(destinationAccount, anAmountOf(20.0d));

        assertThat(sourceAccount).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(30.0d)));
        assertThat(destinationAccount).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(20.0d)));
    }

    @Test(expected = IllegalStateException.class)
    public void throwsExceptionIfYouTryToTransferMoreThantheBalance() {
        Account sourceAccount = anAccountWith(anAmountOf(20.0d));
        sourceAccount.transferTo(anEmptyAccount(), anAmountOf(30.0d));
    }

    @Test
    public void hasTheRightBalanceAfterANumberOfTransactions() {
        Account account = anEmptyAccount();
        account.deposit(anAmountOf(10.0d));
        account.deposit(anAmountOf(80.0d));
        account.deposit(anAmountOf(5.0d));
        account.withdraw(anAmountOf(15.0d));
        account.withdraw(anAmountOf(10.0d));
        assertThat(account).usingComparator(ofBalances()).isEqualTo(anAccountWith(anAmountOf(70.0d)));
    }

    @Test
    public void printOutAnAccountBalance() {
        Account account = anAccountWith(anAmountOf(30.0d));
        account.setStatementWriter(statementWriter);
        account.printBalanceStatement();
        verify(statementWriter).printBalanceOf(any(Money.class));
    }

    @Test
    public void printOutAFullStatement() {
        Account account = anEmptyAccount();
        account.setStatementWriter(statementWriter);
        account.printFullStatement();
        verify(statementWriter).printFullStatementWith(anyListOf(Transaction.class));
    }

}
