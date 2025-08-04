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
import play.api.libs.json.Json
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.disareturns.Util.RandomDataGenerator
import uk.gov.hmrc.perftests.disareturns.models.MonthlyReturnsPayload

object DisaMonthlyReturnsSubmissionRequests extends ServicesConfiguration {

  val disaReturnsBaseUrl: String = baseUrlFor("disa-returns")
  val route: String              = "/monthly/"

  def getMonthlyReturnsPayload(
    nino: String,
    accountNumber: String,
    accountNumberOfTransferringAccount: String
  ): MonthlyReturnsPayload = MonthlyReturnsPayload(
    "First24997",
    null,
    "Last24997",
    "1980-01-22",
    "STOCKS_AND_SHARES",
    true,
    "2025-06-01",
    2500.00,
    10000.00,
    5000.00,
    false,
    nino,
    accountNumber,
    accountNumberOfTransferringAccount
  )

  val submitMonthlyReport: HttpRequestBuilder =
    http("Submit monthly report")
      .post(disaReturnsBaseUrl + route + "${isaManagerReference}" + "/" + "${returnId}")
      .body(StringBody { session =>
        val nino                               = RandomDataGenerator.nino()
        val accountNumber                      = RandomDataGenerator.generateSTDCode()
        val accountNumberOfTransferringAccount = RandomDataGenerator.generateOLDCode()
        val payload                            = getMonthlyReturnsPayload(nino, accountNumber, accountNumberOfTransferringAccount)
        Json.stringify(Json.toJson(payload))
      })
      .check(status.is(201))
}
