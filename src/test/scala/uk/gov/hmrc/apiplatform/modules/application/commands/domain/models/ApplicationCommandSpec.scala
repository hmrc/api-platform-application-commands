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

import uk.gov.hmrc.apiplatform.utils.HmrcSpec
import uk.gov.hmrc.apiplatform.modules.common.domain.models.Actors
import uk.gov.hmrc.apiplatform.modules.common.domain.models.LaxEmailAddress.StringSyntax
import uk.gov.hmrc.apiplatform.modules.applications.domain.models.Collaborator
import uk.gov.hmrc.apiplatform.utils.CollaboratorsSyntax._
import java.time.LocalDateTime
import play.api.libs.json.Json
import uk.gov.hmrc.apiplatform.modules.developers.domain.models.UserId
import java.time.temporal.ChronoUnit


class ApplicationCommandSpec extends HmrcSpec {

  val anActorEmail = "bob@example.com".toLaxEmail
  val aCollaboratorEmail = "alice@example.com".toLaxEmail
  val aUserId = UserId.random
  val ThreeMillisFourNanos = 3 * 1000 * 1000 + 4
  val aCollaborator: Collaborator = aCollaboratorEmail.asDeveloper().copy(userId = aUserId)
  val aTimestamp = LocalDateTime.of(2020,1,1,12,1,2,ThreeMillisFourNanos)

  "An ApplicationCommand" when {
    "an AddCollaborator" should {
      val cmd = AddCollaborator(Actors.AppCollaborator(anActorEmail), aCollaborator, aTimestamp)

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

    "a RemoveCollaborator" should {
      val cmd = RemoveCollaborator(Actors.AppCollaborator(anActorEmail), aCollaborator, aTimestamp)

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
          "updateType" -> "removeCollaborator"
        )
      }

      "read from json" in {
        val jsonText = s""" {"actor":{"email":"bob@example.com","actorType":"COLLABORATOR"},"collaborator":{"emailAddress":"alice@example.com","role":"DEVELOPER","userId":"${aUserId.value}"},"timestamp":"2020-01-01T12:01:02.003","updateType":"removeCollaborator"} """

        Json.parse(jsonText).as[ApplicationCommand] shouldBe cmd.copy(timestamp = cmd.timestamp.truncatedTo(ChronoUnit.MILLIS))
      }
    }
  }
}
