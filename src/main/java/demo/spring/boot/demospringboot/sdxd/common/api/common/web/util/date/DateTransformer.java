package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.date;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTransformer {

    public static Date day(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
        ZonedDateTime zonedDateTimeStart = zonedDateTime.toLocalDate().atStartOfDay(zoneId);
        return Date.from(zonedDateTimeStart.toInstant());
    }

    public static Date today() {
        return day(new Date());
    }
}
