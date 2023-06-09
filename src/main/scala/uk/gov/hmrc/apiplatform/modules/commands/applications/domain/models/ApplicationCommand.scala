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

package uk.gov.hmrc.apiplatform.modules.commands.applications.domain.models

import java.time.LocalDateTime

import play.api.libs.json.Json
import uk.gov.hmrc.play.json.Union

import uk.gov.hmrc.apiplatform.modules.apis.domain.models.ApiIdentifier
import uk.gov.hmrc.apiplatform.modules.applications.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.models._
import uk.gov.hmrc.apiplatform.modules.developers.domain.models.UserId

sealed trait ApplicationCommand {
  // TODO - remove this at earliest opportunity
  def timestamp: LocalDateTime
}

sealed trait GatekeeperApplicationCommand extends ApplicationCommand {
  def gatekeeperUser: String
}

object ApplicationCommands {
  case class AddClientSecret(actor: Actors.AppCollaborator, name: String, id: ClientSecret.Id, hashedSecret: String, timestamp: LocalDateTime)            extends ApplicationCommand
  case class AddCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime)                                                          extends ApplicationCommand
  case class AddRedirectUri(actor: Actor, redirectUriToAdd: RedirectUri, timestamp: LocalDateTime)                                                        extends ApplicationCommand
  case class ChangeProductionApplicationName(gatekeeperUser: String, instigator: UserId, timestamp: LocalDateTime, newName: String)                       extends GatekeeperApplicationCommand
  case class ChangeProductionApplicationPrivacyPolicyLocation(instigator: UserId, timestamp: LocalDateTime, newLocation: PrivacyPolicyLocation)           extends ApplicationCommand
  case class ChangeProductionApplicationTermsAndConditionsLocation(instigator: UserId, timestamp: LocalDateTime, newLocation: TermsAndConditionsLocation) extends ApplicationCommand
  case class ChangeRedirectUri(actor: Actor, redirectUriToReplace: RedirectUri, redirectUri: RedirectUri, timestamp: LocalDateTime)                       extends ApplicationCommand
  case class ChangeResponsibleIndividualToSelf(instigator: UserId, timestamp: LocalDateTime, name: String, email: LaxEmailAddress)                        extends ApplicationCommand
  case class ChangeResponsibleIndividualToOther(code: String, timestamp: LocalDateTime)                                                                   extends ApplicationCommand
  case class DeclineApplicationApprovalRequest(gatekeeperUser: String, reasons: String, timestamp: LocalDateTime)                                         extends GatekeeperApplicationCommand
  case class DeclineResponsibleIndividual(code: String, timestamp: LocalDateTime)                                                                         extends ApplicationCommand
  case class DeclineResponsibleIndividualDidNotVerify(code: String, timestamp: LocalDateTime)                                                             extends ApplicationCommand
  case class DeleteApplicationByCollaborator(instigator: UserId, reasons: String, timestamp: LocalDateTime)                                               extends ApplicationCommand

  case class DeleteApplicationByGatekeeper(gatekeeperUser: String, requestedByEmailAddress: LaxEmailAddress, reasons: String, timestamp: LocalDateTime)
      extends GatekeeperApplicationCommand

  case class DeleteRedirectUri(actor: Actor, redirectUriToDelete: RedirectUri, timestamp: LocalDateTime)                                                extends ApplicationCommand
  case class DeleteProductionCredentialsApplication(jobId: String, reasons: String, timestamp: LocalDateTime)                                           extends ApplicationCommand
  case class DeleteUnusedApplication(jobId: String, authorisationKey: String, reasons: String, timestamp: LocalDateTime)                                extends ApplicationCommand
  case class RemoveClientSecret(actor: Actors.AppCollaborator, clientSecretId: ClientSecret.Id, timestamp: LocalDateTime)                               extends ApplicationCommand
  case class RemoveCollaborator(actor: Actor, collaborator: Collaborator, timestamp: LocalDateTime)                                                     extends ApplicationCommand
  case class SubscribeToApi(actor: Actor, apiIdentifier: ApiIdentifier, timestamp: LocalDateTime)                                                       extends ApplicationCommand
  case class UnsubscribeFromApi(actor: Actor, apiIdentifier: ApiIdentifier, timestamp: LocalDateTime)                                                   extends ApplicationCommand
  case class UpdateRedirectUris(actor: Actor, oldRedirectUris: List[String], newRedirectUris: List[String], timestamp: LocalDateTime)                   extends ApplicationCommand
  case class VerifyResponsibleIndividual(instigator: UserId, timestamp: LocalDateTime, requesterName: String, riName: String, riEmail: LaxEmailAddress) extends ApplicationCommand
}

object ApplicationCommand {
  import ApplicationCommands._
  import uk.gov.hmrc.apiplatform.modules.common.domain.services.LocalDateTimeFormatter._

  implicit private val addCollaboratorFormatter                          = Json.format[AddCollaborator]
  implicit private val removeCollaboratorFormatter                       = Json.format[RemoveCollaborator]
  implicit private val addClientSecretFormatter                          = Json.format[AddClientSecret]
  implicit private val removeClientSecretFormatter                       = Json.format[RemoveClientSecret]
  implicit private val addRedirectUriFormatter                           = Json.format[AddRedirectUri]
  implicit private val changeCollaboratorFormatter                       = Json.format[ChangeRedirectUri]
  implicit private val deleteRedirectUriFormatter                        = Json.format[DeleteRedirectUri]
  implicit private val changeProductionApplicationNameFormatter          = Json.format[ChangeProductionApplicationName]
  implicit private val changePrivacyPolicyLocationFormatter              = Json.format[ChangeProductionApplicationPrivacyPolicyLocation]
  implicit private val changeTermsAndConditionsLocationFormatter         = Json.format[ChangeProductionApplicationTermsAndConditionsLocation]
  implicit private val changeResponsibleIndividualToSelfFormatter        = Json.format[ChangeResponsibleIndividualToSelf]
  implicit private val changeResponsibleIndividualToOtherFormatter       = Json.format[ChangeResponsibleIndividualToOther]
  implicit private val verifyResponsibleIndividualFormatter              = Json.format[VerifyResponsibleIndividual]
  implicit private val declineApplicationApprovalRequestFormatter        = Json.format[DeclineApplicationApprovalRequest]
  implicit private val declineResponsibleIndividualFormatter             = Json.format[DeclineResponsibleIndividual]
  implicit private val declineResponsibleIndividualDidNotVerifyFormatter = Json.format[DeclineResponsibleIndividualDidNotVerify]
  implicit private val deleteApplicationByCollaboratorFormatter          = Json.format[DeleteApplicationByCollaborator]
  implicit private val deleteApplicationByGatekeeperFormatter            = Json.format[DeleteApplicationByGatekeeper]
  implicit private val deleteUnusedApplicationFormatter                  = Json.format[DeleteUnusedApplication]
  implicit private val deleteProductionCredentialsApplicationFormatter   = Json.format[DeleteProductionCredentialsApplication]
  implicit private val subscribeToApiFormatter                           = Json.format[SubscribeToApi]
  implicit private val unsubscribeFromApiFormatter                       = Json.format[UnsubscribeFromApi]
  implicit private val UpdateRedirectUrisFormatter                       = Json.format[UpdateRedirectUris]

  implicit val formatter = Union.from[ApplicationCommand]("updateType")
    .and[AddCollaborator]("addCollaborator")
    .and[RemoveCollaborator]("removeCollaborator")
    .and[AddClientSecret]("addClientSecret")
    .and[RemoveClientSecret]("removeClientSecret")
    .and[AddRedirectUri]("addRedirectUri")
    .and[ChangeRedirectUri]("changeRedirectUri")
    .and[DeleteRedirectUri]("deleteRedirectUri")
    .and[ChangeProductionApplicationName]("changeProductionApplicationName")
    .and[ChangeProductionApplicationPrivacyPolicyLocation]("changeProductionApplicationPrivacyPolicyLocation")
    .and[ChangeProductionApplicationTermsAndConditionsLocation]("changeProductionApplicationTermsAndConditionsLocation")
    .and[ChangeResponsibleIndividualToSelf]("changeResponsibleIndividualToSelf")
    .and[ChangeResponsibleIndividualToOther]("changeResponsibleIndividualToOther")
    .and[DeclineApplicationApprovalRequest]("declineApplicationApprovalRequest")
    .and[DeclineResponsibleIndividual]("declineResponsibleIndividual")
    .and[DeclineResponsibleIndividualDidNotVerify]("declineResponsibleIndividualDidNotVerify")
    .and[DeleteApplicationByCollaborator]("deleteApplicationByCollaborator")
    .and[DeleteApplicationByGatekeeper]("deleteApplicationByGatekeeper")
    .and[DeleteUnusedApplication]("deleteUnusedApplication")
    .and[DeleteProductionCredentialsApplication]("deleteProductionCredentialsApplication")
    .and[SubscribeToApi]("subscribeToApi")
    .and[UnsubscribeFromApi]("unsubscribeFromApi")
    .and[UpdateRedirectUris]("updateRedirectUris")
    .and[VerifyResponsibleIndividual]("verifyResponsibleIndividual")
    .format
}

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
