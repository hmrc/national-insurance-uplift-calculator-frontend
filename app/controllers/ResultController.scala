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

package controllers

import controllers.actions._
import models.{Calculation, EmployedCalculation, SelfEmployedCalculation}
import models.EmploymentStatus.{Employed, SelfEmployed}
import pages.{EmploymentStatusPage, SalaryPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.ResultViewModel
import views.html.ResultView

import javax.inject.Inject

class ResultController @Inject()(
                                  override val messagesApi: MessagesApi,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  val controllerComponents: MessagesControllerComponents,
                                  view: ResultView
                                ) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      (request.userAnswers.get(EmploymentStatusPage), request.userAnswers.get(SalaryPage)) match {
        case (Some(employmentStatus), Some(salary)) =>

          val calculation: Calculation = employmentStatus match {
            case Employed     => EmployedCalculation(salary)
            case SelfEmployed => SelfEmployedCalculation.aprilToApril(salary)
          }

          val viewModel   = ResultViewModel(calculation)

          Ok(view(viewModel))

        case _ => Redirect(routes.JourneyRecoveryController.onPageLoad())
      }
  }
}
