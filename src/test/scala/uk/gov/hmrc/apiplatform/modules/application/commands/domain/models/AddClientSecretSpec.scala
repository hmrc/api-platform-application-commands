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

import play.api.libs.json.Json

import uk.gov.hmrc.apiplatform.modules.application.commands.domain.models._
import uk.gov.hmrc.apiplatform.modules.applications.domain.models.ClientSecret
import uk.gov.hmrc.apiplatform.modules.common.domain.models._

class AddClientSecretSpec extends ApplicationCommandBaseSpec {
  val aClientSecret = ClientSecret("aName", aTimestamp, None, ClientSecret.Id.random, "blahblahsecret")

  "AddClientSecret" should {
    val cmd = ApplicationCommands.AddClientSecret(Actors.AppCollaborator(anActorEmail), aClientSecret, aTimestamp)

    "write to json (as a command)" in {

      Json.toJson[ApplicationCommand](cmd) shouldBe Json.obj(
        "actor"        -> Json.obj(
          "email" -> "bob@example.com"
        ),
        "clientSecret" -> Json.obj(
          "name"         -> "aName",
          "createdOn"    -> s"$nowAsText",
          "id"           -> s"${aClientSecret.id.value}",
          "hashedSecret" -> "blahblahsecret"
        ),
        "timestamp"    -> s"$nowAsText",
        "updateType"   -> "addClientSecret"
      )
    }

    "read from json" in {
      val jsonText =
        s""" {"actor":{"email":"bob@example.com"},"clientSecret":{"name":"aName","createdOn":"$nowAsText","id":"${aClientSecret.id.value}","hashedSecret":"blahblahsecret"},"timestamp":"$nowAsText","updateType":"addClientSecret"} """

      Json.parse(jsonText).as[ApplicationCommand] shouldBe cmd
    }
  }
}
