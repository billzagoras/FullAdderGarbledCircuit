package yao.gate;

import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Random;

public class Utilities
{
    private static int AES_128 = 128;
    private static int INNUM = 129;
    // This function transforms a BitSet object to a byte array object.
    public static byte[] toByteArray1(BitSet bs)
    {
        byte[] bytes = new byte[16];
        int size = bs.size();
        for (int i = 0; i < 128; i++)
        {
            if (bs.get(i))
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
        }
        return bytes;
    }

    //Randomize the encoding of 0 and 1 binary  representation
    public static BitSet randomizeBitSet(BitSet bs, int length)
    {
        int tmp;
        Random rand = new Random();
        for(int i = 0; i < length; i++)
        {
            tmp = rand.nextInt(2);
            if(tmp == 1)
                bs.set(i);
            else
                bs.clear(i);
        }
        return bs;
    }

    // This function selects the correct ciphertext based on the colouring bits given from the input wires of gate.
    public static byte[] pointAndPermute(final Wire wireA, final Wire wireB, Gate gate, SecretKey IV) throws Exception {
        Boolean flag0 = wireA.getEncoding().get(INNUM);
        Boolean flag1 = wireB.getEncoding().get(INNUM);
        if(!flag0 && !flag1)
        {
                byte[] decryptedString = CryptoManager.decrypt(Utilities.toByteArray1(wireB.getEncoding().get(0, AES_128)), IV.getEncoded(), (CryptoManager.decrypt(Utilities.toByteArray1(wireA.getEncoding().get(0, AES_128)), IV.getEncoded(), gate.cipherText0)));
                return decryptedString;
        }
        else if(!flag0 && flag1)
        {
                byte[] decryptedString = CryptoManager.decrypt(Utilities.toByteArray1(wireB.getEncoding().get(0, AES_128)), IV.getEncoded(), (CryptoManager.decrypt(Utilities.toByteArray1(wireA.getEncoding().get(0, AES_128)), IV.getEncoded(), gate.cipherText1)));
                return decryptedString;
        }
        else if(flag0 && !flag1)
        {
                byte[] decryptedString = CryptoManager.decrypt(Utilities.toByteArray1(wireB.getEncoding().get(0, AES_128)), IV.getEncoded(), (CryptoManager.decrypt(Utilities.toByteArray1(wireA.getEncoding().get(0, AES_128)), IV.getEncoded(), gate.cipherText2)));
                return decryptedString;
        }
        else
        {
                byte[] decryptedString = CryptoManager.decrypt(Utilities.toByteArray1(wireB.getEncoding().get(0, AES_128)), IV.getEncoded(), (CryptoManager.decrypt(Utilities.toByteArray1(wireA.getEncoding().get(0, AES_128)), IV.getEncoded(), gate.cipherText3)));
                return decryptedString;
        }
    }
}