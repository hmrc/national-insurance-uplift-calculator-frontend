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

import scala.math.BigDecimal.RoundingMode.HALF_DOWN

case class Comparison private (
                                annualSalary: BigDecimal,
                                ni21_22: BigDecimal,
                                ni22_23: BigDecimal,
                                saving: BigDecimal
                              )

object Comparison {

  def apply(annualSalary: BigDecimal): Comparison = {

    val rates = TaxYearRates.EmployedRates

    val monthlySalary = annualSalary / BigDecimal(12)

    val july21March22Ni = monthlyNi(monthlySalary, rates.july21March22Rates) * BigDecimal(9)
    val april22June22Ni = monthlyNi(monthlySalary, rates.april22June22Rates) * BigDecimal(3)
    val ni21_22         = july21March22Ni + april22June22Ni
    val ni22_23         = monthlyNi(monthlySalary, rates.july22June23Rates) * BigDecimal(12)
    val saving          = (ni21_22 - ni22_23).setScale(0, HALF_DOWN)

    Comparison(annualSalary, ni21_22, ni22_23, saving)
  }

  private def monthlyNi(monthlySalary: BigDecimal, rates: Rates): BigDecimal = {

    val primarySalary = (monthlySalary - rates.threshold).min(rates.upperLimit - rates.threshold)
    val upperSalary   = (monthlySalary - rates.upperLimit).max(0)

    (primarySalary * rates.mainRate).setScale(2, HALF_DOWN) + (upperSalary * rates.upperRate).setScale(2, HALF_DOWN)
  }
}
