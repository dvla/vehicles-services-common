package dvla.common.domain.DmsWeb

import org.joda.time.DateTime

final case class DmsWebHeaderDto (conversationId: String,
                                  originDateTime: DateTime,
                                  applicationCode: String,
                                  channelCode: String,
                                  contactId: Long,
                                  eventFlag: Boolean,
                                  serviceTypeCode: String,
                                  languageCode: String,
                                  endUser: Option[DmsWebEndUserDto])
