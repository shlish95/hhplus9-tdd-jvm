package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointFacadeService {

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = userPointService.charge(id, amount);
        pointHistoryService.record(id, userPoint.point(), TransactionType.CHARGE);
        return userPoint;
    }

    public UserPoint use(long id, long amount) {
        UserPoint userPoint = userPointService.use(id, amount);
        pointHistoryService.record(id, userPoint.point(), TransactionType.USE);
        return userPoint;
    }
}
