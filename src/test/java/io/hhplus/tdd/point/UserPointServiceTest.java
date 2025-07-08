package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserPointServiceTest {

    private UserPointService userPointService;

    @BeforeEach
    void setUp() {
        userPointService = new UserPointService(new UserPointTable());
    }

    @DisplayName("userId로 포인트 조회")
    @Test
    void getPointTest() {
        //given
        long userId = 1L;
        long pointAmount = 1000L;
        userPointService.charge(userId, pointAmount);

        //when
        long userPoint = userPointService.getPoint(userId);

        //then
        assertThat(userPoint).isEqualTo(pointAmount);
    }

    @DisplayName("포인트 적립하고 변화한 포인트 조회")
    @Test
    void insertOrUpdatePointTest_add() {
        //given
        long userId = 2L;
        long pointAmount = 1000L;
        long addPoint = 1000L;

        //when
        userPointService.charge(userId, pointAmount);
        userPointService.charge(userId, addPoint);
        long point = userPointService.getPoint(userId);

        //then
        assertThat(point).isEqualTo(pointAmount + addPoint);
    }

    @DisplayName("포인트 사용하고 변화한 포인트 조회")
    @Test
    void insertOrUpdatePointTest_subtract() {
        //given
        long userId = 3L;
        long pointAmount = 5000L;
        long subtractPoint = 1000L;

        userPointService.charge(userId, pointAmount);

        //when
        userPointService.use(userId, subtractPoint);
        long point = userPointService.getPoint(userId);

        //then
        assertThat(point).isEqualTo(pointAmount - subtractPoint);
    }

}