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

package metrics

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MonitorsSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks {


  "calculationOutcome" - {

    "must increment the correct counter" -{

      "when the saving is less than 0" in {

        forAll(Gen.choose(Int.MinValue, -1)) {
          amount =>
            Monitors.calculationOutcome(BigDecimal(amount)).name mustEqual "CalculationOutcome.willPayMore"
        }
      }

      "when the saving is 0" in {

        Monitors.calculationOutcome(BigDecimal(0)).name mustEqual "CalculationOutcome.willPayTheSame"
      }

      "when the saving is more than 0" in {

        forAll(Gen.choose(1, Int.MaxValue)) {
          amount =>
            Monitors.calculationOutcome(BigDecimal(amount)).name mustEqual "CalculationOutcome.willPayLess"
        }
      }
    }
  }
}
