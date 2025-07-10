package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PointIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserPointTable userPointTable;

    @Autowired
    PointHistoryTable pointHistoryTable;


    @DisplayName("포인트 조회")
    @Test
    void getPointIntegrationTest() throws Exception {
        //given
        long userId = 1L;
        long amount = 500L;
        userPointTable.insertOrUpdate(userId, amount);

        //when
        //then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(amount));

    }

    @DisplayName("포인트 충전")
    @Test
    void chargeIntegrationTest() throws Exception {
        //given
        long userId = 2L;
        long chargeAmount = 1000L;

        //when
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(chargeAmount));

        //then
        assertThat(userPointTable.selectById(userId).point()).isEqualTo(chargeAmount);
        assertThat(pointHistoryTable.selectAllByUserId(userId)).hasSize(1);
    }

    @DisplayName("포인트 사용")
    @Test
    void useIntegrationTest() throws Exception {
        //given
        long userId = 3L;
        long ownPointId = 1000L;
        long usePointId = 200L;
        userPointTable.insertOrUpdate(userId, ownPointId);

        //when
        mockMvc.perform(patch("/point/{id}/use", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(usePointId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(ownPointId - usePointId));

        //then
        assertThat(userPointTable.selectById(userId).point()).isEqualTo(ownPointId - usePointId);
        assertThat(pointHistoryTable.selectAllByUserId(userId)).hasSize(1);
    }

    @DisplayName("히스토리 조회 ")
    @Test
    void getHistoryIntegrationTest() throws Exception {
        //given
        long userId = 4L;
        pointHistoryTable.insert(userId, 300L, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryTable.insert(userId, 100L, TransactionType.USE, System.currentTimeMillis());

        //when
        //then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].amount").value(300L))
                .andExpect(jsonPath("$[1].type").value("USE"));
    }
}
