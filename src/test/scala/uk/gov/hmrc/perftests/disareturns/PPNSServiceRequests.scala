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

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

import scala.io.Source

object PPNSServiceRequests extends ServicesConfiguration {
  val third_party_application_host: String   = baseUrlFor("third-party-application")
  val ppns_host: String                      = baseUrlFor("push-pull-notification")
  val api_subscription_fields_host: String   = baseUrlFor("api-subscription-fields")
  val thirdPartyApplicationPath: String      = "/application"
  val ppnsPath: String                       = "/box"
  val subscriptionPath                       = "/definition/context/disa-returns/version/1.0"
  val subscriptionFieldValuesPath            = "/field/application/clientId/context/disa-returns/version/1.0"
  val clientApplicationPayload: String       =
    Source.fromResource("data/ClientApplication.json").getLines().mkString
  val notificationBoxPayload: String         =
    Source.fromResource("data/NotificationBox.json").getLines().mkString
  val subscriptionFieldsPayload: String      =
    Source.fromResource("data/SubscriptionFields.json").getLines().mkString
  val subscriptionFieldValuesPayload: String =
    Source.fromResource("data/SubscriptionFieldValues.json").getLines().mkString

  val thirdpartyApplicationHadersMap: Map[String, String] = Map(
    "Content-Type"  -> "application/json",
    "Authorization" -> "#{bearerToken}"
  )

  val notificationBoxHadersMap: Map[String, String] = Map(
    "Content-Type"  -> "application/json",
    "Authorization" -> "#{bearerToken}",
    "User-Agent"    -> "disa-returns"
  )

  val subscriptionFieldsHeadersMap: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  def createClientApplication: HttpRequestBuilder =
    http("Create Client Application")
      .post(third_party_application_host + thirdPartyApplicationPath)
      .headers(thirdpartyApplicationHadersMap)
      .body(StringBody(clientApplicationPayload))
      .asJson
      .check(status.is(201))
      .check(jsonPath("$.details.clientId").saveAs("clientId"))
      .silent

  def createNotificationBox: HttpRequestBuilder =
    http("Create Notification Box")
      .put(ppns_host + ppnsPath)
      .headers(notificationBoxHadersMap)
      .body(StringBody(notificationBoxPayload))
      .asJson
      .check(status.is(201))
      .check(jsonPath("$.boxId").saveAs("boxId"))
      .silent

  def createSubscriptionFields: HttpRequestBuilder =
    http("Create Subscription Fields")
      .put(api_subscription_fields_host + subscriptionPath)
      .headers(subscriptionFieldsHeadersMap)
      .body(StringBody(subscriptionFieldsPayload))
      .asJson
      .check(status.is(200))
      .silent

  def createSubscriptionFieldValues: HttpRequestBuilder =
    http("Create Subscription Field values ")
      .put(api_subscription_fields_host + subscriptionFieldValuesPath.replace("clientId", "#{clientId}"))
      .headers(subscriptionFieldsHeadersMap)
      .body(StringBody(subscriptionFieldValuesPayload))
      .asJson
      .check(status.is(201))
      .silent
}
