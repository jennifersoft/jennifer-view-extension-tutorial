package com.aries.tutorial.adapter;

import com.aries.extension.data.EventData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class EventAdapterTest {

    @InjectMocks
    private EventAdapter eventAdapter;

    @Test
    @DisplayName("이벤트 데이터가 전달되었을 때 정상적으로 처리되어야 함")
    void testOnEvent() {
        // Given: 테스트용 이벤트 데이터 생성
        EventData[] events = new EventData[1];
        events[0] = new EventData(
                (short) 1,             // domainId
                new ArrayList<>(),     // domainGroupHierarchy
                "TestDomain",          // domainName
                "Test Description",    // domainDescription
                System.currentTimeMillis(), // time
                101,                   // instanceId
                "TestInstance",        // instanceName
                "TestInstanceLong",    // instanceNameLong
                null,                  // businessName
                "ERROR",               // errorType
                "ServiceError",        // metricsName
                "FATAL",               // eventLevel
                "Test error message",  // message
                1.0,                   // value
                "SYSTEM",              // otypeName
                "Detailed message",    // detailMessage
                "TestService",         // serviceName
                123456789L,            // relevantTxId
                "Custom data",         // customMessage
                null                   // instanceData
        );

        // When & Then: 예외 없이 실행되는지 확인
        assertDoesNotThrow(() -> {
            eventAdapter.on(events);
        });
    }
}
