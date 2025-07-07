package io.hhplus.tdd.point;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    @DisplayName("empty 메서드 테스트")
    @Test
    void emptyTest() {
        //given
        long userId = 1L;

        //when
        UserPoint userA = UserPoint.empty(userId);

        //then
        assertThat(userA.id()).isEqualTo(userId);
        assertThat(userA.point()).isEqualTo(0);
    }

}