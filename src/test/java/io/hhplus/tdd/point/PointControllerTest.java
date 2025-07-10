package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserPointService userPointService;

    @MockBean
    PointHistoryService pointHistoryService;

    @MockBean
    PointFacadeService pointFacadeService;


    @DisplayName("userId를 통해 특정 유저 포인트 조회")
    @Test
    void pointTest() throws Exception {
        //given
        long userId = 1L;

        //when
        given(userPointService.getPoint(userId))
                        .willReturn(new UserPoint(userId, 100L, 12346578L));

        //then
        mockMvc.perform(get("/point/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.point").value(100L));

    }

    @DisplayName("유저 포인트 히스토리 조회")
    @Test
    void historyTest() throws Exception {
        //given
        long userId = 2L;
        long chargePoint = 200L;
        long usePoint = 100L;
        PointHistory pointHistory1 = new PointHistory(1L, userId, chargePoint, TransactionType.CHARGE, 12341234L);
        PointHistory pointHistory2 = new PointHistory(2L, userId, usePoint, TransactionType.USE, 12341299L);
        List<PointHistory> pointHistories = List.of(pointHistory1, pointHistory2);

        //when
        given(pointHistoryService.findByUserId(userId))
                .willReturn(pointHistories);

        //then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].amount").value(chargePoint))
                .andExpect(jsonPath("$[1].type").value("USE"));
    }

    @DisplayName("포인트 충전")
    @Test
    void chargeTest() throws Exception {
        //given
        long ownPoint = 100L;
        long chargePoint = 1000L;

        //when
        given(pointFacadeService.charge(3L, chargePoint))
                .willReturn(new UserPoint(3L, ownPoint + chargePoint, 12341234L));

        //then
        mockMvc.perform(patch("/point/3/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(ownPoint + chargePoint));
    }

    @DisplayName("포인트 사용")
    @Test
    void useTest() throws Exception {
        //given
        long ownPoint = 500L;
        long usePoint = 100L;

        //when
        given(pointFacadeService.use(4L, usePoint))
                .willReturn(new UserPoint(4L, ownPoint - usePoint, 12341234L));

        //then
        mockMvc.perform(patch("/point/4/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(ownPoint - usePoint));

    }
}