package dvla.common.soap

import javax.xml.ws.BindingProvider

import dvla.common.clientsidesession.TrackingId

object TrackingIdHttpHeader {

  def addTrackingIdHttpHeader(bindingProvider: BindingProvider,
                              headersKey: String,
                              trackingId: TrackingId): Unit = {
    val httpHeaders = new java.util.HashMap[String, java.util.List[String]]()
    httpHeaders.put("tracking_id", java.util.Collections.singletonList(trackingId.value))
    bindingProvider.getRequestContext.put(headersKey, httpHeaders)
  }
}
