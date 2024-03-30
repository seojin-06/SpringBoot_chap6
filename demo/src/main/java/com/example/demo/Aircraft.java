package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Data //롬복에서 게터, 세터 , equals(), hashCode(), toString() 메서드를 생성해 데이터 클래스를 만듬
@NoArgsConstructor //롬복에 매개변수가 없는 생성자를 만들도록 지시해 인수가 필요하지 않음
@AllArgsConstructor //롬복에 각 멤버 변수의 매개변수가 있는 생성자를 만들도록 지시, 모든 멤버 변수에 인수 제공
@RedisHash
@JsonIgnoreProperties(ignoreUnknown = true)
//클래스에 상응하는 멤버변수가 없는 경우, 역직렬화 매커니즘이 이를 무시하도록 한다
public class Aircraft {
    @Id //어노테이션이 달린 멤버 변수가 데이터베이스 항목/레코드의 고유 식별자를 가지도록 지정한다
    private Long id;
    private String callsign, squawk, reg, flightno, route, type, category;
    private int altitude, heading, speed;
    @JsonProperty("vert_rate")
    private int vertRate;
    @JsonProperty("selected_altitude")
    private int selectedAltitude;
    private double lat, lon, barometer;
    @JsonProperty("polar_distance")
    private double polarDistance;
    @JsonProperty("polar_bearing")
    private double polarBearing;
    @JsonProperty("is_adsb")
    private boolean isADSB;
    @JsonProperty("is_on_ground")
    private boolean isOnGround;
    @JsonProperty("last_seen_time")
    private Instant lastSeenTime;
    @JsonProperty("pos_update_time")
    private Instant posUpdateTime;
    @JsonProperty("bds40_seen_time")
    private Instant bds40SeenTime;
}
