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

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.play.json.Union

sealed trait CommandFailure

object CommandFailures {
  case object ApplicationNotFound             extends CommandFailure
  case object CannotRemoveLastAdmin           extends CommandFailure
  case object ActorIsNotACollaboratorOnApp    extends CommandFailure
  case object CollaboratorDoesNotExistOnApp   extends CommandFailure
  case object CollaboratorHasMismatchOnApp    extends CommandFailure
  case object CollaboratorAlreadyExistsOnApp  extends CommandFailure
  case class GenericFailure(describe: String) extends CommandFailure
}

object CommandFailure {
  import CommandFailures._

  implicit private val formatApplicationNotFound            = Json.format[ApplicationNotFound.type]
  implicit private val formatCannotRemoveLastAdmin          = Json.format[CannotRemoveLastAdmin.type]
  implicit private val formatActorIsNotACollaboratorOnApp   = Json.format[ActorIsNotACollaboratorOnApp.type]
  implicit private val formatCollaboratorDoesNotExistOnApp  = Json.format[CollaboratorDoesNotExistOnApp.type]
  implicit private val formatCollaboratorHasMismatchOnApp   = Json.format[CollaboratorHasMismatchOnApp.type]
  implicit private val formatCollaboratorAlreadyExistsOnApp = Json.format[CollaboratorAlreadyExistsOnApp.type]
  implicit private val formatGenericFailure                 = Json.format[GenericFailure]

  implicit val format: Format[CommandFailure] = Union.from[CommandFailure]("failureType")
    .and[ApplicationNotFound.type]("ApplicationNotFound")
    .and[CannotRemoveLastAdmin.type]("CannotRemoveLastAdmin")
    .and[ActorIsNotACollaboratorOnApp.type]("ActorIsNotACollaboratorOnApp")
    .and[CollaboratorDoesNotExistOnApp.type]("CollaboratorDoesNotExistOnApp")
    .and[CollaboratorHasMismatchOnApp.type]("CollaboratorHasMismatchOnApp")
    .and[CollaboratorAlreadyExistsOnApp.type]("CollaboratorAlreadyExistsOnApp")
    .and[GenericFailure]("GenericFailure")
    .format
}