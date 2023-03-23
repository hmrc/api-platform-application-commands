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

import java.time.LocalDateTime

import play.api.libs.json.{Json, Format}
import uk.gov.hmrc.play.json.Union

import uk.gov.hmrc.apiplatform.modules.applications.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.models._

sealed trait ApplicationCommand {
  // TODO - remove this at earliest opportunity
  def timestamp: LocalDateTime
}

case class AddCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime)                                                          extends ApplicationCommand
case class RemoveCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime)                                                       extends ApplicationCommand

object ApplicationCommand {
  import uk.gov.hmrc.apiplatform.modules.common.domain.services.LocalDateTimeFormatter._
  
  private implicit val addCollaboratorFormatter                        = Json.format[AddCollaborator]
  private implicit val removeCollaboratorFormatter                     = Json.format[RemoveCollaborator]

  implicit val formatter = Union.from[ApplicationCommand]("updateType")
    .and[AddCollaborator]("addCollaborator")
    .and[RemoveCollaborator]("removeCollaborator")
    .format
}

