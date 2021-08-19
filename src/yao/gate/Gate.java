package yao.gate;

import javax.crypto.SecretKey;

public abstract class Gate
{
    public final int INNUM = 129;

    public
    Gate() { }
    byte[] cipherText0;
    byte[] cipherText1;
    byte[] cipherText2;
    byte[] cipherText3;

    //Encrypted Truth Table Outputs
    public abstract void createCiphertexts(final Wire wireA0, final Wire wireA1, final Wire wireB0, final Wire wireB1, final Wire wireZ0, final Wire wireZ1, final SecretKey IV) throws Exception;
}