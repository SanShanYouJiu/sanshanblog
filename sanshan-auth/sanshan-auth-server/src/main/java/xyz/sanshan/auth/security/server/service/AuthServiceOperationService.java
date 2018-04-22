package xyz.sanshan.auth.security.server.service;

import xyz.sanshan.auth.security.server.entity.Client;

import java.util.List;

public interface AuthServiceOperationService {
    void modifyClientServices(int id, List<String> clients);

    List<Client> getClientServices(int id);
}
