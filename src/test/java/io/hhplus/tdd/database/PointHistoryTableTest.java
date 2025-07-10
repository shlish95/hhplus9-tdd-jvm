package io.hhplus.tdd.database;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTableTest {

    private PointHistoryTable pointHistoryTable;

    @BeforeEach
    void setUp() {
        pointHistoryTable = new PointHistoryTable();
    }

    @DisplayName("insert 테스트")
    @Test
    void insertTest() {
        //given
        long userId = 1L;
        long amount = 100L;
        TransactionType type = TransactionType.USE;
        long updateMillis = 400L;

        //when
        PointHistory insert = pointHistoryTable.insert(userId, amount, type, updateMillis);

        //then
        assertThat(insert.userId()).isEqualTo(userId);
        assertThat(insert.amount()).isEqualTo(amount);
        assertThat(insert.type()).isEqualTo(type);
        assertThat(insert.updateMillis()).isEqualTo(updateMillis);
    }

    @DisplayName("userId로 히스토리 조회")
    @Test
    void selectAllByUserIdTest() {
        //given
        long userIdA = 2L;
        long userIdB = 3L;

        pointHistoryTable.insert(userIdA, 100L, TransactionType.CHARGE, 100L);
        pointHistoryTable.insert(userIdB, 200L, TransactionType.CHARGE, 200L);
        pointHistoryTable.insert(userIdA, 300L, TransactionType.CHARGE, 300L);
        pointHistoryTable.insert(userIdB, 400L, TransactionType.CHARGE, 400L);
        pointHistoryTable.insert(userIdA, 50L, TransactionType.USE, 500L);

        //when
        List<PointHistory> pointHistoriesA = pointHistoryTable.selectAllByUserId(userIdA);
        List<PointHistory> pointHistoriesB = pointHistoryTable.selectAllByUserId(userIdB);

        //then
        assertThat(pointHistoriesA.size()).isEqualTo(3);
        assertThat(pointHistoriesB.size()).isEqualTo(2);
        assertThat(pointHistoriesA.stream().allMatch(pointHistory -> pointHistory.userId() == userIdA)).isTrue();
    }

}