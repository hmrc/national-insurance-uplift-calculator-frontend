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

package forms

import forms.mappings.Mappings
import models.EmploymentStatus

import javax.inject.Inject
import play.api.data.Form

class SalaryFormProvider @Inject() extends Mappings {

  def apply(employmentStatus: EmploymentStatus): Form[BigDecimal] =
    Form(
      "value" -> currency(
        s"salary.${employmentStatus.toString}.error.required",
        s"salary.${employmentStatus.toString}.error.invalidNumeric",
        s"salary.${employmentStatus.toString}.error.nonNumeric")
          .verifying(minimumCurrency(1, s"salary.${employmentStatus.toString}.error.belowMinimum"))
          .verifying(maximumCurrency(1000000, s"salary.${employmentStatus.toString}.error.aboveMaximum"))
    )
}
