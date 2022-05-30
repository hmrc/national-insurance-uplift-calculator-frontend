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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case EmploymentStatusPage => employmentStatusRoutes
    case SalaryPage           => _ => routes.ResultController.onPageLoad()
    case AnnualIncomePage     => _ => routes.ResultController.onPageLoad()
    case _                    => _ => routes.IndexController.onPageLoad
  }

  private def employmentStatusRoutes(answers: UserAnswers): Call = answers.get(EmploymentStatusPage).map {
    case EmploymentStatus.Employed     => routes.SalaryController.onPageLoad(NormalMode)
    case EmploymentStatus.SelfEmployed => routes.AnnualIncomeController.onPageLoad(NormalMode)
  }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  private val checkRouteMap: Page => UserAnswers => Call = {
    case EmploymentStatusPage => employmentStatusCheckRoutes
    case _                    => _ => routes.CheckYourAnswersController.onPageLoad
  }

  private def employmentStatusCheckRoutes(answers: UserAnswers): Call =
    answers.get(EmploymentStatusPage).map {
      case EmploymentStatus.Employed =>
        answers.get(SalaryPage) match {
          case Some(_) => routes.CheckYourAnswersController.onPageLoad
          case None    => routes.SalaryController.onPageLoad(CheckMode)
        }

      case EmploymentStatus.SelfEmployed =>
        answers.get(AnnualIncomePage) match {
          case Some(_) => routes.CheckYourAnswersController.onPageLoad
          case None    => routes.AnnualIncomeController.onPageLoad(CheckMode)
        }
    }.getOrElse(routes.JourneyRecoveryController.onPageLoad())

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
