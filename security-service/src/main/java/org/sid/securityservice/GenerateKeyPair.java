package org.sid.securityservice;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class GenerateKeyPair {
    public static void main(String[] args) throws  Exception{
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        byte[] pri = keyPair.getPrivate().getEncoded();
        byte[] pub = keyPair.getPublic().getEncoded();

        PemWriter pemWriter=new PemWriter(new OutputStreamWriter(new FileOutputStream("pub.pem")));
        PemObject pemObject=new PemObject("Public Key",pub);
        pemWriter.writeObject(pemObject);
        pemWriter.close();


        PemWriter pemWriter2=new PemWriter(new OutputStreamWriter(new FileOutputStream("pri.pem")));
        PemObject pemObject2=new PemObject("Private Key",pri);
        pemWriter2.writeObject(pemObject2);
        pemWriter2.close();

    }
}
