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

package uk.gov.hmrc.perftests.disareturns.constant

object Headers {

  val headerOnlyWithBearerToken: Map[String, String] = Map(
    "Authorization" -> "#{bearerToken}"
  )

  val headerWithClientIdForStagingTests: Map[String, String] = {
    if (EnvConfig.isLocal)
      Map(
        "X-Client-ID" -> ""
      )
    else
      Map(
        "X-Client-ID" -> "#{stagingClientIDs}"
      )
   }

  val reportingWindowHeaders: Map[String, String] = Map(
    "Content-Type" -> "application/json"
  )

  val headerWithBearerTokenAndContentTypeJson: Map[String, String] = Map(
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

}

object EnvConfig {
  def isLocal: Boolean = sys.props.get("runLocal").exists(_.equalsIgnoreCase("true"))
}
