package io.hhplus.tdd.point;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PointFacadeServiceTest {

    @Mock
    UserPointService userPointService;

    @Mock
    PointHistoryService pointHistoryService;

    @InjectMocks
    PointFacadeService pointFacadeService;

    @DisplayName("포인트 충전시 포인트 증가 및 히스토리 저장")
    @Test
    void recordHistoryTest_charge() {
        //given
        long userId = 1L;
        long amount = 300L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        given(userPointService.charge(userId, amount))
                .willReturn(userPoint);

        //when
        UserPoint result = pointFacadeService.charge(userId, amount);

        //then
        assertThat(result).isEqualTo(userPoint);
        verify(userPointService).charge(userId, amount);
        verify(pointHistoryService).record(userId, amount, TransactionType.CHARGE);
    }

    @DisplayName("포인트 사용시 포인트 차감 및 히스토리 저장")
    @Test
    void recordHistoryTest_use() {
        //given
        long userId = 2L;
        long amount = 300L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        given(userPointService.use(userId, amount))
                .willReturn(userPoint);

        //when
        UserPoint result = pointFacadeService.use(userId, amount);

        //then
        assertThat(result).isEqualTo(userPoint);
        verify(userPointService).use(userId, amount);
        verify(pointHistoryService).record(userId, amount, TransactionType.USE);
    }
}