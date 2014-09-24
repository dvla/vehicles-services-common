package dvla.common.soap

import javax.xml.ws.BindingProvider

object TrackingIdHttpHeader {

  def addTrackingIdHttpHeader(bindingProvider: BindingProvider,
                              headersKey: String,
                              trackingId: String): Unit = {
    val httpHeaders = new java.util.HashMap[String, java.util.List[String]]()
    httpHeaders.put("tracking_id", java.util.Collections.singletonList(trackingId))
    bindingProvider.getRequestContext.put(headersKey, httpHeaders)
  }
}
