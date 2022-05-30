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

import base.SpecBase
import models.{EmployedCalculation, EmploymentStatus, SelfEmployedCalculation}
import pages.{AnnualIncomePage, EmploymentStatusPage, SalaryPage}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.ResultViewModel
import views.html.ResultView

class ResultControllerSpec extends SpecBase {

  "Result Controller" - {

    "must return OK and the correct view for a GET for the employed" in {

      val salary           = BigDecimal(1)
      val employmentStatus = EmploymentStatus.Employed

      val answers =
        emptyUserAnswers
          .set(EmploymentStatusPage, employmentStatus).success.value
          .set(SalaryPage, salary).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.ResultController.onPageLoad().url)

        val result = route(application, request).value

        val view        = application.injector.instanceOf[ResultView]
        val calculation = EmployedCalculation(salary)
        val viewModel   = ResultViewModel(calculation)(messages(application))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(viewModel)(request, messages(application)).toString
      }
    }

    "must return OK and the correct view for a GET for the self-employed" in {

      val income           = BigDecimal(1)
      val employmentStatus = EmploymentStatus.SelfEmployed

      val answers =
        emptyUserAnswers
          .set(EmploymentStatusPage, employmentStatus).success.value
          .set(AnnualIncomePage, income).success.value

      val application = applicationBuilder(userAnswers = Some(answers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.ResultController.onPageLoad().url)

        val result = route(application, request).value

        val view        = application.injector.instanceOf[ResultView]
        val calculation = SelfEmployedCalculation.aprilToApril(income)
        val viewModel   = ResultViewModel(calculation)(messages(application))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(viewModel)(request, messages(application)).toString
      }
    }
  }
}
