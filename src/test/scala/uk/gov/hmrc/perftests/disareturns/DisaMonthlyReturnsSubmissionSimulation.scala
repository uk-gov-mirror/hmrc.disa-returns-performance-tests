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

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.disareturns.AuthRequests.getSubmissionBearerToken
import uk.gov.hmrc.perftests.disareturns.DisaMonthlyReturnsSubmissionRequests.submitMonthlyReport
import uk.gov.hmrc.perftests.disareturns.InitialiseReturnsSubmissionRequests.{setReportingWindowsOpen, submitInitialiseReturnsSubmission}
import uk.gov.hmrc.perftests.disareturns.PPNSServiceRequests.{createClientApplication, createNotificationBox, createSubscriptionFieldValues, createSubscriptionFields}

class DisaMonthlyReturnsSubmissionSimulation extends PerformanceTestRunner {

  setup("Get-Bearer-Token", "Get Bearer Token")
    .withRequests(
      getSubmissionBearerToken
    )

  setup("PPNS-Setup", "Setup PPNS for a Box Id")
    .withRequests(
      createClientApplication,
      createNotificationBox,
      createSubscriptionFields,
      createSubscriptionFieldValues
    )

  setup("Disa-Monthly-returns-Submission", "Disa Monthly returns submission")
    .withRequests(setReportingWindowsOpen, submitInitialiseReturnsSubmission, submitMonthlyReport)

  runSimulation()
}
