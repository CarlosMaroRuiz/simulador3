package org.example.models;

import org.example.threads.Client;

public class Pending {
    private Client client;
    private  Cordenads tablePoint;

    public Pending (Client client,Cordenads cordenads){
         this.client = client;
         this.tablePoint = cordenads;
    }

    public Client getClient() {
        return client;
    }

    public Cordenads getTablePoint() {
        return tablePoint;
    }
}
