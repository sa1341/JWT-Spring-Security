
## 1. 사용한 기술 및 툴
- Spring Boot
- Spring Data JPA
- JAVA 8
- MariaDB
- QueryDsl
- mustache (서버 템플릿 엔진)
- Postman(REST API 호출용)


## 2. REST API 기능 목록

- 회원가입 기능 
- 로그인 기능
- 토큰 발급 및 검증 기능
- Admin 페이지에서 회원 통계 기능 조회
- 비디오 업로드 기능 구현
- 비디오 재생 기능 구현


## 3.클래스 다이어그램 및 테이블 구조 

![image](https://user-images.githubusercontent.com/22395934/116813048-cf805f80-ab8c-11eb-9ef9-4669a22a9bf2.png)


## 4. 프로젝트 및 엔티티 구성도

![](https://i.imgur.com/gkCTUtC.png)

## 엔티티 연관 관계

기본적으로 프로젝트에 핵심 인터페이스 역할을 하는 도메인은 회원, 비디오, 리프레시 토큰이라고 생각하였습니다. 

회원은 여러개의 비디오 파일을 업로드 할 수 있어서 DB 스키마 구조를 OneToMany로 구성하였고, 리프레시 토큰같은 경우에는 회원 한명 당 한개의 리프레시 토큰을 가지도록 설정하였습니다. 

### 회원 엔티티

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"email", "name", "phoneNumber"})
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "unsubscribable")
    private boolean unsubscribable = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Video> videos = new ArrayList<>();

    @OneToOne(mappedBy = "member", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @Builder
    public Member(Email email, Password password, Name name, PhoneNumber phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void encodePassword(String encodedPassword) {
        this.password = Password.of(encodedPassword);
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean hasRefreshToken() {
        return this.refreshToken != null ? true: false;
    }

    public void changeUnsubscribableStatus() {
        setUnsubscribable(true);
    }

    private void setUnsubscribable(boolean unsubscribable) {
        this.unsubscribable = unsubscribable;
    }

    public void updateProfile(final Name name) {
        setName(name);
    }

    private void setName(Name name) {
        this.name = name;
    }

    public MemberResponse toResponse() {
        return new MemberResponse(this);
    }
}
```

### 비디오 엔티티

```java
@ToString(of = {"name"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private FilePath filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private Video(Name name, FilePath filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public static Video of(final Name name, final FilePath filePath) {
        return new Video(name, filePath);
    }

    public void addMember(final Member member) {
        if (this.member != null) {
            this.member.getVideos().remove(this);
        }
        this.member = member;
        this.member.getVideos().add(this);
    }
}
```

### Refresh 토큰 엔티티

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private RefreshToken(final String value) {
        this.value = value;
    }

    public static RefreshToken of(final String value) {
        return new RefreshToken(value);
    }

    public void addMember(final Member member) {
        this.member = member;
        member.setRefreshToken(this);
    }

    public void deleteMember(final Member member) {
        this.member = null;
        member.setRefreshToken(null);
    }
}
```

## 4. Spring Security와 JWT를 연계하여 인증 기능 구현

토큰 기반의 인증 방식중 JWT(Json Web Token)을 이용하여 사용자 인증 처리룰 구현하였습니다. JWT에 대한 자세한 내용은 아래 링크에 정리하였습니다.

> [JWT란 무엇인가?](https://github.com/sa1341/TIL/blob/master/Java/Spring/JWT%20%ED%86%A0%ED%81%B0%20%EA%B8%B0%EB%B0%98%20%EC%9D%B8%EC%A6%9D.md)

### JWT 토큰 인증 시나리오

아래는 리소스 접근을 위한 통행증 같은 Access 토큰과 Refresh 토큰의 발급 과정입니다.


1. 클라이언트에서 USER 권한이 필요한 리소스에 대해서 요청을 하는 인증 요청 (로그인 기반)

2. Access 토큰과 Refresh 토큰을 발급하여 클라이언트에 전달

3. 인증 완료 후 해당 리소스에 대해서 권한 체크(USER or Admin)

4. Access 토큰의 유효기간을 체크하여 만료가 되면 Reffresh 토큰을 재발급하는 API 기능을 호출하여 AccessToken과  Refresh 토큰을 세트로 재발급 합니다.


### build.gradle 

```java
plugins {
	id 'org.springframework.boot' version '2.4.5'
	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
	id 'java'
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

group = 'com.genesislab'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	compile 'com.querydsl:querydsl-jpa'
	compile 'org.springframework.boot:spring-boot-starter-mustache'
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.6.2'
	implementation 'org.mariadb.jdbc:mariadb-java-client'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'io.jsonwebtoken:jjwt:0.9.1'
	compileOnly 'org.springframework.boot:spring-boot-starter-validation'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()
}

def querydslDir = "$buildDir/generated/querydsl"

querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}

sourceSets {
	main.java.srcDir querydslDir
}

configurations {
	querydsl.extendsFrom compileClasspath
}

compileQuerydsl {
	options.annotationProcessorPath = configurations.querydsl
}
```

### JWT 필터 

아래 JwtAuthenticationFilter는 실제로  JwtTokenProvider을 사용하여 Access 토큰과 Refresh 토큰을 발급해주는 필터 클래스 입니다.   

Spring Security에서 제공하고 있는 FilterChain 중 로그인 폼 기반의 UserNameAndPasswordFilter 클래스보다 먼저 동작하여 이곳에서 인증이 이루어지고, UserNameAndPasswordToken 객체를 생성하여 다음 필터인 UserNameAndPasswordFilter에게 인가 체크를 하게 됩니다.

```java
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String HEAD_AUTH = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws InvalidException, IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(HEAD_AUTH);

        if (StringUtils.hasText(token) && !jwtTokenProvider.isExpiredToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
```


### JWT 토큰 발급 및 검증 클래스

JwtTokenProvider는 실제로 salt 값을 이용해서 JWT 기반의 토큰을 발급하기도 하고, 리소스들에 대한 접근제어를 위해 Access 토큰과 Refresh 토큰의 검증에 대한 책임을 가지고 있습니다.

#### JwtTokenProvider.java 

```java
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public TokenDto generateJwtToken(final Member member) {
        long now = (new Date()).getTime();
        // AccessToken 발급
        String jwt = createAccessToken(member, now);
        // refreshToken 발급
        String refreshToken = createRefreshToken(member, now);

        String email = member.getEmail().getValue();
        TokenDto tokenDto = TokenDto.of(email, jwt, refreshToken);
        return tokenDto;
    }

    private String createRefreshToken(final Member member, final long now) {
        Date refreshTokenExpiredTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiredTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return refreshToken;
    }

    private String createAccessToken(final Member member, long now) {
        String jwt = Jwts.builder()
                .setHeaderParams(createHeader())
                .setClaims(createClaims(member))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return jwt;
    }

    public String getUserEmail(final String jwt) throws InvalidException {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().get("id", String.class);
        } catch (Exception e) {
            throw new InvalidException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    public Authentication getAuthentication(final String jwt) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isExpiredToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        return headers;
    }

    private Map<String, Object> createClaims(final Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getEmail().getValue());
        claims.put("username", member.getName().getValue());
        return claims;
    }
}
```

### application.yaml

```java
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/genesislab
    username: junyoung
    password: wnsdud@123
  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    open-in-view: false
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        # show_sql: true
        format_sql: true
  servlet:
    multipart:
      max-request-size: 10MB
      max-file-size: 10MB

logging:
  level:
    root: info
    com.genesislab.videoservice: debug
    org.hibernate.SQL: info
    # org.hibernate.type: trace

jwt:
  secret:
    key: secret

file:
  storage:
    path: /Users/limjun-young/workspace/privacy/dev/test/video
```
