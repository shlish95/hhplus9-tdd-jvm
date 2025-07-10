package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryTable pointHistoryTable;

    public PointHistory record(long userId, long pointAmount, TransactionType pointType) {
        return pointHistoryTable.insert(userId, pointAmount, pointType, System.currentTimeMillis());
    }

    public List<PointHistory> findByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
