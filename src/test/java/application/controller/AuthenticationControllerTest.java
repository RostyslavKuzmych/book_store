package application.controller;

import application.dto.user.UserLoginRequestDto;
import application.dto.user.UserLoginResponseDto;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    protected static MockMvc mockMvc;
    private static final String PATH_USERS = "classpath:database/users/";
    private static final String PATH_SHOPPING_CARTS = "classpath:database/shopping_carts/";
    private static final String ID = "id";
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
    @Sql(value = PATH_USERS + "remove_clark_user_from_users_table.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Verify register() method with correct userRequest
            """)
    void register_ValidUserRequest_ReturnUserDto() throws Exception {
        // given
        UserRegistrationRequestDto clarkRequestDto
                = new UserRegistrationRequestDto()
                .setEmail("clark@example.com")
                .setPassword("clark123")
                .setFirstName("clark")
                .setLastName("johnson")
                .setRepeatPassword("clark123")
                .setShippingAddress("Shevchenko 1");
        UserResponseDto clarkResponseDto = new UserResponseDto()
                        .setEmail(clarkRequestDto.getEmail())
                .setFirstName(clarkRequestDto.getFirstName())
                .setLastName(clarkRequestDto.getLastName())
                .setShippingAddress(clarkRequestDto.getShippingAddress());
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
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(clarkResponseDto, actual, ID);
    }

    @Test
    @DisplayName("""
            Verify login() method with correct userRequest
            """)
    void login_ValidUserRequest_ReturnToken() throws Exception {
        // given
        UserLoginRequestDto aliceLoginRequestDto
                = new UserLoginRequestDto("alice@example.com", "12345678");
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
