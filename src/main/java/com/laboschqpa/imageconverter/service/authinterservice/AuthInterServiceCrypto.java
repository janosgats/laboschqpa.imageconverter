package com.laboschqpa.imageconverter.service.authinterservice;

public interface AuthInterServiceCrypto {
    boolean isHeaderValid(String authInterServiceHeader);

    String generateHeader();
}
