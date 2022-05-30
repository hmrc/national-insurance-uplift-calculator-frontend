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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad
      }

      "must go from Employment Status" - {

        import models.EmploymentStatus._

        "to Salary when the result is Employed" in {

          val answers = emptyUserAnswers.set(EmploymentStatusPage, Employed).success.value

          navigator.nextPage(EmploymentStatusPage, NormalMode, answers) mustBe routes.SalaryController.onPageLoad(NormalMode)
        }

        "to Annual Income when the result is Self-employed" in {

          val answers = emptyUserAnswers.set(EmploymentStatusPage, SelfEmployed).success.value

          navigator.nextPage(EmploymentStatusPage, NormalMode, answers) mustBe routes.AnnualIncomeController.onPageLoad(NormalMode)
        }
      }
      "must go from Salary to Result" in {

        navigator.nextPage(SalaryPage, NormalMode, emptyUserAnswers) mustBe routes.ResultController.onPageLoad
      }

      "must go from Annual Income to Result" in {

        navigator.nextPage(AnnualIncomePage, NormalMode, emptyUserAnswers) mustBe routes.ResultController.onPageLoad
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }

      "must go from Employment Status" - {

        import models.EmploymentStatus._

        "when the answer is Employed" - {

          "to Check Your Answers when it has already been answered" in {

            val answers =
              emptyUserAnswers
                .set(SalaryPage, BigDecimal(1)).success.value
                .set(EmploymentStatusPage, Employed).success.value

            navigator.nextPage(EmploymentStatusPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
          }

          "to Salary when it has not already been answered" in {

            val answers = emptyUserAnswers.set(EmploymentStatusPage, Employed).success.value

            navigator.nextPage(EmploymentStatusPage, CheckMode, answers) mustBe routes.SalaryController.onPageLoad(CheckMode)
          }
        }

        "when the answer is Self-employed" - {

          "to Check Your Answers when it has already been answered" in {

            val answers =
              emptyUserAnswers
                .set(AnnualIncomePage, BigDecimal(1)).success.value
                .set(EmploymentStatusPage, SelfEmployed).success.value

            navigator.nextPage(EmploymentStatusPage, CheckMode, answers) mustBe routes.CheckYourAnswersController.onPageLoad
          }

          "to Annual Income when it has not already been answered" in {

            val answers = emptyUserAnswers.set(EmploymentStatusPage, SelfEmployed).success.value

            navigator.nextPage(EmploymentStatusPage, CheckMode, answers) mustBe routes.AnnualIncomeController.onPageLoad(CheckMode)
          }
        }
      }
    }
  }
}
