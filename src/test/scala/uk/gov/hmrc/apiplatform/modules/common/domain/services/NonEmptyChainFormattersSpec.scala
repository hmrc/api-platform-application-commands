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

package uk.gov.hmrc.apiplatform.modules.common.domain.services

import uk.gov.hmrc.apiplatform.utils.HmrcSpec
import play.api.libs.json.Json
import cats.data.NonEmptyChain

class NonEmptyChainFormattersSpec extends HmrcSpec {
  import NonEmptyChainFormatters._

  "NonEmptyChainFormatters" should {
    "read from json" in {
      Json.parse("""[ "a", "b", "c" ]""").as[NonEmptyChain[String]] shouldBe NonEmptyChain.of("a","b","c")
    }
    
    "read as a failure" in {
      Json.parse("""[]""").asOpt[NonEmptyChain[String]] shouldBe None
    }

    "write to json" in {
      Json.toJson(NonEmptyChain.of("a","b","c")).toString shouldBe """["a","b","c"]""" 
    }
  }
}
