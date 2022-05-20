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

case class Rates(
                  threshold: BigDecimal,
                  upperLimit: BigDecimal,
                  mainRate: BigDecimal,
                  upperRate: BigDecimal
                )

trait TaxYearRates {

  val july21March22Rates: Rates
  val april22June22Rates: Rates
  val july22June23Rates: Rates
}

object TaxYearRates {

  object EmployedRates extends TaxYearRates {

    val july21March22Rates: Rates = Rates(
      threshold = BigDecimal(797),
      upperLimit = BigDecimal(4189),
      mainRate = BigDecimal(0.12),
      upperRate = BigDecimal(0.02)
    )

    val april22June22Rates: Rates = Rates(
      threshold = BigDecimal(823),
      upperLimit = BigDecimal(4189),
      mainRate = BigDecimal(0.1325),
      upperRate = BigDecimal(0.0325)
    )

    val july22June23Rates: Rates = Rates(
      threshold = BigDecimal(1048),
      upperLimit = BigDecimal(4189),
      mainRate = BigDecimal(0.1325),
      upperRate = BigDecimal(0.0325)
    )
  }
}