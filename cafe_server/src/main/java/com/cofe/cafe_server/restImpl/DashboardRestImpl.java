package com.cofe.cafe_server.restImpl;

import com.cofe.cafe_server.rest.DashboardRest;
import com.cofe.cafe_server.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class DashboardRestImpl implements DashboardRest {
    DashboardService dashboardService;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        return dashboardService.getCount();
    }
}
