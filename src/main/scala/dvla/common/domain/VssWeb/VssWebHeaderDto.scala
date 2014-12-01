package dvla.common.domain.VssWeb

import org.joda.time.DateTime

final case class VssWebHeaderDto (transactionId: String,
                                  originDateTime: DateTime,
                                  applicationCode: String,
                                  serviceTypeCode: String,
                                  endUser: VssWebEndUserDto)
