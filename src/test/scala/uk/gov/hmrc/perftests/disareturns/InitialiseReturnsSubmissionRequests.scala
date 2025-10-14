/*
 * Copyright 2025 HM Revenue & Customs
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

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.disareturns.constant.AppConfig._
import uk.gov.hmrc.perftests.disareturns.constant.Headers.{initialiseReturnsSubmissionHeaders, reportingWindowHeaders}
import uk.gov.hmrc.perftests.disareturns.models.InitialiseReturnsSubmissionPayload

object InitialiseReturnsSubmissionRequests extends ServicesConfiguration {
  val reportingWindowPayload: JsObject = Json.obj("reportingWindowOpen" -> true)
  private val config                   = ConfigFactory.load()
  private val noOfJsons: Int           = config.getInt("saveMonthlyReturnLocally.no-of-json-lines")

  def getInitialiseReturnsSubmissionPayload(currentTaxYear: Int): InitialiseReturnsSubmissionPayload =
    InitialiseReturnsSubmissionPayload(noOfJsons * 6, "APR", currentTaxYear)

  val setReportingWindowsOpen: HttpRequestBuilder =
    http("Set reporting window as Open")
      .post(s"$disaReturnsStubHost$reportingWindowPath")
      .headers(reportingWindowHeaders)
      .body(StringBody(reportingWindowPayload.toString()))
      .check(status.is(204))
      .silent

  val setObligationStatusOpen: HttpRequestBuilder =
    http("Set Obligation status as Open")
      .post(s"$disaReturnsStubHost$obligationStatusPath#{isaManagerReference}")
      .check(status.is(200))
      .silent

  val submitInitialiseReturnsSubmission: HttpRequestBuilder =
    http("Submit 'initialise returns submission'")
      .post(s"$disaReturnsHost$disaReturnsRoute#{isaManagerReference}$initialiseReturnsSubmissionApiRoute")
      .disableFollowRedirect
      .headers(initialiseReturnsSubmissionHeaders)
      .body(StringBody { session =>
        val currentYear = java.time.Year.now().getValue
        val payload     = getInitialiseReturnsSubmissionPayload(currentYear)
        Json.toJson(payload).toString
      })
      .check(jsonPath("$.returnId").saveAs("returnId"))
      .check(status.is(200))
}
