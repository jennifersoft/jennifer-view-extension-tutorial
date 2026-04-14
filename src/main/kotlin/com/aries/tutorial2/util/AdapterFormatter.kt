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
     * 로그인 시도 1건을 한 줄로 표현한다.
     *
     * @param attemptedId 사용자가 입력한 로그인 ID (실패 시에도 받지만, 비밀번호는 절대 받지 않는다)
     * @param result      인증 성공 시 채워진 UserData, 실패 시 null
     * @return "★ LGN  ..." 로 시작하는 한 줄 로그
     *
     * TODO(사용자 작성): 아래 보안 의사결정을 반영해 본문을 채워주세요.
     *   1) 성공/실패 표시: ✓ / ✗ 중 어떤 글리프 또는 [OK]/[FAIL] 같은 텍스트?
     *   2) 실패 시 attemptedId를 그대로 노출할지, 부분 마스킹("us***1") 할지?
     *      — 그대로 두면 무차별 대입 공격 시도 ID가 로그에 남아 분석에 유용하지만, GDPR/PII 관점에선 마스킹이 안전.
     *   3) 성공 시 어떤 UserData 필드를 추가로 보여줄지? (groupId, name 정도만? email은 PII라 제외 권장)
     *   4) 비밀번호 또는 그 길이/해시 같은 부수 정보는 *절대* 출력하지 말 것.
     */
    fun formatLogin(attemptedId: String, result: UserData?): String {
        // TODO: 사용자 구현
        return "★ LGN  <not implemented>"
    }

    /**
     * SSO 로그인 시도 1건을 한 줄로 표현한다.
     *
     * @param ssoHeaderId SSO 헤더(SSO_ID)에서 읽은 사용자 식별자, 헤더가 없으면 null
     * @param result      인증 성공 시 채워진 UserData, 실패(헤더 누락 등) 시 null
     * @return "☆ SSO  ..." 로 시작하는 한 줄 로그
     *
     * TODO(사용자 작성): formatLogin과 동일한 결정 + 추가 결정.
     *   - SSO는 헤더 기반이라 "헤더 누락"과 "헤더는 있지만 인증 거부"를 구분해 표시할지?
     *     예: ssoHeaderId == null 이면 "<no header>", 그 외 실패면 "<denied>"
     */
    fun formatSsoLogin(ssoHeaderId: String?, result: UserData?): String {
        // TODO: 사용자 구현
        return "☆ SSO  <not implemented>"
    }
}
