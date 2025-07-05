package com.swp391.hivtmss.util;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@UtilityClass
public class DateUtil {
    public final String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public final String DATE_FORMAT = "MM/dd/yyyy";

    public static String formatTimestamp(Date date) {
        return formatTimestamp(date, DATE_TIME_FORMAT);
    }

    public static String formatTimestamp(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date convertToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // Chuyển từ ICT -> UTC
    public static Date convertICTtoUTC(Date ictDate) {
        return convertTimeZone(ictDate, "Asia/Ho_Chi_Minh", "UTC");
    }

    // Chuyển từ UTC -> ICT
    public static Date convertUTCtoICT(Date utcDate) {
        return convertTimeZone(utcDate, "UTC", "Asia/Ho_Chi_Minh");
    }

    // Hàm chuyển đổi giữa các múi giờ
    public static Date convertTimeZone(Date date, String fromTimeZone, String toTimeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
        String formattedDate = sdf.format(date);

        try {
            sdf.setTimeZone(TimeZone.getTimeZone(toTimeZone));
            return sdf.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getCurrentTimestamp() {
        return Date.from(ZonedDateTime.now().toInstant());
    }

    public static Date convertToStartOfTheDay(Date startDate) {
        return DateUtil.convertToDate(DateUtil.convertToLocalDateTime(DateUtil.convertUTCtoICT(startDate)).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date convertToEndOfTheDay(Date endDate) {
        return DateUtil.convertToDate(DateUtil.convertToLocalDateTime(DateUtil.convertUTCtoICT(endDate)).withHour(23).withMinute(59).withSecond(59).withNano(99));
    }

    public static Date convertUtcToIctDate(Instant utcInstant) {
        // Chuyển UTC -> ICT (GMT+7)
        ZonedDateTime ictZoned = utcInstant.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        // Từ ICT ZonedDateTime -> Instant -> Date
        return Date.from(ictZoned.toInstant());

    }

    public LocalDateTime getLocalDateTime(LocalDateTime date, int time) {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time, 0);
    }

    public static String getTimeAgo(Date createdDate) {
        Instant now = Instant.now();
        Instant createdInstant = createdDate.toInstant();
        long secondsAgo = now.getEpochSecond() - createdInstant.getEpochSecond();

        if (secondsAgo < 60) {
            return secondsAgo + " giây trước";
        } else if (secondsAgo < 3600) {
            return secondsAgo / 60 + " phút trước";
        } else if (secondsAgo < 86400) {
            return secondsAgo / 3600 + " giờ trước";
        } else if (secondsAgo < 2592000) {
            return secondsAgo / 86400 + " ngày trước";
        } else if (secondsAgo < 31536000) {
            return secondsAgo / 2592000 + " tháng trước";
        } else {
            return secondsAgo / 31536000 + " năm trước";
        }
    }

    public static Date subtractOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1); // Lùi 1 tháng
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // Gán ngày cuối tháng
        return cal.getTime();
    }
}
