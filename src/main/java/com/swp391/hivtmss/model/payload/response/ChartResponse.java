package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartResponse {

    @JsonProperty("week")
    private int week;

    @JsonProperty("weekly_dashboard")
    private List<DashboardResponse> weeklyDashboard;

}
