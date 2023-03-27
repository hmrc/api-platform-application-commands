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

import uk.gov.hmrc.apiplatform.modules.applications.domain.models.ClientSecret
import play.api.libs.json.Json

class ClientSecretDetailsSpec extends ApplicationCommandBaseSpec {

  val aClientSecretDetails = ClientSecretDetails("aName", aTimestamp, None, ClientSecret.Id.random, "blahblahsecret")

  "ClientSecretDetails" should {
    "write to json" in {
      Json.toJson[ClientSecretDetails](aClientSecretDetails) shouldBe Json.obj(
        "name"         -> "aName",
        "createdOn"    -> s"$nowAsText",
        "id"           -> s"${aClientSecretDetails.id.value}",
        "hashedSecret" -> "blahblahsecret"
      )
    }

    "read from json" in {
      val jsonText =
        s""" {"name":"aName","createdOn":"$nowAsText","id":"${aClientSecretDetails.id.value}","hashedSecret":"blahblahsecret"} """

      Json.parse(jsonText).as[ClientSecretDetails] shouldBe aClientSecretDetails
    }
  }
  
}
