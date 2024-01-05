package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.user.UserLoginRequestDto;
import application.dto.user.UserLoginResponseDto;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    protected static MockMvc mockMvc;
    private static final String PATH_USERS = "classpath:database/users/";
    private static final String PATH_SHOPPING_CARTS = "classpath:database/shopping_carts/";
    private static final String CLARK_EMAIL = "clark@example.com";
    private static final String CLARK_PASSWORD = "clark123";
    private static final String CLARK_FIRST_NAME = "clark";
    private static final String CLARK_LAST_NAME = "johnson";
    private static final String CLARK_SHIPPING_ADDRESS = "Shevchenko 1";
    private static final Long CLARK_ID = 3L;
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD = "12345678";
    private static final String BASE_URL = "/api/auth";
    private static final String REGISTRATION = "/registration";
    private static final String LOGIN = "/login";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Sql(value = PATH_SHOPPING_CARTS + "remove_clark_shoppingCart_from_shopping_carts_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Verify register() method with correct userRequest
            """)
    void register_ValidUserRequest_ReturnUserDto() throws Exception {
        // given
        UserRegistrationRequestDto clarkRequestDto
                = new UserRegistrationRequestDto()
                .setEmail(CLARK_EMAIL)
                .setPassword(CLARK_PASSWORD)
                .setFirstName(CLARK_FIRST_NAME)
                .setLastName(CLARK_LAST_NAME)
                .setRepeatPassword(CLARK_PASSWORD)
                .setShippingAddress(CLARK_SHIPPING_ADDRESS);
        UserResponseDto clarkResponseDto = new UserResponseDto()
                .setEmail(clarkRequestDto.getEmail())
                .setFirstName(clarkRequestDto.getFirstName())
                .setLastName(clarkRequestDto.getLastName())
                .setShippingAddress(clarkRequestDto.getShippingAddress())
                .setId(CLARK_ID);
        String jsonRequest = objectMapper.writeValueAsString(clarkRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + REGISTRATION)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        UserResponseDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), UserResponseDto.class);
        System.out.println(actual);
        assertNotNull(actual.getId());
        assertEquals(clarkResponseDto, actual);
    }

    @Test
    @DisplayName("""
            Verify login() method with correct userRequest
            """)
    void login_ValidUserRequest_ReturnToken() throws Exception {
        // given
        UserLoginRequestDto aliceLoginRequestDto
                = new UserLoginRequestDto(ALICE_EMAIL, ALICE_PASSWORD);
        String jsonRequest = objectMapper.writeValueAsString(aliceLoginRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + LOGIN)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        UserLoginResponseDto aliceLoginResponseDto =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        UserLoginResponseDto.class);
        assertNotNull(aliceLoginResponseDto);
    }
}
