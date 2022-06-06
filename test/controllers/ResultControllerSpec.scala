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
import metrics.{MetricsService, Monitors}
import models.Calculation
import org.mockito.ArgumentMatchers.{eq => eqTo}
import org.mockito.Mockito.{times, verify}
import org.scalatestplus.mockito.MockitoSugar
import pages.SalaryPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.ResultViewModel
import views.html.ResultView

class ResultControllerSpec extends SpecBase with MockitoSugar {

  "Result Controller" - {

    "must return OK and the correct view for a GET, incrementing a metric counter" in {

      val mockMetricsService = mock[MetricsService]

      val salary = BigDecimal(1)

      val answers = emptyUserAnswers.set(SalaryPage, salary).success.value

      val application =
        applicationBuilder(userAnswers = Some(answers))
          .overrides(bind[MetricsService].toInstance(mockMetricsService))
          .build()

      running(application) {
        val request = FakeRequest(GET, routes.ResultController.onPageLoad().url)

        val result = route(application, request).value

        val view        = application.injector.instanceOf[ResultView]
        val calculation = Calculation(salary)
        val viewModel   = ResultViewModel(calculation)(messages(application))

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(viewModel)(request, messages(application)).toString
        verify(mockMetricsService, times(1)).inc(eqTo(Monitors.calculationOutcome(calculation.saving)))
      }
    }
  }
}
