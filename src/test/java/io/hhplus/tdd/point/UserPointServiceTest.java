package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
        UserPoint userPoint = userPointService.getPoint(userId);

        //then
        assertThat(userPoint.point()).isEqualTo(pointAmount);
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
        UserPoint userPoint = userPointService.getPoint(userId);

        //then
        assertThat(userPoint.point()).isEqualTo(pointAmount + addPoint);
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
        UserPoint userPoint = userPointService.getPoint(userId);

        //then
        assertThat(userPoint.point()).isEqualTo(pointAmount - subtractPoint);
    }

    @Nested
    class 예외처리 {
        @DisplayName("충전하려는 포인트가 1보다 작으면 예외 발생")
        @Test
        void chargePointUnderOne_shouldFail() {
            //given
            long userId = 4L;
            long invalidAmount = 0L;

            //when
            //then
            assertThatThrownBy(() -> userPointService.charge(userId, invalidAmount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("충전 포인트는 1 이상이어야 합니다.");
        }

        @DisplayName("사용하려는 포인트가 1보다 작으면 예외 발생")
        @Test
        void usePointUnderOne_shouldFail() {
            //given
            long userId = 5L;
            long invalidAmount = 0L;

            //when
            //then
            assertThatThrownBy(() -> userPointService.use(userId, invalidAmount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("사용 포인트는 1 이상이어야 합니다.");
        }

        @DisplayName("사용하려는 포인트가 잔여 포인트 보다 크면 예외 발생")
        @Test
        void usePointOverBalance_shouldFail() {
            //given
            long userId = 6L;
            long ownPointAmount = 1000L;
            long usePointAmount = 1500L;

            //when
            userPointService.charge(userId, ownPointAmount);

            //then
            assertThatThrownBy(() -> userPointService.use(userId, usePointAmount))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("잔액보다 많은 포인트를 사용할 수 없습니다.");

        }
    }

}