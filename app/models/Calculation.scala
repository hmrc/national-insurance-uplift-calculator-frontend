/*
 * Copyright 2023 HM Revenue & Customs
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

import TaxYearRates._

import java.time.LocalDate
import scala.math.BigDecimal.RoundingMode.HALF_DOWN

case class Calculation private(
                                annualSalary: BigDecimal,
                                period1: Period,
                                period2: Period,
                                saving: BigDecimal
                              )

object Calculation {

  def apply(annualSalary: BigDecimal): Calculation = {

    val monthlySalary = annualSalary / BigDecimal(12)

    val july21March22Ni = monthlyNi(monthlySalary, july21March22Rates) * BigDecimal(9)
    val april22June22Ni = monthlyNi(monthlySalary, april22June22Rates) * BigDecimal(3)
    val july22June23Ni  = monthlyNi(monthlySalary, july22June23Rates)  * BigDecimal(12)

    val period1 = Period(LocalDate.of(2021, 7, 6), LocalDate.of(2022, 7, 5), july21March22Ni + april22June22Ni)
    val period2 = Period(LocalDate.of(2022, 7, 6), LocalDate.of(2023, 7, 5), july22June23Ni)
    val saving  = (period1.estimatedNic - period2.estimatedNic).setScale(0, HALF_DOWN)

    Calculation(annualSalary, period1, period2, saving)
  }

    private def monthlyNi(monthlySalary: BigDecimal, rates: Rates): BigDecimal = {

      val primarySalary = (monthlySalary - rates.threshold).min(rates.upperLimit - rates.threshold).max(0)
      val upperSalary   = (monthlySalary - rates.upperLimit).max(0)

      (primarySalary * rates.mainRate).setScale(2, HALF_DOWN) + (upperSalary * rates.upperRate).setScale(2, HALF_DOWN)
    }
}
