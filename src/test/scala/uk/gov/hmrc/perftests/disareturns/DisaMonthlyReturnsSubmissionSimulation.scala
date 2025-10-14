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
import uk.gov.hmrc.perftests.disareturns.CompleteMonthlyReturnRequests.submitDeclaration
import uk.gov.hmrc.perftests.disareturns.DisaMonthlyReturnsSubmissionRequests.submitMonthlyReport
import uk.gov.hmrc.perftests.disareturns.InitialiseReturnsSubmissionRequests.{setObligationStatusOpen, setReportingWindowsOpen, submitInitialiseReturnsSubmission}
import uk.gov.hmrc.perftests.disareturns.PPNSServiceRequests.{createClientApplication, createNotificationBox, createSubscriptionFields}
import uk.gov.hmrc.perftests.disareturns.ReconciliationReportService.{getReportingResultsSummary, makeReturnSummaryCallback, triggerReportReadyScenario}

class DisaMonthlyReturnsSubmissionSimulation extends PerformanceTestRunner {

  setup("Get-Bearer-Token", "Get Bearer Token")
    .withRequests(
      getSubmissionBearerToken
    )

  setup("PPNS-Setup", "Setup PPNS for a Box Id")
    .withRequests(
      createClientApplication,
      createNotificationBox,
      createSubscriptionFields
    )

  setup("Disa-Monthly-returns-Submission", "Disa Monthly returns submission")
    .withRequests(
      setReportingWindowsOpen,
      setObligationStatusOpen,
      submitInitialiseReturnsSubmission,
      submitMonthlyReport,
      submitDeclaration,
      setObligationStatusOpen
    )

  setup("Reconciliation-Report-Journey-1", "Reconciliation Report Journey through call back api")
    .withRequests(
      makeReturnSummaryCallback,
      getReportingResultsSummary
    )

  setup("Reconciliation-Report-Journey-2", "Reconciliation Report Journey through test support api")
    .withRequests(
      triggerReportReadyScenario,
      getReportingResultsSummary
    )

  runSimulation()
}
