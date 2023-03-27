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

package uk.gov.hmrc.apiplatform.modules.application.commands.domain.models

import play.api.libs.json.Json

class DeleteProductionCredentialsApplicationSpec extends ApplicationCommandBaseSpec {

  "DeleteProductionCredentialsApplication" should {
    val cmd = ApplicationCommands.DeleteProductionCredentialsApplication(aScheduledJob.jobId, reasons, aTimestamp)

    "write to json (as a command)" in {

      Json.toJson[ApplicationCommand](cmd) shouldBe Json.obj(
        "jobId"      -> s"${aScheduledJob.jobId}",
        "reasons"    -> s"$reasons",
        "timestamp"  -> s"$nowAsText",
        "updateType" -> "deleteProductionCredentialsApplication"
      )
    }

    "read from json" in {
      val jsonText =
        s""" {"jobId":"${aScheduledJob.jobId}","reasons":"$reasons","timestamp":"$nowAsText","updateType":"deleteProductionCredentialsApplication"} """

      Json.parse(jsonText).as[ApplicationCommand] shouldBe cmd
    }
  }
}
