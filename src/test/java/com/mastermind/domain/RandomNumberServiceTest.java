package com.mastermind.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class RandomNumberServiceTest {

    @Test
    public void shouldGenerateSecretCode() {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), eq(String.class))).thenReturn("0\n1\n2\n3\n");

        RandomNumberService service = new RandomNumberService(mockRestTemplate);
        String code = service.generateSecretCode();
        assertEquals("0123", code);

        verify(mockRestTemplate).getForObject("https://www.random.org/integers/?num=4&min=0&max=7&col=1&base=10&format=plain&rnd=new", String.class);
    }
}
