package ru.ineureka.patents.service.client;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.client.Client;
import ru.ineureka.patents.persistence.client.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAll() {
        return clientRepository.getAllBy();
    }
}
