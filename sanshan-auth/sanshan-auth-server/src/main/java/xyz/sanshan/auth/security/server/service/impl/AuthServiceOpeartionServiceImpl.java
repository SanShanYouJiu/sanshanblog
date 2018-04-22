package xyz.sanshan.auth.security.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.sanshan.auth.security.server.entity.Client;
import xyz.sanshan.auth.security.server.entity.ClientService;
import xyz.sanshan.auth.security.server.mapper.ClientMapper;
import xyz.sanshan.auth.security.server.mapper.ClientServiceMapper;
import xyz.sanshan.auth.security.server.service.AuthServiceOperationService;

import java.util.List;

@Service
public class AuthServiceOpeartionServiceImpl implements AuthServiceOperationService {
    @Autowired
    private ClientServiceMapper clientServiceMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void modifyClientServices(int id, List<String> clients) {
        clientServiceMapper.deleteByServiceId(id);
        for (int i = 0; i < clients.size(); i++) {
            String client = clients.get(i);
            ClientService clientService = new ClientService();
            clientService.setServiceId(client);
            clientService.setClientId(id + "");
            clientServiceMapper.insertSelective(clientService);
        }
    }

    @Override
    public List<Client> getClientServices(int id) {
        return clientMapper.selectAuthorityServiceInfo(id);
    }
}
