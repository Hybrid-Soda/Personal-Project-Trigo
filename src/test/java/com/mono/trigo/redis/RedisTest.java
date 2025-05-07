package com.mono.trigo.redis;

import com.mono.trigo.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOps;

    @BeforeEach
    public void setUp() {
        valueOps = redisTemplate.opsForValue();
    }

    @Test
    void testSaveAndRetrieveJsonObject() {
        // 객체 생성
        User user = User.builder().id(1L).username("user123").password("qwer123").role("member").build();

        // 객체 저장
        String key = "user::1";
        valueOps.set(key, user, 15, TimeUnit.SECONDS);

        // 객체 조회
        User fetchedUser = (User) valueOps.get(key);

        // 검증
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isEqualTo(user.getId());
        assertThat(fetchedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(fetchedUser.getPassword()).isEqualTo(user.getPassword());
    }
}
