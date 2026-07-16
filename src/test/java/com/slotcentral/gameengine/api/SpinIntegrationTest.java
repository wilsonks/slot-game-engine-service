package com.slotcentral.gameengine.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"gaffing.enabled=true"})
class SpinIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void spinComputeWithOverrides_returnsCorrectSpinId() {
        SpinRequest req = new SpinRequest("test-spin-001", "GAME001", 0, 4, "EGM001",
                new ReelStopOverrides(1, 1, 1), false);
        ResponseEntity<SpinResponse> resp = restTemplate.postForEntity(
                "/api/v1/spin/compute", req, SpinResponse.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getSpinId()).isEqualTo("test-spin-001");
    }

    @Test
    void getPaytable_returnsValidPaytable() {
        ResponseEntity<String> resp = restTemplate.getForEntity("/api/v1/paytable", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("betValues");
    }

    @Test
    void spinWithoutOverrides_returnsOk() {
        SpinRequest req = new SpinRequest("test-spin-002", "GAME001", 2, 4, null, null, true);
        ResponseEntity<SpinResponse> resp = restTemplate.postForEntity(
                "/api/v1/spin/compute", req, SpinResponse.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getBetAmount()).isEqualTo(60);
    }
}
