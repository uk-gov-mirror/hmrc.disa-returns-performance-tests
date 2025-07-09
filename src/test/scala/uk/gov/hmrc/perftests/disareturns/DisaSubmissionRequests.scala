/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.disareturns

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.disareturns.Util.FileUtils.updateNdjsonWithNino
import uk.gov.hmrc.perftests.disareturns.Util.RandomDataGenerator
import uk.gov.hmrc.perftests.disareturns.Util.SubPathGenerator.generateSubpath

object DisaSubmissionRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("disa-return")
  val route: String   = "/monthly-v3/"

  val sessionHeaders: Map[String, String] =
    Map(
      "Content-Type" -> "application/x-ndjson"
    )

  val ninoGenerator: Iterator[Map[String, String]] =
    Iterator.continually(Map("nino" -> RandomDataGenerator.nino()))

  val stdCodeGenerator: Iterator[Map[String, String]] =
    Iterator.continually(Map("stdCode" -> RandomDataGenerator.generateSTDCode()))

  val oldCodeGenerator: Iterator[Map[String, String]] =
    Iterator.continually(Map("oldCode" -> RandomDataGenerator.generateOLDCode()))

  val submitMonthlyReport: HttpRequestBuilder =
    http("Submit monthly report")
      .post { session =>
        val url = s"$baseUrl$route/" + generateSubpath()
        url
      }
      .headers(sessionHeaders)
      .body(StringBody { session =>
        val payload = updateNdjsonWithNino("Submission1")
        payload
      })
      .check(status.is(200))
}
