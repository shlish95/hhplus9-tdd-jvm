package io.hhplus.tdd.database;

import io.hhplus.tdd.point.UserPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTableTest {

    private UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        userPointTable = new UserPointTable();
    }

    @DisplayName("존재하지 않는 UserPoint의 ID 조회 시 새로운 UserPoint 객체 생성")
    @Test
    void selectByIdTest_empty() {
        //given
        long userId = 1L;

        //when
        UserPoint userPoint = userPointTable.selectById(userId);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
    }

    @DisplayName("insertOrUpdate insert 테스트")
    @Test
    void insertOrUpdateTest_insert() {
        //given
        long userId = 3L;
        long userPointAmount = 100L;

        //when
        UserPoint userPoint = userPointTable.insertOrUpdate(userId, userPointAmount);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(userPointAmount);
    }

    @DisplayName("insertOrUpdate update 테스트")
    @Test
    void insertOrUpdateTest_update() {
        //given
        long userId = 4L;
        long beforUserPointAmount = 100L;
        long afterUserPointAmount = 200L;

        //when
        userPointTable.insertOrUpdate(userId, beforUserPointAmount);
        userPointTable.insertOrUpdate(userId, afterUserPointAmount);
        UserPoint userPoint = userPointTable.selectById(userId);

        //then
        assertThat(userPoint.point()).isNotEqualTo(beforUserPointAmount);
        assertThat(userPoint.point()).isEqualTo(afterUserPointAmount);
    }

    @DisplayName("존재하는 UserPoint의 ID 조회 시 해당 UserPoint가 조회")
    @Test
    void selectByIdTest_notEmpty() {
        //given
        long userPointId = 2L;
        long userPointAmount = 100L;

        //when
        userPointTable.insertOrUpdate(userPointId, userPointAmount);
        UserPoint userPoint = userPointTable.selectById(userPointId);

        //then
        assertThat(userPoint.id()).isEqualTo(userPointId);
        assertThat(userPoint.point()).isEqualTo(userPointAmount);
    }
}