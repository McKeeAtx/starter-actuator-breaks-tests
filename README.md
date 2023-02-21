# starter-actuator-breaks-tests

Setup: A bare bones Spring Boot application that uses the following starters:

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web:3.0.2'
    implementation 'org.springframework.boot:spring-boot-starter-security:3.0.2'
    implementation 'org.springframework.boot:spring-boot-starter-actuator:3.0.2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test:3.0.2'
}
```

Application class:
```
@EnableMethodSecurity
@SpringBootApplication
public class Application {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```


I have two tests that perform `GET` against an unknown endpoint:

```
    @Test
    void testGetWithValidHeader() throws Exception {
        var validHeader = "Bearer ABCD";
        mockMvc
                .perform(get("/unknown-endpoint").header("Authorization", validHeader))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWithInvalidHeader() throws Exception {
        var invalidHeader = "Bearer \t";

        mockMvc
                .perform(get("/unknown-endpoint").header("Authorization", invalidHeader))
                .andExpect(status().is4xxClientError());
    }
```

The first test passes, the 2nd test fails because the service returns an empty response with status code 200.

The 2nd test passes if I remove `spring-boot-starter-actuator`:

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web:3.0.2'
    implementation 'org.springframework.boot:spring-boot-starter-security:3.0.2'
   // implementation 'org.springframework.boot:spring-boot-starter-actuator:3.0.2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test:3.0.2'
}

```
