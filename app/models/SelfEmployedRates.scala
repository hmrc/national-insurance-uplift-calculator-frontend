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

case class SelfEmployedRates(
                              smallProfitsThreshold: BigDecimal,
                              lowerProfitLimit: BigDecimal,
                              upperProfitLimit: BigDecimal,
                              smallProfitsRate: BigDecimal,
                              mainRate: BigDecimal,
                              upperRate: BigDecimal
                            )

object SelfEmployedRates {

  val april21April22Rates: SelfEmployedRates = SelfEmployedRates(
    smallProfitsThreshold = BigDecimal(6515),
    lowerProfitLimit      = BigDecimal(9568),
    upperProfitLimit      = BigDecimal(50270),
    smallProfitsRate      = BigDecimal(3.05),
    mainRate              = BigDecimal(0.09),
    upperRate             = BigDecimal(0.02)
  )

  val april22April23Rates: SelfEmployedRates = SelfEmployedRates(
    smallProfitsThreshold = BigDecimal(11908),
    lowerProfitLimit      = BigDecimal(11908),
    upperProfitLimit      = BigDecimal(50270),
    smallProfitsRate      = BigDecimal(3.15),
    mainRate              = BigDecimal(0.1025),
    upperRate             = BigDecimal(0.0325)
  )

  val april23April24Rates: SelfEmployedRates = SelfEmployedRates(
    smallProfitsThreshold = BigDecimal(12570),
    lowerProfitLimit      = BigDecimal(12570),
    upperProfitLimit      = BigDecimal(50270),
    smallProfitsRate      = BigDecimal(3.15),
    mainRate              = BigDecimal(0.1025),
    upperRate             = BigDecimal(0.0325)
  )
}
