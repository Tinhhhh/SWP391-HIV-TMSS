package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    @JsonProperty("total_customers")
    private Long totalCustomers;

    @JsonProperty("total_doctors")
    private Long totalDoctors;

    @JsonProperty("total_appointments")
    private Long totalAppointments;

    @JsonProperty("total_blogs")
    private Long totalBlogs;

    @JsonProperty("customer_registration_rate")
    private double customerRegistrationRate;

    @JsonProperty("appointment_rate")
    private double appointmentRate;

    @JsonProperty("active_doctors")
    private Long activeDoctors;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

}
