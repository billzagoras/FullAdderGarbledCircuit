package yao.gate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class XOR extends Gate
{
    private static int AES_128 = 128;
    public
    XOR(final Wire wireA0, final Wire wireA1, final Wire wireB0, final Wire wireB1, final Wire wireZ0, final Wire wireZ1, final SecretKey IV) throws Exception
    {
        createCiphertexts(wireA0, wireA1, wireB0, wireB1, wireZ0, wireZ1, IV);
    }

    //Encrypted Truth Table Outputs
    @Override
    public void createCiphertexts(final Wire wireA0, final Wire wireA1, final Wire wireB0, final Wire wireB1, final Wire wireZ0, final Wire wireZ1, final SecretKey IV) throws Exception
    {
        // Generate the corresponding ciphertext quartet by encrypting the outpout encoding with the input encodings.
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoManager.ALGORITHM);
        keyGenerator.init(AES_128);
        this.cipherText0 = CryptoManager.encrypt(Utilities.toByteArray1(wireA0.getEncoding().get(0, AES_128)), IV.getEncoded(), CryptoManager.encrypt(Utilities.toByteArray1(wireB0.getEncoding().get(0, AES_128)), IV.getEncoded(), Utilities.toByteArray1(wireZ0.getEncoding())));
        this.cipherText1 = CryptoManager.encrypt(Utilities.toByteArray1(wireA0.getEncoding().get(0, AES_128)), IV.getEncoded(), CryptoManager.encrypt(Utilities.toByteArray1(wireB1.getEncoding().get(0, AES_128)), IV.getEncoded(), Utilities.toByteArray1(wireZ1.getEncoding())));
        this.cipherText2 = CryptoManager.encrypt(Utilities.toByteArray1(wireA1.getEncoding().get(0, AES_128)), IV.getEncoded(), CryptoManager.encrypt(Utilities.toByteArray1(wireB0.getEncoding().get(0, AES_128)), IV.getEncoded(), Utilities.toByteArray1(wireZ1.getEncoding())));
        this.cipherText3 = CryptoManager.encrypt(Utilities.toByteArray1(wireA1.getEncoding().get(0, AES_128)), IV.getEncoded(), CryptoManager.encrypt(Utilities.toByteArray1(wireB1.getEncoding().get(0, AES_128)), IV.getEncoded(), Utilities.toByteArray1(wireZ0.getEncoding())));

        // Sort ciphertexts ascending, based on the bits used for the point and permute technique.
        byte[] tmp;
        if(wireA0.getEncoding().get(INNUM))
            if(wireB0.getEncoding().get(INNUM))
            {
                tmp = this.cipherText0;
                this.cipherText0 = this.cipherText3;
                this.cipherText3 = tmp;
                tmp = this.cipherText1;
                this.cipherText1 = this.cipherText2;
                this.cipherText2 = tmp;
            }
            else
            {
                tmp = this.cipherText0;
                this.cipherText0 = this.cipherText2;
                this.cipherText2 = tmp;
                tmp = this.cipherText1;
                this.cipherText1 = this.cipherText3;
                this.cipherText3 = tmp;
            }
        else
            if(wireB0.getEncoding().get(INNUM))
            {
                tmp = this.cipherText0;
                this.cipherText0 = this.cipherText1;
                this.cipherText1 = tmp;
                tmp = this.cipherText2;
                this.cipherText2 = this.cipherText3;
                this.cipherText3 = tmp;
            }
    }
}