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

import config.CurrencyFormatter
import forms.behaviours.CurrencyFieldBehaviours
import play.api.data.FormError

class AnnualIncomeFormProviderSpec extends CurrencyFieldBehaviours {

  val form = new AnnualIncomeFormProvider()()

  ".value" - {

    val fieldName = "value"

    val minimum = 1
    val maximum = 1000000000

    val validDataGenerator = intsInRangeWithCommas(minimum, maximum)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like currencyField(
      form,
      fieldName,
      nonNumericError  = FormError(fieldName, "annualIncome.error.nonNumeric"),
      invalidNumericError = FormError(fieldName, "annualIncome.error.invalidNumeric")
    )

    behave like currencyFieldWithMinimum(
      form,
      fieldName,
      minimum,
      FormError(fieldName, "annualIncome.error.belowMinimum", Seq(CurrencyFormatter.currencyFormat(minimum)))
    )

    behave like currencyFieldWithMaximum(
      form,
      fieldName,
      maximum,
      FormError(fieldName, "annualIncome.error.aboveMaximum", Seq(CurrencyFormatter.currencyFormat(maximum)))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "annualIncome.error.required")
    )
  }
}
