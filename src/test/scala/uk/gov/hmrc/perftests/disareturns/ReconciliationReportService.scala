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

object ReconciliationReportService extends ServicesConfiguration {

  val disaReturnsBaseUrl: String            = baseUrlFor("disa-returns")
  val disaReturnsTestSupportBaseUrl: String = baseUrlFor("disa-returns-test-support-api")
  val route: String                         = "/monthly/"
  val callBackendPointPath: String          = "/callback/monthly/"
  val testSupportPath: String               = "/reconciliation"
  val reportingResultsSummaryPath: String   = "/results/summary"

  val monthlyReturnsSubmissionHeaders: Map[String, String] = Map(
    "X-Client-ID"   -> "#{clientId}",
    "Authorization" -> "#{bearerToken}"
  )

  val testSupportHeaders: Map[String, String] = Map(
    "Authorization" -> "#{bearerToken}",
    "Content-Type"  -> "application/json"
  )

  val testSupportHeadersWithAuthOnly: Map[String, String] = Map(
    "Authorization" -> "#{bearerToken}"
  )

  val makeReturnSummaryCallback: HttpRequestBuilder =
    http("Make return summary callback")
      .post(disaReturnsBaseUrl + callBackendPointPath + "#{isaManagerReference}" + "/2025-26/" + "/" + "#{month}")
      .headers(testSupportHeaders)
      .body(StringBody { session =>
        val payload = s"""
                          |{
                          |  "totalRecords": 1000
                          |}
                          |""".stripMargin
        payload
      })
      .check(status.is(204))

  val triggerReportReadyScenario: HttpRequestBuilder =
    http("Trigger report ready scenario")
      .post(
        disaReturnsTestSupportBaseUrl + "/" + "#{isaManagerReference}" + "/2025-26/" + "/" + "#{month}" + testSupportPath
      )
      .headers(testSupportHeaders)
      .body(StringBody { session =>
        val payload = s"""
         {
                         |    "oversubscribed": 1,
                         |    "traceAndMatch": 2,
                         |    "failedEligibility": 3
                         |}""".stripMargin
        payload
      })
      .check(status.is(204))

  val getReportingResultsSummary: HttpRequestBuilder =
    http("Get Reporting Results Summary")
      .get(
        disaReturnsBaseUrl + route + "#{isaManagerReference}" + "/2025-26/" + "/" + "#{month}" + reportingResultsSummaryPath
      )
      .headers(testSupportHeadersWithAuthOnly)
      .check(status.is(200))
}
