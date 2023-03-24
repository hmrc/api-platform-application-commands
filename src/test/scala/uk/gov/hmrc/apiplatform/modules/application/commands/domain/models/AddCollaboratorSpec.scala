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

package uk.gov.hmrc.apiplatform.modules.application.commands

import uk.gov.hmrc.apiplatform.modules.common.domain.models._
import play.api.libs.json.Json
import java.time.temporal.ChronoUnit
import uk.gov.hmrc.apiplatform.modules.application.commands.domain.models._


class AddCollaboratorSpec extends ApplicationCommandBaseSpec {

  "AddCollaborator" should {
    val cmd = ApplicationCommands.AddCollaborator(Actors.AppCollaborator(anActorEmail), aCollaborator, aTimestamp)

    "write to json (as a command)" in {

      Json.toJson[ApplicationCommand](cmd) shouldBe Json.obj(
        "actor" -> Json.obj(
          "email" -> "bob@example.com",
          "actorType" -> "COLLABORATOR"
        ),
        "collaborator" -> Json.obj(
          "emailAddress" -> "alice@example.com",
          "role" -> "DEVELOPER",
          "userId" -> s"${aUserId.value}"
        ),
        "timestamp" -> "2020-01-01T12:01:02.003Z",
        "updateType" -> "addCollaborator"
      )
    }

    "read from json" in {
      val jsonText = s""" {"actor":{"email":"bob@example.com","actorType":"COLLABORATOR"},"collaborator":{"emailAddress":"alice@example.com","role":"DEVELOPER","userId":"${aUserId.value}"},"timestamp":"2020-01-01T12:01:02.003","updateType":"addCollaborator"} """

      Json.parse(jsonText).as[ApplicationCommand] shouldBe cmd.copy(timestamp = cmd.timestamp.truncatedTo(ChronoUnit.MILLIS))
    }
  }
}