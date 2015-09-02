package dvla.common.microservice.helpers

import java.util.TimeZone

import org.joda.time.DateTimeZone

class Timezone {

  private def setDefaultTimeZone() = {
    val localTimeZone = "UTC"
    TimeZone.setDefault(TimeZone.getTimeZone(localTimeZone))
    DateTimeZone.setDefault(DateTimeZone.forID(localTimeZone))
  }

}
