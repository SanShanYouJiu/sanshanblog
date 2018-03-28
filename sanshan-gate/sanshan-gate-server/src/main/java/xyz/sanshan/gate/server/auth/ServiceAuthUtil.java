package xyz.sanshan.gate.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.gate.server.feign.MainPermissionsService;
import xyz.sanshan.gate.server.feign.SearchPermissionsService;

import java.util.LinkedList;
import java.util.List;

/**
 */
@Configuration
@Slf4j
@EnableScheduling
public class ServiceAuthUtil  {
    private List<PermissionInfo> userAllowUrl = new LinkedList<>();
    private List<PermissionInfo> adminAllowUrl = new LinkedList<>();

    @Autowired
    private MainPermissionsService mainPermissionsService;

    @Autowired
    private SearchPermissionsService searchPermissionsService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void refreshAllowedClient() {
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshPermissionUrl() {
        log.debug("refresh permission url ");
        userAllowUrl.addAll(mainPermissionsService.getAllUserPermission().getData());
        adminAllowUrl.addAll(searchPermissionsService.getAllAdminPermission().getData());
    }

    public List<PermissionInfo> getUserAllowUrl() {
        return userAllowUrl;
    }

    public List<PermissionInfo> getAdminAllowUrl() {
        return adminAllowUrl;
    }
}