package org.acmebank.katas.bankaccount

import java.math.BigDecimal

/**
 * Immutable case class to represent Money as a concept.
 * This class should have no accessor methods beyond what case classes provide.
 */
case class Money(amount: BigDecimal) {
  require(amount != null, "Amount cannot be null")

  def this(amount: String) = this(new BigDecimal(amount))
  def this(amount: Double) = this(BigDecimal.valueOf(amount))

  def +(other: Money): Money = Money(this.amount.add(other.amount))
  def -(other: Money): Money = Money(this.amount.subtract(other.amount))

  def >(other: Money): Boolean = this.amount.compareTo(other.amount) > 0
  def >=(other: Money): Boolean = this.amount.compareTo(other.amount) >= 0
  def <(other: Money): Boolean = this.amount.compareTo(other.amount) < 0

  def isNegative: Boolean = this.amount.compareTo(BigDecimal.ZERO) < 0

  override def toString: String = amount.toString
}

object Money {
  val zero: Money = Money(BigDecimal.ZERO)
  
  def apply(amount: String): Money = new Money(amount)
  def apply(amount: Double): Money = new Money(amount)
}
