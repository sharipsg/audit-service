package com.example.auditservice.controller;

import com.example.audit.dto.AuditHistoryResponse;
import com.example.audit.service.AuditQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditQueryService auditQueryService;

    @GetMapping("/entity/{entity}/{id}")
    public AuditHistoryResponse getHistory(@PathVariable String entity, @PathVariable String id) {
        return auditQueryService.getHistoryByEntity(entity, id);
    }

    @GetMapping("/request/{requestId}")
    public AuditHistoryResponse getByRequest(@PathVariable String requestId) {
        return auditQueryService.getHistoryByRequestId(requestId);
    }
}

