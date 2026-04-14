package com.aries.tutorial2.util

import com.aries.extension.data.EventData
import com.aries.extension.data.SystemEventData
import com.aries.extension.data.TransactionData
import com.aries.extension.data.UserData

object AdapterFormatter {

    fun formatEvent(idx: Int, data: EventData): String = buildString(192) {
        append("▸ EVT  #").append(idx).append("  ")
        append('⟨').append(data.eventLevel).append("⟩  ")
        append("domain#").append(data.domainId).append('·').append(data.instanceName)
        append("  │  biz=").append(data.businessName)
        append("  │  tx=").append(data.txid)
        append("  │  svc=").append(data.serviceName)
        append("  │  ").append(data.errorType)
    }

    fun formatTransaction(idx: Int, data: TransactionData): String = buildString(192) {
        append("◆ TXN  #").append(idx).append("  ")
        append("domain#").append(data.domainId).append('·').append(data.instanceName)
        append("  │  tx=").append(data.txid)
        append("  │  app=").append(data.applicationName)
        append("  │  ").append(data.responseTime).append("ms")
        append("  │  err=").append(data.errorType ?: "-")
    }

    fun formatSystemEvent(idx: Int, data: SystemEventData): String = buildString(160) {
        append("■ SYS  #").append(idx).append("  ")
        append("domain#").append(data.domainId)
        append("  │  subject=").append(data.subject)
        append("  │  ds=").append(data.dataServer)
        append("  │  message=").append(data.message)
    }

    /**
     * 로그인 시도 1건을 한 줄 로그로 만든다.
     *
     * 교육용 단순 예제 — 운영 환경에서는 아래 항목을 추가로 고려할 것:
     *   - 실패 시 attemptedId 부분 마스킹 (예: "us***1"). brute-force 시 PII 보호.
     *   - email / cellphone 등 추가 PII 필드는 로그에 남기지 말 것.
     *   - 비밀번호는 평문 / 해시 / 길이를 막론하고 어떤 형태로도 출력 금지.
     *   - 성공/실패 표기는 ✓/✗ 글리프 외에 (OK) / (FAIL) 같은 grep 친화 텍스트도 가능.
     */
    fun formatLogin(attemptedId: String, result: UserData?): String =
        if (result != null) {
            "★ LGN  ✓  $attemptedId  │  group=${result.groupId}  │  name=${result.name}"
        } else {
            "★ LGN  ✗  $attemptedId  │  <denied>"
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
    fun formatSsoLogin(ssoHeaderId: String?, result: UserData?): String =
        if (result != null) {
            "☆ SSO  ✓  $ssoHeaderId  │  group=${result.groupId ?: "-"}  │  name=${result.name ?: "-"}"
        } else {
            "☆ SSO  ✗  ${ssoHeaderId ?: "-"}  │  <denied>"
        }
}
