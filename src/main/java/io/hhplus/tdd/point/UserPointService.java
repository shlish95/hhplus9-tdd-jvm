package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;

    public void charge(long userId, long pointAmount) {
        long afterAddPoint = userPointTable.selectById(userId).point() + pointAmount;
        userPointTable.insertOrUpdate(userId, afterAddPoint);
    }

    public long getPoint(long userId) {
        return userPointTable.selectById(userId).point();
    }

    public void use(long userId, long subtractPoint) {
        long afterSubtractPoint = userPointTable.selectById(userId).point() - subtractPoint;
        userPointTable.insertOrUpdate(userId, afterSubtractPoint);
    }
}
