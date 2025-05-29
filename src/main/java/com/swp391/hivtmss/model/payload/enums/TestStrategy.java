package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum TestStrategy {
    STRATEGY_I("Strategy I"),
    STRATEGY_II("Strategy II"),
    STRATEGY_III("Strategy III");

    /*
    Strategy I
    Dùng cho: Mục đích sàng lọc máu, mô, tạng trước khi truyền, hiến.
    Cách làm: Chỉ cần 1 test nhanh hoặc ELISA.
    Kết luận:
        + Âm tính → Kết luận âm tính.
        + Dương tính → Không đủ kết luận nhiễm HIV → loại mẫu (không dùng để truyền), nhưng không báo người hiến là HIV+.

    Strategy II
    Dùng cho: Phát hiện nhiễm HIV ở người ≥18 tháng trong môi trường có tỷ lệ HIV cao (như phòng khám, nhóm nguy cơ).
    Cách làm:
        Test 1: Nếu âm tính → kết luận âm tính.
        Test 1 dương tính → Làm Test 2.
            Nếu Test 2 dương tính → Kết luận dương tính.
            Nếu Test 2 âm tính → không kết luận, yêu cầu làm lại sau.

    Strategy III
    Dùng cho: Chẩn đoán chính xác, đặc biệt trong các tình huống tỷ lệ nhiễm thấp hoặc đối tượng đặc biệt (ví dụ: người hiến máu, khám sức khỏe định kỳ).
    Cách làm:
        Test 1 → Dương tính → Test 2.
        Test 2 dương tính → Test 3.
            Nếu cả 3 test dương tính → Kết luận nhiễm HIV.
            Nếu kết quả không phù hợp (ví dụ: test 2 hoặc 3 âm tính) → không kết luận, theo dõi lại.
     */


    private final String value;

    TestStrategy(String value) {
        this.value = value;
    }

}
