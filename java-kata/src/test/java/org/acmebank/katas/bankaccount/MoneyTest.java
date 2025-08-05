package org.acmebank.katas.bankaccount;

import org.junit.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MoneyTest {

    @Test
    public void canCreateMoneyFromBigDecimal() {
        Money money = new Money(new BigDecimal("100.50"));
        assertThat(money).isNotNull();
    }

    @Test
    public void canCreateMoneyFromString() {
        Money money = new Money("100.50");
        assertThat(money).isNotNull();
    }

    @Test
    public void canCreateMoneyFromDouble() {
        Money money = new Money(100.50);
        assertThat(money).isNotNull();
    }

    @Test
    public void canCreateZeroMoney() {
        Money zero = Money.zero();
        assertThat(zero).isNotNull();
        assertThat(zero).isEqualTo(new Money("0"));
    }

    @Test
    public void throwsExceptionForNullAmount() {
        assertThatThrownBy(() -> new Money((BigDecimal) null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Amount cannot be null");
    }

    @Test
    public void canAddMoney() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        Money result = money1.add(money2);
        
        assertThat(result).isEqualTo(new Money("150.75"));
    }

    @Test
    public void canSubtractMoney() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        Money result = money1.subtract(money2);
        
        assertThat(result).isEqualTo(new Money("50.25"));
    }

    @Test
    public void canCompareMoneyGreaterThan() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        
        assertThat(money1.isGreaterThan(money2)).isTrue();
        assertThat(money2.isGreaterThan(money1)).isFalse();
    }

    @Test
    public void canCompareMoneyGreaterThanOrEqual() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        Money money3 = new Money("100.50");
        
        assertThat(money1.isGreaterThanOrEqual(money2)).isTrue();
        assertThat(money1.isGreaterThanOrEqual(money3)).isTrue();
        assertThat(money2.isGreaterThanOrEqual(money1)).isFalse();
    }

    @Test
    public void canCompareMoneyLessThan() {
        Money money1 = new Money("50.25");
        Money money2 = new Money("100.50");
        
        assertThat(money1.isLessThan(money2)).isTrue();
        assertThat(money2.isLessThan(money1)).isFalse();
    }

    @Test
    public void canDetectNegativeMoney() {
        Money negativeMoney = new Money("-10.00");
        Money positiveMoney = new Money("10.00");
        Money zeroMoney = Money.zero();
        
        assertThat(negativeMoney.isNegative()).isTrue();
        assertThat(positiveMoney.isNegative()).isFalse();
        assertThat(zeroMoney.isNegative()).isFalse();
    }

    @Test
    public void equalMoneyObjectsAreEqual() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("100.50");
        
        assertThat(money1).isEqualTo(money2);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    public void differentMoneyObjectsAreNotEqual() {
        Money money1 = new Money("100.50");
        Money money2 = new Money("50.25");
        
        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    public void moneyToStringReturnsAmount() {
        Money money = new Money("100.50");
        assertThat(money.toString()).isEqualTo("100.50");
    }
}