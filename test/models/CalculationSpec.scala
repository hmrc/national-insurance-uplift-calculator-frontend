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

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class CalculationSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks {

  "niBeforeUplift" - {

    "must be 0 when the salary is £9,880 or less (below threshold)" in {

      forAll(Gen.choose[BigDecimal](1, 9880)) {
        amount =>
          Calculation(amount).niBeforeUplift mustEqual 0
      }
    }

    "must be 0 when the salary is £9,880.03 (above threshold, rounding down)" in {

      Calculation(BigDecimal(9880.03)).niBeforeUplift mustEqual 0
    }

    "must be 0.01 when the salary is £9,880.04 (above threshold, rounding up)" in {

      Calculation(BigDecimal(9880.04)).niBeforeUplift mustEqual BigDecimal(0.01)
    }

    "must be 5351.67 when the salary is £50,270 (at UEL)" in {

      Calculation(BigDecimal(50270)).niBeforeUplift mustEqual 5351.67
    }

    "must be 5251.68 when the salary is £50,270.01 (above UEL)" in {

      Calculation(BigDecimal(50270.01)).niBeforeUplift mustEqual 5351.68
    }

    "must be 5251.68 when the salary is £50,270.30 (above UEL, rounding down)" in {

      Calculation(BigDecimal(50270.30)).niBeforeUplift mustEqual 5351.68
    }

    "must be 5251.69 when the salary is £50,270.31 (above UEL, rounding up)" in {

      Calculation(BigDecimal(50270.31)).niBeforeUplift mustEqual 5351.69
    }
  }

  "niAfterUplift" - {

    "must be 0 when the salary is £12,570 or less (below threshold)" in {

      forAll(Gen.choose[BigDecimal](1, 12570)) {
        amount =>
          Calculation(amount).niAfterUplift mustEqual 0
      }
    }

    "must be 0 when the salary is £12,570.03 (above threshold, rounding down)" in {

      Calculation(BigDecimal(12570.03)).niAfterUplift mustEqual 0
    }

    "must be 0.01 when the salary is £12,570.04 (above threshold, rounding up)" in {

      Calculation(BigDecimal(12570.04)).niAfterUplift mustEqual BigDecimal(0.01)
    }

    "must be 4995.25 when the salary is £50,270 (at UEL)" in {

      Calculation(BigDecimal(50270)).niAfterUplift mustEqual 4995.25
    }

    "must be 4995.25 when the salary is £50,270.01 (above UEL)" in {

      Calculation(BigDecimal(50270.01)).niAfterUplift mustEqual 4995.25
    }

    "must be 4995.25 when the salary is £50,270.15 (above UEL, rounding down)" in {

      Calculation(BigDecimal(50270.15)).niAfterUplift mustEqual 4995.25
    }

    "must be 4995.25 when the salary is £50,270.16 (above UEL, rounding up)" in {

      Calculation(BigDecimal(50270.16)).niAfterUplift mustEqual 4995.26
    }
  }
}
