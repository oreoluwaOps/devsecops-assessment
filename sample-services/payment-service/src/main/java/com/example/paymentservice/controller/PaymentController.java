package com.example.paymentservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

/**
 * REST controller for payment operations.  This controller makes HTTP calls
 * to the account service to retrieve balances and issue credits.
 */
@RestController
public class PaymentController {

    @Value("${api.key}")
    private String apiKey;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * POST /pay
     * Processes a payment against an account.  Calls the account service to
     * fetch the balance and, if sufficient, debits the account.
     * Returns an error if the balance is insufficient or
     * if the account service call fails.
     */
    @PostMapping("/pay")
    public ResponseEntity<Map<String, Object>> pay(@RequestBody Map<String, Object> payload) {
        String accountId = String.valueOf(payload.get("accountId"));
        double amount = Double.parseDouble(String.valueOf(payload.get("amount")));
        try {
            // Fetch current balance
            ResponseEntity<Map> balanceRes = restTemplate.getForEntity(
                    accountServiceUrl + "/balance?accountId=" + accountId, Map.class);
            if (!balanceRes.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Failed to fetch balance");
                return ResponseEntity.badRequest().body(err);
            }
            Map<String, Object> balanceBody = balanceRes.getBody();
            double balance = Double.parseDouble(String.valueOf(balanceBody.get("balance")));
            if (balance < amount) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Insufficient funds");
                return ResponseEntity.badRequest().body(err);
            }

            Map<String, Object> creditPayload = new HashMap<>();
            creditPayload.put("accountId", accountId);
            creditPayload.put("amount", -amount);
            restTemplate.postForEntity(accountServiceUrl + "/credit", creditPayload, Void.class);
            Map<String, Object> ok = new HashMap<>();
            ok.put("accountId", accountId);
            ok.put("amount", amount);
            ok.put("status", "Payment processed");
            return ResponseEntity.ok(ok);
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Payment error");
            return ResponseEntity.status(500).body(err);
        }
    }
}