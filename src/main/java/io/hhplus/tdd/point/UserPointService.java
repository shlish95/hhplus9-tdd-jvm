package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;

    public UserPoint charge(long userId, long pointAmount) {
        long afterAddPoint = userPointTable.selectById(userId).point() + pointAmount;
        return userPointTable.insertOrUpdate(userId, afterAddPoint);
    }

    public UserPoint getPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    public UserPoint use(long userId, long subtractPoint) {
        long afterSubtractPoint = userPointTable.selectById(userId).point() - subtractPoint;
        return userPointTable.insertOrUpdate(userId, afterSubtractPoint);
    }
}
