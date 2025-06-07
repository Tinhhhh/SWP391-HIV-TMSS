package com.swp391.hivtmss.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentTime {
    public final int WORKING_HOURS_START = 8; // 8h sáng
    public final int WORKING_HOURS_END = 17; // 5h chiều
    public final int LUNCH_BREAK_START = 12; // 12h trưa
    public final int LUNCH_BREAK_END = 13; // 1h chiều
    public final int MAX_WORKING_DAYS = 5; // 5 ngày làm việc
    public final int MAX_WORKING_HOURS = 8; // 8h làm việc mỗi ngày
    public final int SLOT_DURATION = 1; // 1h ít nhất mỗi cuộc hẹn
}
