package aop.advanced.trace.hellotrace;

import aop.advanced.trace.TraceId;
import aop.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j  //로깅 추상 레이어
@Component // 기본적으로 싱글톤으로 구현한다.
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";


    public TraceStatus begin(String message){
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}]{}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    };
    public void end(TraceStatus status){
        complete(status, null);
    };
    public void exception(TraceStatus status, Exception e){
        complete(status, e);
    }
    // 예외가 터졌을때 내용 출력

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}]{}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX,
                            traceId.getLevel()),
                    status.getTraceId(),
                    resultTimeMs);
        }else  {
            log.info("[{}]{}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX,
                            traceId.getLevel()),
                    status.getTraceId(),
                    resultTimeMs,
                    e.toString());
        }
    }


    private static String addSpace(String prefix, int level) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1 ) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }
}
