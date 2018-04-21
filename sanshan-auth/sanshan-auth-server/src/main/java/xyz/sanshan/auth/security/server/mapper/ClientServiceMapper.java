package xyz.sanshan.auth.security.server.mapper;

import tk.mybatis.mapper.common.Mapper;
import xyz.sanshan.auth.security.server.entity.ClientService;

public interface ClientServiceMapper extends Mapper<ClientService> {
    void deleteByServiceId(int id);
}