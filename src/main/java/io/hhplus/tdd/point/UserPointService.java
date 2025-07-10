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
        if (pointAmount < 1) {
            throw new IllegalArgumentException("충전 포인트는 1 이상이어야 합니다.");
        }

        long afterAddPoint = userPointTable.selectById(userId).point() + pointAmount;
        return userPointTable.insertOrUpdate(userId, afterAddPoint);
    }

    public UserPoint getPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    public UserPoint use(long userId, long subtractPoint) {
        if (subtractPoint < 1) {
            throw new IllegalArgumentException("사용 포인트는 1 이상이어야 합니다.");
        }

        long currentPoint = userPointTable.selectById(userId).point();
        if (subtractPoint > currentPoint) {
            throw new IllegalStateException("잔액보다 많은 포인트를 사용할 수 없습니다.");
        }

        long afterSubtractPoint = currentPoint - subtractPoint;
        return userPointTable.insertOrUpdate(userId, afterSubtractPoint);
    }
}
