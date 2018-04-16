package xyz.sanshan.auth.security.server.mapper;


import tk.mybatis.mapper.common.Mapper;
import xyz.sanshan.auth.security.server.entity.Client;
import java.util.List;

public interface ClientMapper extends Mapper<Client> {

    List<String> selectAllowedClient(String serviceId);

    List<Client> selectAuthorityServiceInfo(int clientId);

}
