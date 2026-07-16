package com.slotcentral.gameengine.api;

import com.slotcentral.gameengine.paytable.PaytableConfig;
import com.slotcentral.gameengine.paytable.PaytableService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Validated
public class SpinController {
    private final SpinService spinService;
    private final PaytableService paytableService;

    public SpinController(SpinService spinService, PaytableService paytableService) {
        this.spinService = spinService;
        this.paytableService = paytableService;
    }

    @PostMapping("/spin/compute")
    public ResponseEntity<SpinResponse> computeSpin(@Valid @RequestBody SpinRequest request) {
        return ResponseEntity.ok(spinService.computeSpin(request));
    }

    @GetMapping("/paytable")
    public ResponseEntity<PaytableConfig> getPaytable() {
        return ResponseEntity.ok(paytableService.getConfig());
    }
}
