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

import SelfEmployedRates._

import java.time.LocalDate
import scala.math.BigDecimal.RoundingMode.HALF_DOWN

case class SelfEmployedCalculation private(
                                            annualSalary: BigDecimal,
                                            period1: Period,
                                            period2: Period,
                                            saving: BigDecimal
                                          ) extends Calculation {

  val employmentStatus: EmploymentStatus = EmploymentStatus.SelfEmployed
}

object SelfEmployedCalculation {

  private val julyToAprilWeeks = BigDecimal(40)
  private val aprilToJulyWeeks = BigDecimal(12)
  private val yearWeeks        = BigDecimal(52)
  private val aprilToJulyDays  = BigDecimal(91)
  private val julyToAprilDays  = BigDecimal(274)
  private val yearDays         = BigDecimal(365)

  def aprilToApril(annualSalary: BigDecimal): SelfEmployedCalculation = {

    val ni21_22 =
      (class2Due(annualSalary, april21April22Rates) + class4Due(annualSalary, april21April22Rates)).setScale(2, HALF_DOWN)

    val ni22_23 =
      (class2Due(annualSalary, april22April23Rates) + class4Due(annualSalary, april22April23Rates)).setScale(2, HALF_DOWN)

    val period1 = Period(LocalDate.of(2021, 4, 6), LocalDate.of(2022, 4, 5), ni21_22)
    val period2 = Period(LocalDate.of(2022, 4, 6), LocalDate.of(2023, 4, 5), ni22_23)
    val saving = (period1.estimatedNic - period2.estimatedNic).setScale(0, HALF_DOWN)

    SelfEmployedCalculation(annualSalary, period1, period2, saving)
  }

  def julyToJuly(annualSalary: BigDecimal): SelfEmployedCalculation = {

    val july21April22class2 = class2Due(annualSalary, april21April22Rates) * julyToAprilWeeks / yearWeeks
    val july21April22class4 = class4Due(annualSalary, april21April22Rates) * julyToAprilDays / yearDays

    val april22July22class2 = class2Due(annualSalary, april22April23Rates) * aprilToJulyWeeks / yearWeeks
    val april22July22class4 = class4Due(annualSalary, april22April23Rates) * aprilToJulyDays / yearDays

    val july22April23class2 = class2Due(annualSalary, april22April23Rates) * julyToAprilDays / yearDays
    val july22April23class4 = class4Due(annualSalary, april22April23Rates) * julyToAprilDays / yearDays

    val april23July23class2 = class2Due(annualSalary, april23April24Rates) * aprilToJulyDays / yearDays
    val april23July23class4 = class4Due(annualSalary, april23April24Rates) * aprilToJulyDays / yearDays

    val period1Ni = july21April22class2 + july21April22class4 + april22July22class2 + april22July22class4
    val period2Ni = july22April23class2 + july22April23class4 + april23July23class2 + april23July23class4

    val period1 = Period(LocalDate.of(2021, 7, 6), LocalDate.of(2022, 7, 5), period1Ni)
    val period2 = Period(LocalDate.of(2022, 7, 6), LocalDate.of(2023, 7, 5), period2Ni)
    val saving  = (period1.estimatedNic - period2.estimatedNic).setScale(0, HALF_DOWN)

    SelfEmployedCalculation(annualSalary, period1, period2, saving)
  }

  private def class2Due(annualSalary: BigDecimal, rates: SelfEmployedRates): BigDecimal =
    if (annualSalary >= rates.smallProfitsThreshold) rates.smallProfitsRate * yearWeeks else 0

  private def class4Due(annualSalary: BigDecimal, rates: SelfEmployedRates): BigDecimal = {

    val primary = (annualSalary - rates.lowerProfitLimit).min(rates.upperProfitLimit - rates.lowerProfitLimit).max(0)
    val upper   = (annualSalary - rates.upperProfitLimit).max(0)

    (primary * rates.mainRate) + (upper * rates.upperRate)
  }
}
