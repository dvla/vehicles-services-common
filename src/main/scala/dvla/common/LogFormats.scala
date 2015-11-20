package dvla.common

import akka.event.LoggingAdapter
import dvla.common.clientsidesession.TrackingId

object LogFormats {

  final val logSeparator = " - "
  private final val anonymousChar = "*"
  private final val nullString = "null"
  final val optionNone = "none"

  def anonymize(input: AnyRef): String = {
    if (input==null){
      nullString
    } else {
      val stringInput = input.toString
      val startOfNonAnonymizedText =
        if (stringInput.length == 0) 0
        else if (stringInput.length > 8) 4
        else stringInput.length / 2
      anonymousChar * (stringInput.length - startOfNonAnonymizedText) + stringInput.takeRight(startOfNonAnonymizedText)
    }
  }

  def anonymize(input: Option[_]): String = {
    input match {
      case null => nullString
      case Some(i) => anonymize(i.toString)
      case None => nullString
    }
  }

  // When changes these two logMessage methods, be sure to replicate the changes in
  //    vehicles-presentation-common/app/uk/.../common/LogFormats.scala
  // in order to keep a consistent log format
  trait DVLALogger {

    sealed trait LogMessageType
    case object Debug extends LogMessageType
    case object Info extends LogMessageType
    case object Error extends LogMessageType
    case object Warn extends  LogMessageType

    def logMessage(trackingId: TrackingId, 
                   messageType: LogMessageType, 
                   messageText: String, 
                   logData: Option[Seq[String]] = None)(implicit log: LoggingAdapter) =
      messageType match {
        case Debug => log.debug(logMessageFormat(trackingId, messageText, logData))
        case Info => log.info(logMessageFormat(trackingId, messageText, logData))
        case Error => log.error(logMessageFormat(trackingId, messageText, logData))
        case Warn => log.warning(logMessageFormat(trackingId, messageText, logData))
      }

    def logErrorMessage(trackingId: TrackingId,
                        messageText: String,
                        throwable: Throwable)(implicit log: LoggingAdapter) =
        log.error(throwable, logMessageFormat(trackingId, messageText, None))

    private def logMessageFormat(trackingId: TrackingId, messageText: String, logData: Option[Seq[String]]): String =
      s"[TrackingID: ${trackingId.value}]$logSeparator$messageText" +
        logData.map( d => s"$logSeparator$logData" ).getOrElse("")
  }
}
