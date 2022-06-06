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

package metrics

import com.codahale.metrics.MetricRegistry

import javax.inject.Inject

class MetricsService @Inject()(kenshooMetrics: com.kenshoo.play.metrics.Metrics) {

  private val registry: MetricRegistry = kenshooMetrics.defaultRegistry

  def inc(metric: Counter): Unit = registry.counter(metric.name).inc()
}

case class Counter(name: String)

object Monitors {
  def calculationOutcome(saving: BigDecimal): Counter = {
    if (saving < BigDecimal(0))       Counter("CalculationOutcome.willPayMore")
    else if (saving == BigDecimal(0)) Counter("CalculationOutcome.willPayTheSame")
    else                              Counter("CalculationOutcome.willPayLess")
  }
}
