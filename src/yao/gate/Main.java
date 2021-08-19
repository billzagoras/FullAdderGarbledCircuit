package yao.gate;

import java.util.BitSet;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Main
{
    private static final int INNUM = 129;
    private static int AES_128 = 128;
    private static final int N = 5;

    public static void main(String[] args) throws Exception
    {
        int S[] = {0, 0, 0, 0, 0, 0};
        int C[] = {0, 0, 0, 0, 0};
        Wire wireZ40replica = new Wire();
        Wire wireZ41replica = new Wire();
        byte[] or0output_prev = new byte[128];
        boolean LoSTbit = false;
        //Alice
         for(int round = 0; round < 2; round++)
         {
            for (int count = N; count > 1 - round; count--)
            {
                // randomize inputs
                BitSet inputs;
                // Random bits for integers A and B
                inputs = new BitSet(2);
                inputs = Utilities.randomizeBitSet(inputs, 2);
                boolean Alpha; // 1st additive's bit.
                if (count != 1)
                    Alpha = inputs.get(1);
                else
                    Alpha = LoSTbit;
                boolean Beta;  // 2nd additive's bit.
                if (round == 0)
                {
                    Beta = inputs.get(1);
                }
                else
                {
                    if (S[count] == 0)
                        Beta = false;
                    else
                        Beta = true;
                }
                // Create key & IV for AES algorithm.
                KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoManager.ALGORITHM);
                keyGenerator.init(AES_128);
                SecretKey IV = keyGenerator.generateKey();

                // xor0 gate creation
                Wire wireA0 = new Wire();
                // wireA1 is created so that it gets the opposite colouring bit of wireA0.
                Wire wireA1 = new Wire(!wireA0.getEncoding().get(INNUM));
                Wire wireB0 = new Wire();
                // wireB1 is created so that it gets the opposite colouring bit of wireB0.
                Wire wireB1 = new Wire(!wireB0.getEncoding().get(INNUM));
                Wire wireZ00 = new Wire();
                // wireZ01 is created so that it gets the opposite colouring bit of wireZ00.
                Wire wireZ01 = new Wire(!wireZ00.getEncoding().get(INNUM));
                XOR xor0 = new XOR(wireA0, wireA1, wireB0, wireB1, wireZ00, wireZ01, IV);

                // and0 gate creation
                Wire wireZ10 = new Wire();
                Wire wireZ11 = new Wire(!wireZ10.getEncoding().get(INNUM));
                AND and0 = new AND(wireA0, wireA1, wireB0, wireB1, wireZ10, wireZ11, IV);

                //xor1 gate creation
                Wire wireZ20in = new Wire(wireZ00);
                Wire wireZ21in = new Wire(wireZ01, !wireZ20in.getEncoding().get(INNUM));

                Wire wireZ20;
                XOR xor1;
                Wire wireZ21;
                Wire wireZ30in;
                Wire wireZ31in;
                Wire wireZ40in;
                Wire wireZ41in;

                if (count == N)
                {
                    wireZ30in = new Wire();
                    wireZ31in = new Wire(!wireZ30in.getEncoding().get(INNUM));
                    wireZ20 = new Wire();
                    wireZ21 = new Wire(!wireZ20.getEncoding().get(INNUM));
                    xor1 = new XOR(wireZ20in, wireZ21in, wireZ30in, wireZ31in, wireZ20, wireZ21, IV); // S
                }
                else
                {
                    wireZ30in = new Wire(wireZ40replica);
                    wireZ31in = new Wire(wireZ41replica, !wireZ30in.getEncoding().get(INNUM));
                    wireZ20 = new Wire();
                    wireZ21 = new Wire(!wireZ20.getEncoding().get(INNUM));
                    xor1 = new XOR(wireZ20in, wireZ21in, wireZ30in, wireZ31in, wireZ20, wireZ21, IV); // S
                }


            //and1 gate creation

                if (count == N)
                {
                    wireZ40in = new Wire();
                    wireZ41in = new Wire(!wireZ40in.getEncoding().get(INNUM));
                }
                else
                {
                    wireZ40in = new Wire(wireZ40replica);
                    wireZ41in = new Wire(wireZ41replica, !wireZ40in.getEncoding().get(INNUM));
                }

                Wire wireZ50in = new Wire(wireZ00);
                Wire wireZ51in = new Wire(wireZ01, !wireZ50in.getEncoding().get(INNUM));

                Wire wireZ30 = new Wire();
                Wire wireZ31 = new Wire(!wireZ30.getEncoding().get(INNUM)); //2
                AND and1 = new AND(wireZ40in, wireZ41in, wireZ50in, wireZ51in, wireZ30, wireZ31, IV);


            //or0 gate creation

                Wire wireZ60in = new Wire(wireZ30);
                Wire wireZ61in = new Wire(wireZ31, !wireZ60in.getEncoding().get(INNUM));

                Wire wireZ70in = new Wire(wireZ10);
                Wire wireZ71in = new Wire(wireZ11, !wireZ70in.getEncoding().get(INNUM));

                Wire wireZ40 = new Wire();
                Wire wireZ41 = new Wire(!wireZ40.getEncoding().get(INNUM)); //2

                // Prepare next Cin!
                wireZ40replica = new Wire(wireZ40);
                wireZ41replica = new Wire(wireZ41, !wireZ40replica.getEncoding().get(INNUM));

                OR or0 = new OR(wireZ60in, wireZ61in, wireZ70in, wireZ71in, wireZ40, wireZ41, IV);




            //BoB - debugging
                byte[] xor0output;
                byte[] xor0ZERO = Utilities.toByteArray1(wireZ00.getEncoding());
                byte[] xor0ONE = Utilities.toByteArray1(wireZ01.getEncoding());
                byte[] and0output;
                byte[] and0ZERO = Utilities.toByteArray1(wireZ10.getEncoding());
                byte[] and0ONE = Utilities.toByteArray1(wireZ11.getEncoding());

                byte[] wireZ20BYTE = Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128)); //debugging
                byte[] wireZ21BYTE = Utilities.toByteArray1(wireZ21in.getEncoding().get(0, AES_128));
                byte[] xor1output;
                byte[] xor1ZEROout = Utilities.toByteArray1(wireZ20.getEncoding());
                byte[] xor1ONEout = Utilities.toByteArray1(wireZ21.getEncoding());

                byte[] wireZ30BYTE = Utilities.toByteArray1(wireZ50in.getEncoding().get(0, AES_128)); //debugging
                byte[] wireZ31BYTE = Utilities.toByteArray1(wireZ51in.getEncoding().get(0, AES_128));
                byte[] and1output;
                byte[] and1ZEROout = Utilities.toByteArray1(wireZ30.getEncoding());
                byte[] and1ONEout = Utilities.toByteArray1(wireZ31.getEncoding());


                byte[] wireZ40BYTE = Utilities.toByteArray1(wireZ60in.getEncoding().get(0, AES_128)); //debugging
                byte[] wireZ41BYTE = Utilities.toByteArray1(wireZ61in.getEncoding().get(0, AES_128));

                byte[] wireZ50BYTE = Utilities.toByteArray1(wireZ70in.getEncoding().get(0, AES_128)); //debugging

                byte[] or0output;
                byte[] or0ZEROout = Utilities.toByteArray1(wireZ40.getEncoding());
                byte[] or0ONEout = Utilities.toByteArray1(wireZ41.getEncoding());

                if (Alpha && Beta) {
                    xor0output = Utilities.pointAndPermute(wireA1, wireB1, xor0, IV);
                    and0output = Utilities.pointAndPermute(wireA1, wireB1, and0, IV);
                } else if (Alpha && !Beta) {
                    xor0output = Utilities.pointAndPermute(wireA1, wireB0, xor0, IV);
                    and0output = Utilities.pointAndPermute(wireA1, wireB0, and0, IV);
                } else if (!Alpha && Beta) {
                    xor0output = Utilities.pointAndPermute(wireA0, wireB1, xor0, IV);
                    and0output = Utilities.pointAndPermute(wireA0, wireB1, and0, IV);
                } else {
                    xor0output = Utilities.pointAndPermute(wireA0, wireB0, xor0, IV);
                    and0output = Utilities.pointAndPermute(wireA0, wireB0, and0, IV);
                }

                // xor1 - S calculation
             if(count == N)
             {
                 int x = 0;
                 int i = 0;
                 do {
                     x = Byte.valueOf(xor0output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))[i]));
                     i++;
                 } while (x == 0 && i != 16);

                 if (x == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                     xor1output = Utilities.pointAndPermute(wireZ20in, wireZ30in, xor1, IV);
                 else
                     xor1output = Utilities.pointAndPermute(wireZ21in, wireZ30in, xor1, IV);
             }
             else
             {
                 int x = 0;
                 int i = 0;
                 do {
                     x = Byte.valueOf(xor0output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))[i]));
                     i++;
                 } while (x == 0 && i != 16);

                 int y = 0;
                 i = 0;
                 do {
                     y = Byte.valueOf(or0output_prev[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ30in.getEncoding().get(0, AES_128))[i]));
                     i++;
                 } while (y == 0 && i != 16);

                 if (x == 0 && y == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                     xor1output = Utilities.pointAndPermute(wireZ20in, wireZ30in, xor1, IV);
                 else if (x == 0 && y != 0)
                     xor1output = Utilities.pointAndPermute(wireZ20in, wireZ31in, xor1, IV);
                 else if (x != 0 && y == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                     xor1output = Utilities.pointAndPermute(wireZ21in, wireZ30in, xor1, IV);
                 else
                     xor1output = Utilities.pointAndPermute(wireZ21in, wireZ31in, xor1, IV);
             }



            // and1
                int x = 0;
                int y = 0;
                int i = 0;
                if (count == N)
                {
                    do {
                        x = Byte.valueOf(xor0output[i]).compareTo(Byte.valueOf(wireZ30BYTE[i]));
                        i++;
                    } while (x == 0 && i != 16);

                    if (x == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                        and1output = Utilities.pointAndPermute(wireZ40in, wireZ50in, and1, IV);
                    else
                        and1output = Utilities.pointAndPermute(wireZ40in, wireZ51in, and1, IV);
                }
                else
                {
                    do {
                        x = Byte.valueOf(or0output_prev[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ40in.getEncoding().get(0, AES_128))[i]));
                        i++;
                    } while (x == 0 && i != 16);

                    i = 0;
                    y = 0;
                    do {
                        y = Byte.valueOf(xor0output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ50in.getEncoding().get(0, AES_128))[i]));
                        i++;
                    } while (y == 0 && i != 16);

                    if (x == 0 && y == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                        and1output = Utilities.pointAndPermute(wireZ40in, wireZ50in, and1, IV);
                    else if (x == 0 && y != 0)
                        and1output = Utilities.pointAndPermute(wireZ40in, wireZ51in, and1, IV);
                    else if (x != 0 && y == 0)
                        and1output = Utilities.pointAndPermute(wireZ41in, wireZ50in, and1, IV);
                    else
                        and1output = Utilities.pointAndPermute(wireZ41in, wireZ51in, and1, IV);
                }


            // or0 - COUT calculation
                x = 0;
                i = 0;
                do {
                    x = Byte.valueOf(and1output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ60in.getEncoding().get(0, AES_128))[i]));
                    i++;
                } while (x == 0 && i != 16);

                y = 0;
                i = 0;
                do {
                    y = Byte.valueOf(and0output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ70in.getEncoding().get(0, AES_128))[i]));
                    i++;
                } while (y == 0 && i != 16);

                if (x == 0 && y == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                    or0output = Utilities.pointAndPermute(wireZ60in, wireZ70in, or0, IV);
                else if (x == 0 && y != 0)
                    or0output = Utilities.pointAndPermute(wireZ60in, wireZ71in, or0, IV);
                else if (x != 0 && y == 0)//xor0output == Utilities.toByteArray1(wireZ20in.getEncoding().get(0, AES_128))) //toByteArray1!!!1   OR check (x == 0)
                    or0output = Utilities.pointAndPermute(wireZ61in, wireZ70in, or0, IV);
                else
                    or0output = Utilities.pointAndPermute(wireZ61in, wireZ71in, or0, IV);
                or0output_prev = or0output; //keep cout for next round


            // LOCAL OUTPUTS - debugging
                byte[] xor0z00 = Utilities.toByteArray1(wireZ00.getEncoding());
                byte[] xor0z01 = Utilities.toByteArray1(wireZ01.getEncoding());

                byte[] and0z10 = Utilities.toByteArray1(wireZ10.getEncoding());
                byte[] and0z11 = Utilities.toByteArray1(wireZ11.getEncoding());

                byte[] xor1z10 = Utilities.toByteArray1(wireZ20.getEncoding());
                byte[] xor1z11 = Utilities.toByteArray1(wireZ21.getEncoding());

                byte[] and1z10 = Utilities.toByteArray1(wireZ30.getEncoding());
                byte[] and1z11 = Utilities.toByteArray1(wireZ31.getEncoding());

                byte[] or0z10 = Utilities.toByteArray1(wireZ40.getEncoding());
                byte[] or0z11 = Utilities.toByteArray1(wireZ41.getEncoding());

                // Identification of output S
                i = 0;
                int z = 0;
                do {
                    z = Byte.valueOf(xor1output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ20.getEncoding())[i]));
                    i++;
                } while (z == 0 && i != 16);
                if (z == 0)
                    S[count] = 0;
                else
                    S[count] = 1;
                    // Identification of output C
                i = 0;
                z = 0;
                do {
                    z = Byte.valueOf(or0output[i]).compareTo(Byte.valueOf(Utilities.toByteArray1(wireZ40.getEncoding())[i]));
                    i++;
                } while (z == 0 && i != 16);
                if (z == 0)
                    C[count - 1] = 0;
                else
                    C[count - 1] = 1;
                if (round == 0 && count == 2)
                    S[1] = C[1]; //last complement goes to the Sum by default
                else if (round == 1 && count == 2)
                {
                    if (C[1] == 1)
                        LoSTbit = true;
                    else
                        LoSTbit = false;
                }
                else if (count == 1)
                    S[0] = C[0];
            }
         }
    }
}