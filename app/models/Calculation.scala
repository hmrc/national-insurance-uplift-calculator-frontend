/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import scala.math.BigDecimal.RoundingMode

case class Calculation(
                        salary: BigDecimal,
                        niBeforeUplift: BigDecimal,
                        niAfterUplift: BigDecimal,
                        amountSaved: BigDecimal
                      )

object Calculation {

  private val preUpliftThreshold  = BigDecimal(9880)
  private val postUpliftThreshold = BigDecimal(12570)
  private val upperEarningsLimit  = BigDecimal(50270)
  private val standardRate        = BigDecimal(0.1325)
  private val upperRate           = BigDecimal(0.0325)

  def apply(salary: BigDecimal): Calculation = {

    val niPreUplift = niDue(salary, preUpliftThreshold)
    val niPostUplift = niDue(salary, postUpliftThreshold)
    val amountSaved = niPreUplift - niPostUplift

    Calculation(salary, niPreUplift, niPostUplift, amountSaved)
  }

  private def niDue(salary: BigDecimal, threshold: BigDecimal): BigDecimal = {

    val standard = (salary - threshold).max(0).min(upperEarningsLimit - threshold)
    val upper = (salary - upperEarningsLimit).max(0)

    ((standard * standardRate) + (upper * upperRate)).setScale(2, RoundingMode.HALF_DOWN)
  }
}
