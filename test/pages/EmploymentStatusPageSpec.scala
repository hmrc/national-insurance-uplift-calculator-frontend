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

package pages

import models.{EmploymentStatus, UserAnswers}
import pages.behaviours.PageBehaviours

class EmploymentStatusPageSpec extends PageBehaviours {

  "EmploymentStatusPage" - {

    beRetrievable[EmploymentStatus](EmploymentStatusPage)

    beSettable[EmploymentStatus](EmploymentStatusPage)

    beRemovable[EmploymentStatus](EmploymentStatusPage)

    "must remove Annual Income when the answer is Employed" in {

      val answers =
        UserAnswers("id")
          .set(AnnualIncomePage, BigDecimal(1)).success.value
          .set(SalaryPage, BigDecimal(1)).success.value

      val result = answers.set(EmploymentStatusPage, EmploymentStatus.Employed).success.value

      result.get(SalaryPage) mustBe defined
      result.get(AnnualIncomePage) must not be defined
    }

    "must remove Salary when the answer is Self Employed" in {

      val answers =
        UserAnswers("id")
          .set(AnnualIncomePage, BigDecimal(1)).success.value
          .set(SalaryPage, BigDecimal(1)).success.value

      val result = answers.set(EmploymentStatusPage, EmploymentStatus.SelfEmployed).success.value

      result.get(SalaryPage) must not be defined
      result.get(AnnualIncomePage) mustBe defined
    }
  }
}
