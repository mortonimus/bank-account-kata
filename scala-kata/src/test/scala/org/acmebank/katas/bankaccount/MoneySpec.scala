package org.acmebank.katas.bankaccount

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.math.BigDecimal

class MoneySpec extends AnyFlatSpec with Matchers {

  "Money" should "be created from BigDecimal" in {
    val money = Money(new BigDecimal("100.50"))
    money should not be null
  }

  it should "be created from String" in {
    val money = Money("100.50")
    money should not be null
  }

  it should "be created from Double" in {
    val money = Money(100.50)
    money should not be null
  }

  it should "create zero money" in {
    val zero = Money.zero
    zero should not be null
    zero shouldEqual Money("0")
  }

  it should "throw exception for null amount" in {
    an[IllegalArgumentException] should be thrownBy Money(null.asInstanceOf[BigDecimal])
  }

  it should "add money correctly" in {
    val money1 = Money("100.50")
    val money2 = Money("50.25")
    val result = money1 + money2
    
    result shouldEqual Money("150.75")
  }

  it should "subtract money correctly" in {
    val money1 = Money("100.50")
    val money2 = Money("50.25")
    val result = money1 - money2
    
    result shouldEqual Money("50.25")
  }

  it should "compare money with greater than" in {
    val money1 = Money("100.50")
    val money2 = Money("50.25")
    
    (money1 > money2) shouldBe true
    (money2 > money1) shouldBe false
  }

  it should "compare money with greater than or equal" in {
    val money1 = Money("100.50")
    val money2 = Money("50.25")
    val money3 = Money("100.50")
    
    (money1 >= money2) shouldBe true
    (money1 >= money3) shouldBe true
    (money2 >= money1) shouldBe false
  }

  it should "compare money with less than" in {
    val money1 = Money("50.25")
    val money2 = Money("100.50")
    
    (money1 < money2) shouldBe true
    (money2 < money1) shouldBe false
  }

  it should "detect negative money" in {
    val negativeMoney = Money("-10.00")
    val positiveMoney = Money("10.00")
    val zeroMoney = Money.zero
    
    negativeMoney.isNegative shouldBe true
    positiveMoney.isNegative shouldBe false
    zeroMoney.isNegative shouldBe false
  }

  it should "have proper equality" in {
    val money1 = Money("100.50")
    val money2 = Money("100.50")
    
    money1 shouldEqual money2
    money1.hashCode shouldEqual money2.hashCode
  }

  it should "have different objects not equal" in {
    val money1 = Money("100.50")
    val money2 = Money("50.25")
    
    money1 should not equal money2
  }

  it should "convert to string properly" in {
    val money = Money("100.50")
    money.toString shouldEqual "100.50"
  }
}