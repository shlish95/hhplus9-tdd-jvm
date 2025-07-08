package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PointHistoryServiceTest {

    private PointHistoryService pointHistoryService;

    @BeforeEach
    public void setUp() {
        pointHistoryService = new PointHistoryService(new PointHistoryTable());
    }

    @DisplayName("히스토리 추가")
    @Test
    void historyRecordTest() {
        //given
        long userId = 1L;
        long pointAmount = 100L;
        TransactionType pointType = TransactionType.CHARGE;

        //when
        PointHistory pointHistory = pointHistoryService.record(userId, pointAmount, pointType);

        //then
        assertThat(pointHistory).isNotNull();
        assertThat(pointHistory.userId()).isEqualTo(userId);
        assertThat(pointHistory.amount()).isEqualTo(pointAmount);
        assertThat(pointHistory.type()).isEqualTo(pointType);
    }

    @DisplayName("생성된 히스토리 userId를 통해 조회")
    @Test
    void Test() {
        //given
        long userId = 2L;

        //when
        pointHistoryService.record(userId, 100L, TransactionType.CHARGE);
        pointHistoryService.record(userId, 50L, TransactionType.USE);

        List<PointHistory> pointHistoryList = pointHistoryService.findByUserId(userId);

        //then
        assertThat(pointHistoryList.size()).isEqualTo(2);
        assertThat(pointHistoryList.get(0).userId()).isEqualTo(userId);
        assertThat(pointHistoryList).allMatch(pointHistory -> pointHistory.userId() == userId);
    }
}
