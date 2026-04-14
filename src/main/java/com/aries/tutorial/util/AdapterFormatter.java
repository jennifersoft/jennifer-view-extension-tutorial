package com.aries.tutorial.util;

import com.aries.extension.data.EventData;
import com.aries.extension.data.SystemEventData;
import com.aries.extension.data.TransactionData;
import com.aries.extension.data.UserData;

public final class AdapterFormatter {

    private AdapterFormatter() {
    }

    public static String formatEvent(int idx, EventData data) {
        StringBuilder sb = new StringBuilder(192);
        sb.append("▸ EVT  #").append(idx).append("  ");
        sb.append('⟨').append(data.eventLevel).append("⟩  ");
        sb.append("domain#").append(data.domainId).append('·').append(data.instanceName);
        sb.append("  │  biz=").append(data.businessName);
        sb.append("  │  tx=").append(data.txid);
        sb.append("  │  svc=").append(data.serviceName);
        sb.append("  │  ").append(data.errorType);
        return sb.toString();
    }

    public static String formatTransaction(int idx, TransactionData data) {
        StringBuilder sb = new StringBuilder(192);
        sb.append("◆ TXN  #").append(idx).append("  ");
        sb.append("domain#").append(data.domainId).append('·').append(data.instanceName);
        sb.append("  │  tx=").append(data.txid);
        sb.append("  │  app=").append(data.applicationName);
        sb.append("  │  ").append(data.responseTime).append("ms");
        sb.append("  │  err=").append(data.errorType == null ? "-" : data.errorType);
        return sb.toString();
    }

    public static String formatSystemEvent(int idx, SystemEventData data) {
        StringBuilder sb = new StringBuilder(160);
        sb.append("■ SYS  #").append(idx).append("  ");
        sb.append("domain#").append(data.domainId);
        sb.append("  │  subject=").append(data.subject);
        sb.append("  │  ds=").append(data.dataServer);
        sb.append("  │  message=").append(data.message);
        return sb.toString();
    }

    /**
     * 로그인 시도 1건을 한 줄 로그로 만든다.
     *
     * 교육용 단순 예제 — 운영 환경에서는 아래 항목을 추가로 고려할 것:
     *   - 실패 시 attemptedId 부분 마스킹 (예: "us***1"). brute-force 시 PII 보호.
     *   - email / cellphone 등 추가 PII 필드는 로그에 남기지 말 것.
     *   - 비밀번호는 평문 / 해시 / 길이를 막론하고 어떤 형태로도 출력 금지.
     *   - 성공/실패 표기는 ✓/✗ 글리프 외에 [OK]/[FAIL] 같은 grep 친화 텍스트도 가능.
     */
    public static String formatLogin(String attemptedId, UserData result) {
        if (result != null) {
            return "★ LGN  ✓  " + attemptedId
                    + "  │  group=" + result.groupId
                    + "  │  name=" + result.name;
        }
        return "★ LGN  ✗  " + attemptedId + "  │  <denied>";
    }

    /**
     * SSO 로그인 시도 1건을 한 줄 로그로 만든다.
     *
     * 교육용 단순 예제 — 운영 환경에서는 아래 3-state로 분리해 디버깅 정보를 풍부하게 만들 것:
     *   - 성공            : ✓ + 헤더 ID + UserData 필드
     *   - 헤더 누락       : ✗ + "<no header>"  (ssoHeaderId == null 분기)
     *   - 헤더 있지만 거부 : ✗ + 마스킹 ID + "<denied>"
     *
     * formatLogin과 동일한 PII / 비밀번호 출력 금지 원칙이 적용된다.
     * SSO는 2-arg UserData 생성자를 쓰므로 groupId / name 이 null 일 수 있어 "-" 로 폴백한다.
     */
    public static String formatSsoLogin(String ssoHeaderId, UserData result) {
        if (result != null) {
            String group = result.groupId == null ? "-" : result.groupId;
            String name = result.name == null ? "-" : result.name;
            return "☆ SSO  ✓  " + ssoHeaderId
                    + "  │  group=" + group
                    + "  │  name=" + name;
        }
        String shown = ssoHeaderId == null ? "-" : ssoHeaderId;
        return "☆ SSO  ✗  " + shown + "  │  <denied>";
    }
}
