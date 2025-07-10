# 동시성 제어
___
### 동시성 제어란?
- 동시성 제어 (Concurrency Control)는 여러 개의 작업이 동시에 실행될 때 발생할 수 있는 문제를 방지하고, 시스템의 일관성과 안정성을 유지하기 위한 기술
- 여러 프로세스나 스레드가 공유 자원(데이터)에 접근할 때, 충돌이나 예기치 않은 결과를 방지하기 위해 사용
    - 출처 ChatGPT
### 동시성 제어 방법
- synchronized
- ReentrantLock
- ConcurrentHashMap
- Interruptible lock

#### synchronized
- 구현이 간단하고 자동해제 된다는 장점이 있지만 성능 저하와 교착 상태가 발생할 수 있다는 단점이 존재
#### ReentrantLock
- 데드락 방지, 타임아웃 등의 기능이 있지만, 락을 명시적으로 해제해야 하며 코드가 복잡해 질 수 있음
#### ConcurrentHashMap
- 이번 과제에서는 Table 클래스는 변경하지 않아 해당 제외
#### Interruptible lock
- 스레드가 락을 기다리는 동안 중단할 수 있는 기능을 제공
- 해당 과제에서는 락을 중단시키는 기능이 필요 없기 때문에 제외 