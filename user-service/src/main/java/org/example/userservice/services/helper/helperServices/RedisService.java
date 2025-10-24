package org.example.userservice.services.helper.helperServices;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final long OTP_TTL = 180 ;
    private final StringRedisTemplate redisTemplate;

    public void redisSaveForgetPasswordCode(String key , String value ){
        redisTemplate.opsForValue().set(key,value, Duration.ofSeconds(OTP_TTL));
    }

    public String getOtp(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void validateOtp(String username, String otpCode) throws Exception{
        String redisStoredOtp =getOtp(username);
        if(redisStoredOtp == null )
            throw new Exception("Expired OTP");

        if (!redisStoredOtp.equals(otpCode)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        long ttl = getTimeToLive(username);
        if (ttl <= 0) {
            throw new IllegalArgumentException("OTP Expired");
        }
    }

    public long getTimeToLive(String key){
        return redisTemplate.getExpire(key);
    }
}