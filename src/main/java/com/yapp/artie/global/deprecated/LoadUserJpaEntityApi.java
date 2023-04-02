package com.yapp.artie.global.deprecated;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;

/**
 * @author le2ksy
 * @deprecated {@code findById} deprecated for package-dependency
 * @apiNote
 * UserJpaEntity를 로드하는 API입니다. 클래스 의존성을 생각해보면,
 * 외부 클라이언트 코드(서비스, 도메인 레이어)에서 Persistence Adapter의 JpaEntity를 의존합니다.
 * 외부 클라이언트 코드에서는 이 API를 이용해서 주로 외래키 제약을 만족시키거나, 자신의 리소스에 접근하고 있는지
 * 확인할 때 사용합니다.
 * <br><br>
 * 따라서, 최종적으로 의존성을 제거하기 위해서는 Category#user가 아닌, Category#ownerId를 가지고 있는 설계가
 * 필요할 것 같다고 생각했습니다. 이에 대해서는 더욱 이야기를 해볼 필요가 있을 것 같습니다.
 */
@Deprecated
public interface LoadUserJpaEntityApi {

  UserJpaEntity findById(Long id);
}
