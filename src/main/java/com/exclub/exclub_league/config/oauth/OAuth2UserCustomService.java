package com.exclub.exclub_league.config.oauth;
import com.exclub.exclub_league.User.entity.Role;
import com.exclub.exclub_league.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;
import com.exclub.exclub_league.User.respository.RoleRepository;
import com.exclub.exclub_league.User.respository.UserRepository;
import com.exclub.exclub_league.User.entity.User;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // RoleRepository 추가

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);

        return user;
    }

    private User saveOrUpdate(OAuth2User oAuth2User) { // 유저가 있으면 업데이트, 없으면 유저 생성
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElseGet(() -> createNewUser(email, name)); // 새로운 사용자 생성

        return userRepository.save(user);
    }

    private User createNewUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setPassword(""); // OAuth2 사용자는 일반적으로 비밀번호가 없음

        // 기본 역할 ROLE_USER를 할당합니다
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found: ROLE_USER"));
        user.setRoles(Set.of(userRole));

        return user;
    }
}