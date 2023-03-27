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

import play.api.libs.json.Json
import uk.gov.hmrc.play.json.Union

import uk.gov.hmrc.apiplatform.modules.applications.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.models._
import uk.gov.hmrc.apiplatform.modules.developers.domain.models.UserId
import uk.gov.hmrc.apiplatform.modules.apis.domain.models.ApiIdentifier

sealed trait ApplicationCommand {
  // TODO - remove this at earliest opportunity
  def timestamp: LocalDateTime
}

object ApplicationCommands {
  case class AddClientSecret(actor: Actors.AppCollaborator, clientSecret: ClientSecret, timestamp: LocalDateTime)                                         extends ApplicationCommand

  case class AddCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime)    extends ApplicationCommand

  case class ChangeProductionApplicationPrivacyPolicyLocation(instigator: UserId, timestamp: LocalDateTime, newLocation: PrivacyPolicyLocation)           extends ApplicationCommand

  case class ChangeProductionApplicationTermsAndConditionsLocation(instigator: UserId, timestamp: LocalDateTime, newLocation: TermsAndConditionsLocation) extends ApplicationCommand

  case class ChangeResponsibleIndividualToSelf(instigator: UserId, timestamp: LocalDateTime, name: String, email: LaxEmailAddress)                        extends ApplicationCommand
  
  case class ChangeResponsibleIndividualToOther(code: String, timestamp: LocalDateTime)                                                                   extends ApplicationCommand
  
  case class DeclineResponsibleIndividual(code: String, timestamp: LocalDateTime)                                                                         extends ApplicationCommand
  
  case class DeclineResponsibleIndividualDidNotVerify(code: String, timestamp: LocalDateTime)                                                             extends ApplicationCommand
  
  case class DeleteApplicationByCollaborator(instigator: UserId, reasons: String, timestamp: LocalDateTime)                                               extends ApplicationCommand
  
  case class DeleteProductionCredentialsApplication(jobId: String, reasons: String, timestamp: LocalDateTime)                                             extends ApplicationCommand
  
  case class DeleteUnusedApplication(actor: Actors.ScheduledJob, authorisationKey: String, reasons: String, timestamp: LocalDateTime)                                  extends ApplicationCommand
  
  case class RemoveClientSecret(actor: Actors.AppCollaborator, clientSecretId: ClientSecret.Id, timestamp: LocalDateTime)                                          extends ApplicationCommand
  case class RemoveCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime) extends ApplicationCommand
  case class SubscribeToApi(actor: Actor, apiIdentifier: ApiIdentifier, timestamp: LocalDateTime)                                                         extends ApplicationCommand
  
  case class UnsubscribeFromApi(actor: Actor, apiIdentifier: ApiIdentifier, timestamp: LocalDateTime)                                                     extends ApplicationCommand
  
  case class UpdateRedirectUris(actor: Actor, oldRedirectUris: List[String], newRedirectUris: List[String], timestamp: LocalDateTime)                     extends ApplicationCommand
  case class VerifyResponsibleIndividual(instigator: UserId, timestamp: LocalDateTime, requesterName: String, riName: String, riEmail: LaxEmailAddress)   extends ApplicationCommand
}

sealed trait GatekeeperSpecificApplicationCommand extends ApplicationCommand {
  def gatekeeperUser: String
}

case class ChangeProductionApplicationName(instigator: UserId, timestamp: LocalDateTime, gatekeeperUser: String, newName: String) extends GatekeeperSpecificApplicationCommand
case class DeclineApplicationApprovalRequest(gatekeeperUser: String, reasons: String, timestamp: LocalDateTime)                   extends GatekeeperSpecificApplicationCommand

case class DeleteApplicationByGatekeeper(gatekeeperUser: String, requestedByEmailAddress: LaxEmailAddress, reasons: String, timestamp: LocalDateTime)
    extends GatekeeperSpecificApplicationCommand

/* 
--- Used in TPDFE ---
  *** Extends ApplicationUpdate ***
  ChangeProductionApplicationPrivacyPolicyLocation
  ChangeProductionApplicationTermsAndConditionsLocation
  ChangeResponsibleIndividualToSelf
  ChangeResponsibleIndividualToOther
  DeclineResponsibleIndividual
  RemoveClientSecret
  VerifyResponsibleIndividual
  DeleteApplicationByCollaborator

  *** Extends ApplicationCommand ***
  AddCollaborator
  RemoveCollaborator

--- Used in GKFE--- 
  *** Extends ApplicationCommand ***
  AddCollaborator
  RemoveCollaborator

  *** Extends Application Update ***
  ChangeProductionApplicationName
  DeleteApplicationByGatekeeper


--- Used in GKAFE ---
  *** extends GatekeeperApplicationUpdate ***
  DeclineApplicationApprovalRequest 

--- Used in APJ ---
  ***  extends ApplicationUpdate ***
  DeleteUnusedApplication

  *** Extends ApplicationCommand ***
  AddCollaborator
  RemoveCollaborator


--- Used in APM ---
  ***   extends UpdateRequest ***
  AddCollaboratorRequest
  RemoveCollaboratorRequest

  ***  extends ApplicationUpdate ***
  AddCollaborator
  RemoveCollaborator
  SubscribeToApi
  UnsubscribeFromApi
  UpdateRedirectUris



*/

object ApplicationCommand {
  import ApplicationCommands._
  import uk.gov.hmrc.apiplatform.modules.common.domain.services.LocalDateTimeFormatter._

  private implicit val addCollaboratorFormatter    = Json.format[AddCollaborator]
  private implicit val removeCollaboratorFormatter = Json.format[RemoveCollaborator]
  private implicit val addClientSecretFormatter    = Json.format[AddClientSecret]
  private implicit val removeClientSecretFormatter    = Json.format[RemoveClientSecret]

  implicit val changePrivacyPolicyLocationFormatter               = Json.format[ChangeProductionApplicationPrivacyPolicyLocation]
  implicit val changeTermsAndConditionsLocationFormatter          = Json.format[ChangeProductionApplicationTermsAndConditionsLocation]
  implicit val changeResponsibleIndividualToSelfFormatter         = Json.format[ChangeResponsibleIndividualToSelf]
  implicit val changeResponsibleIndividualToOtherFormatter        = Json.format[ChangeResponsibleIndividualToOther]
  implicit val verifyResponsibleIndividualFormatter               = Json.format[VerifyResponsibleIndividual]
  implicit val declineResponsibleIndividualFormatter              = Json.format[DeclineResponsibleIndividual]
  implicit val declineResponsibleIndividualDidNotVerifyFormatter  = Json.format[DeclineResponsibleIndividualDidNotVerify]
  implicit val deleteApplicationByCollaboratorFormatter           = Json.format[DeleteApplicationByCollaborator]
  implicit val deleteUnusedApplicationFormatter                   = Json.format[DeleteUnusedApplication]
  implicit val deleteProductionCredentialsApplicationFormatter    = Json.format[DeleteProductionCredentialsApplication]
  implicit val subscribeToApiFormatter                            = Json.format[SubscribeToApi]
  implicit val unsubscribeFromApiFormatter                        = Json.format[UnsubscribeFromApi]
  implicit val UpdateRedirectUrisFormatter                        = Json.format[UpdateRedirectUris]


  implicit val formatter = Union.from[ApplicationCommand]("updateType")
    .and[AddCollaborator]("addCollaborator")
    .and[RemoveCollaborator]("removeCollaborator")
    .and[AddClientSecret]("addClientSecret")
    .and[RemoveClientSecret]("removeClientSecret")
    .and[ChangeProductionApplicationPrivacyPolicyLocation]("changeProductionApplicationPrivacyPolicyLocation")
    .and[ChangeProductionApplicationTermsAndConditionsLocation]("changeProductionApplicationTermsAndConditionsLocation")
    .and[ChangeResponsibleIndividualToSelf]("changeResponsibleIndividualToSelf")
    .and[ChangeResponsibleIndividualToOther]("changeResponsibleIndividualToOther")
    .and[VerifyResponsibleIndividual]("verifyResponsibleIndividual")
    .and[DeclineResponsibleIndividual]("declineResponsibleIndividual")
    .and[DeclineResponsibleIndividualDidNotVerify]("declineResponsibleIndividualDidNotVerify")
    .and[DeleteApplicationByCollaborator]("deleteApplicationByCollaborator")
    .and[DeleteUnusedApplication]("deleteUnusedApplication")
    .and[DeleteProductionCredentialsApplication]("deleteProductionCredentialsApplication")
    .and[SubscribeToApi]("subscribeToApi")
    .and[UnsubscribeFromApi]("unsubscribeFromApi")
    .and[UpdateRedirectUris]("updateRedirectUris")
    .format
}
