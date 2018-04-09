package xyz.sanshan.gate.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import xyz.sanshan.common.vo.PermissionInfo;
import xyz.sanshan.gate.server.feign.MainPermissionsService;
import xyz.sanshan.gate.server.feign.SearchPermissionsService;

import java.util.*;

/**
 */
@Configuration
@Slf4j
@EnableScheduling
public class AuthPermissionUrlUtil   {
    private Map<String, PermissionInfo> userAllowUrl = new HashMap<>();
    private Set<PermissionInfo> userAntPatternAllowUrl = new HashSet<>();

    private Map<String, PermissionInfo> adminAllowUrl = new HashMap<>();
    private Set<PermissionInfo>  adminAntPatternAllowUrl = new HashSet<>();

    @Autowired
    private MainPermissionsService mainPermissionsService;

    @Autowired
    private SearchPermissionsService searchPermissionsService;

    @Value("${zuul.routes.main.prefix}")
    private String mainPrefix;

    @Value("${zuul.routes.search.prefix}")
    private String searchPrefix;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void refreshPermissionUrl() {
        log.debug("refresh permission url ");
        List<PermissionInfo> mainUserPermissionUrl=   mainPermissionsService.getAllUserPermission().getData();
        List<PermissionInfo> mainAdminPermissionUrl=   mainPermissionsService.getAllAdminPermission().getData();
        List<PermissionInfo> searchAdminPermissionUrl= searchPermissionsService.getAllAdminPermission().getData();

        assembleUserPermissionUrl(mainUserPermissionUrl,mainPrefix);
        assembleAdminPermissionUrl(searchAdminPermissionUrl,searchPrefix);
        assembleAdminPermissionUrl(mainAdminPermissionUrl,mainPrefix);
    }

    private void assembleUserPermissionUrl(List<PermissionInfo> permissionUrls,String prefix){
        assemblePermission(permissionUrls,userAntPatternAllowUrl,prefix,userAllowUrl);
    }

    private void assembleAdminPermissionUrl(List<PermissionInfo> permissionUrls,String prefix) {
        assemblePermission(permissionUrls,adminAntPatternAllowUrl,prefix,adminAllowUrl);
    }

    /**
     *
     * @param permissionUrls
     * @param antPatternAllowUrl
     * @param prefix
     * @param allowUrl
     */
    private void assemblePermission(List<PermissionInfo> permissionUrls,Set<PermissionInfo> antPatternAllowUrl ,String prefix,Map<String, PermissionInfo> allowUrl){
        for (int i = 0; i < permissionUrls.size(); i++) {
            PermissionInfo permissionInfo = permissionUrls.get(i);
            if (antUrlMatch(permissionInfo)) {
                permissionInfo.setUri(prefix+permissionInfo.getUri());
                antPatternAllowUrl.add(permissionInfo);
            }
            allowUrl.put(prefix+permissionInfo.getUri(), permissionInfo);
        }
    }
    /**
     * 是否是Ant类型的URL
     * @param permissionInfo
     * @return
     */
    private boolean antUrlMatch(PermissionInfo permissionInfo){
        String url = permissionInfo.getUri();
        if (url.contains("*")||url.contains("?")){
            return true;
        }else {
            return false;
        }
    }

    public Set<PermissionInfo> getUserAntPatternAllowUrl() {
        return userAntPatternAllowUrl;
    }

    public Set<PermissionInfo> getAdminAntPatternAllowUrl() {
        return adminAntPatternAllowUrl;
    }

    public Map<String, PermissionInfo> getUserAllowUrl() {
        return userAllowUrl;
    }

    public Map<String, PermissionInfo> getAdminAllowUrl() {
        return adminAllowUrl;
    }


}